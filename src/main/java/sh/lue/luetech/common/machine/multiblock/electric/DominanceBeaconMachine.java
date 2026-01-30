package sh.lue.luetech.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.api.IBeaconConnected;
import sh.lue.luetech.common.machine.multiblock.UniqueMultiblockMachine;
import sh.lue.luetech.common.saveddata.beacon.BeaconNetwork;
import sh.lue.luetech.utils.BigIntegerUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static sh.lue.luetech.LueTech.savedData;

public class DominanceBeaconMachine extends UniqueMultiblockMachine implements IBeaconConnected {
    @ApiStatus.Internal
    public static final Set<DominanceBeaconMachine> ALL_INSTANCES = new ObjectOpenHashSet<>();

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            DominanceBeaconMachine.class,
            UniqueMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Nullable
    private BeaconNetwork network;

    public DominanceBeaconMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (getLevel() instanceof ServerLevel && allowedToRun) {
            loadNetwork();
            if (isWorkingEnabled() && isFormed()) {
                getRecipeLogic().setStatus(RecipeLogic.Status.WORKING);
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        if (getLevel() instanceof ServerLevel && network != null) {
            network.setActive(false);
        }
    }

    @Override
    protected void onUpdateStatus() {
        if (!allowedToRun) {
            network = null;
        } else {
            loadNetwork();
            if (isWorkingEnabled() && isFormed()) {
                getRecipeLogic().setStatus(RecipeLogic.Status.WORKING);
            }
        }
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        super.setWorkingEnabled(isWorkingAllowed);
        if (allowedToRun && network != null) {
            network.setActive(isWorkingEnabled());
            if (isWorkingEnabled() && isFormed()) {
                getRecipeLogic().setStatus(RecipeLogic.Status.WORKING);
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel) {
            ALL_INSTANCES.add(this);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (getLevel() instanceof ServerLevel) {
            ALL_INSTANCES.remove(this);
        }
    }

    @Override
    public void onMachineRemoved() {
        super.onMachineRemoved();
        if (getLevel() instanceof ServerLevel && network != null) {
            network.setActive(false);
        }
    }

    private void loadNetwork() {
        var playerUUID = getOwnerUUID();
        if (playerUUID == null) {
            network = null;
            return;
        }
        var teamNetwork = savedData.dominanceBeacon.teamNetwork(teamUUID);
        var playerNetwork = savedData.dominanceBeacon.playerNetwork(playerUUID);
        if (teamNetwork != null) {
            network = teamNetwork;
            if (playerNetwork != null && playerNetwork != teamNetwork) {
                savedData.dominanceBeacon.deleteNetwork(playerNetwork.getUUID());
            }
            savedData.dominanceBeacon.setNetworkPlayer(network, playerUUID);
            network.setActive(isFormed() && isWorkingEnabled());
            return;
        }
        if (playerNetwork != null) {
            network = playerNetwork;
            savedData.dominanceBeacon.setNetworkTeam(network, teamUUID);
        }
        if (network == null) {
            network = savedData.dominanceBeacon.createNetwork(playerUUID, teamUUID);
        }
        network.setActive(isFormed() && isWorkingEnabled());
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117)
                .setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150)
                        .clickHandler(this::handleDisplayClick)));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        if (isFormed()) {
            if (!isWorkingEnabled()) {
                if (allowedToRun) {
                    textList.add(Component.translatable("luetech.multiblock.beacon.network_disabled"));
                } else {
                    textList.add(Component.translatable("luetech.multiblock.unique_disabled"));
                }
            } else if (network != null ) {
                textList.add(Component.translatable("luetech.multiblock.beacon.network_active"));
            }
        } else if (!allowedToRun) {
            textList.add(Component.translatable("luetech.multiblock.unique_disabled"));
        }
        getDefinition().getAdditionalDisplay().accept(this, textList);
    }

    @Override
    public boolean isBeaconConnected() {
        return true;
    }

    @Override
    @Nullable
    public BeaconNetwork getConnectedBeaconNetwork() {
        return network;
    }

    @Override
    public void setBeaconNetwork(@Nullable UUID networkUUID) {
    }

    @ApiStatus.Internal
    public void beaconTick() {
        if (!isFormed || !isWorkingEnabled() || network == null || !network.getActive()) return;
        List<IEnergyContainer> containers = new ArrayList<>();
        var handlers = getCapabilitiesFlat(IO.IN, EURecipeCapability.CAP);
        if (handlers.isEmpty()) handlers = getCapabilitiesFlat(IO.OUT, EURecipeCapability.CAP);
        for (IRecipeHandler<?> handler : handlers) {
            if (handler instanceof IEnergyContainer container) {
                containers.add(container);
            }
        }
        var hatchEnergy = BigInteger.ZERO;
        for (var container : containers) {
            hatchEnergy = hatchEnergy.add(BigInteger.valueOf(container.getEnergyStored()));
        }
        if (hatchEnergy.equals(BigInteger.ZERO)) return;
        var networkPower = network.getStoredPower();
        var networkCapacity = network.getMaxPower();
        var energyToPull = networkCapacity
                .subtract(networkPower)
                .max(BigInteger.ZERO)
                .min(hatchEnergy);
        if (energyToPull.signum() > 0) {
            network.setStoredPower(networkPower.add(energyToPull));
            for (var container : containers) {
                if (energyToPull.signum() <= 0) break;
                long removedEnergy = container.removeEnergy(BigIntegerUtils.saturatedLong(energyToPull));
                energyToPull = energyToPull.subtract(BigInteger.valueOf(removedEnergy));
            }
        }
    }
}
