package sh.lue.luetech.common.machine.multiblock.part;

import com.google.common.base.Predicates;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.IBeaconConnected;
import sh.lue.luetech.common.machine.multiblock.electric.DominanceBeaconMachine;
import sh.lue.luetech.common.saveddata.beacon.BeaconNetwork;
import sh.lue.luetech.utils.BigIntegerUtils;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class EldritchEnergyHatchPartMachine extends EnergyHatchPartMachine implements IBeaconConnected {
    @ApiStatus.Internal
    public static final Map<UUID, Set<EldritchEnergyHatchPartMachine>> NETWORK_MEMBERS = new Object2ObjectOpenHashMap<>();

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            EldritchEnergyHatchPartMachine.class, EnergyHatchPartMachine.MANAGED_FIELD_HOLDER);

    @Nullable
    @Persisted
    private UUID networkUUID;

    @Nullable
    private BeaconNetwork network;

    @Nullable
    @Persisted
    private String customName;

    @Persisted
    private boolean autoDiscover;

    @Nullable
    private TickableSubscription tickSubscription;

    public EldritchEnergyHatchPartMachine(IMachineBlockEntity holder, int tier, boolean uplink, int amperage,
                                          Object... args) {
        super(holder, tier, uplink ? IO.OUT : IO.IN, amperage, args);
        autoDiscover = true;
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public boolean isUplink() {
        return io == IO.OUT;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel) {
            if (autoDiscover && networkUUID == null) {
                var playerUUID = getOwnerUUID();
                if (playerUUID != null) {
                    network = LueTech.savedData.dominanceBeacon.getNetworkForPlayer(playerUUID);
                    if (network != null) {
                        networkUUID = network.getUUID();
                    }
                }
            } else if (networkUUID != null) {
                network = LueTech.savedData.dominanceBeacon.getNetwork(networkUUID);
                if (network == null) {
                    networkUUID = null;
                }
            }
            autoDiscover = false;
            NETWORK_MEMBERS.computeIfAbsent(networkUUID, k -> new ObjectOpenHashSet<>()).add(this);
            updateTickSubscription();
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (getLevel() instanceof ServerLevel) {
            if (networkUUID != null) {
                NETWORK_MEMBERS.computeIfPresent(networkUUID, (k, v) -> {
                    v.remove(this);
                    return v.isEmpty() ? null : v;
                });
            }
            unsubscribeFromTick();
        }
    }

    @ApiStatus.Internal
    public void onDeleteNetwork() {
        network = null;
        networkUUID = null;
        unsubscribeFromTick();
    }

    @ApiStatus.Internal
    public void onNetworkActiveChange() {
        updateTickSubscription();
    }

    @MustBeInvokedByOverriders
    @Override
    public void addedToController(IMultiController controller) {
        super.addedToController(controller);
        if (getLevel() instanceof ServerLevel) {
            if (controller instanceof DominanceBeaconMachine) {
                // Beacon of Dominance can be trusted with this hatch.
                unsubscribeFromTick();
            } else {
                updateTickSubscription();
            }
        }
    }

    @MustBeInvokedByOverriders
    @Override
    public void removedFromController(IMultiController controller) {
        super.removedFromController(controller);
        if (getLevel() instanceof ServerLevel) {
            updateTickSubscription();
        }
    }

    private void unsubscribeFromTick() {
        if (tickSubscription != null) {
            tickSubscription.unsubscribe();
            tickSubscription = null;
        }
    }

    private void updateTickSubscription() {
        boolean shouldTick;
        if (getControllers().isEmpty()) {
            shouldTick = true;
        } else {
            shouldTick = getControllers().stream()
                    .noneMatch(c -> c instanceof DominanceBeaconMachine);
        }
        if (network == null || !network.getActive() || !shouldTick) {
            unsubscribeFromTick();
        } else if (tickSubscription == null) {
            tickSubscription = subscribeServerTick(this::tick);
        }
    }

    private void tick() {
        if (network == null) {
            unsubscribeFromTick();
            return;
        }

        long voltage = GTValues.V[getTier()];
        var networkPower = network.getStoredPower();
        if (io == IO.IN) {
            long space = energyContainer.getEnergyCapacity() - energyContainer.getEnergyStored();
            long ampsToAccept = Math.min(space / voltage, amperage);
            if (ampsToAccept > 0) {
                long networkPowerSaturated = BigIntegerUtils.saturatedValue(networkPower);
                long ampsAvailable = Math.min(networkPowerSaturated / voltage, ampsToAccept);
                long toDrain = voltage * ampsAvailable;
                if (toDrain > 0) {
                    energyContainer.changeEnergy(toDrain);
                    network.setStoredPower(networkPower.subtract(BigInteger.valueOf(toDrain)));
                }
            }
        } else {
            long stored = energyContainer.getEnergyStored();
            var maxNetworkPower = network.getMaxPower();
            if (stored > 0 && networkPower.compareTo(maxNetworkPower) < 0) {
                long transferLimit = voltage * amperage;
                long availableCapacity = BigIntegerUtils.saturatedValue(maxNetworkPower.subtract(networkPower));
                long toPush = Math.min(Math.min(stored, transferLimit), availableCapacity);
                if (toPush > 0) {
                    network.setStoredPower(networkPower.add(BigInteger.valueOf(toPush)));
                    energyContainer.changeEnergy(-toPush);
                }
            }
        }
    }

    @Override
    @NotNull
    protected NotifiableEnergyContainer createEnergyContainer(Object @NotNull ... args) {
        var container = super.createEnergyContainer(args);
        container.setSideInputCondition(Predicates.alwaysFalse());
        container.setSideOutputCondition(Predicates.alwaysFalse());
        container.setCapabilityValidator(Objects::isNull);
        return container;
    }

    @Override
    @Nullable
    public BeaconNetwork getConnectedBeaconNetwork() {
        return network;
    }
}
