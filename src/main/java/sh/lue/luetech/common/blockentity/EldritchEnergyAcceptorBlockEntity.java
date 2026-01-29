package sh.lue.luetech.common.blockentity;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.networking.events.GridPowerStorageStateChanged;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.util.AECableType;
import appeng.blockentity.grid.AENetworkedBlockEntity;
import appeng.me.energy.StoredEnergyAmount;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LTCompat;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.api.IBeaconConnected;
import sh.lue.luetech.common.saveddata.beacon.BeaconNetwork;
import sh.lue.luetech.data.LTBlocks;

import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;

public class EldritchEnergyAcceptorBlockEntity extends AENetworkedBlockEntity implements IAEPowerStorage, IGridTickable, IBeaconConnected {
    @ApiStatus.Internal
    public static Set<EldritchEnergyAcceptorBlockEntity> INSTANCES = new ObjectOpenHashSet<>();

    @Nullable
    private UUID networkUUID;
    private static final BigInteger MAX_SAFE_POWER = BigInteger.valueOf((long)(StoredEnergyAmount.MAX_MAXIMUM / 2));
    private static final double MAX_MAXIMUM = (double)MAX_SAFE_POWER.longValue() * LTCompat.aePerEu();

    private double buffer;
    private long lastTimeStamp = Long.MIN_VALUE;
    private double usedLastTick;
    private double usedThisTick;

    public EldritchEnergyAcceptorBlockEntity(BlockPos pos, BlockState state) {
        super(LTBlocks.ELDRITCH_ENERGY_ACCEPTOR_ENTITY.get(), pos, state);
        getMainNode()
                .setIdlePowerUsage(0)
                .addService(IAEPowerStorage.class, this)
                .addService(IGridTickable.class, this)
                .setVisualRepresentation(state.getBlock());
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.COVERED;
    }

    @Override
    public void onReady() {
        super.onReady();
    }

    @Override
    public AccessRestriction getPowerFlow() {
        return AccessRestriction.READ;
    }

    @Override
    public boolean isAEPublicPowerStorage() {
        return true;
    }

    @Override
    public double getAEMaxPower() {
        return MAX_MAXIMUM;
    }

    @Override
    public double getAECurrentPower() {
        var network = getConnectedBeaconNetwork();
        if (network == null) return 0;
        var networkPower = network.getStoredPower();
        if (networkPower.compareTo(MAX_SAFE_POWER) >= 0) {
            return MAX_MAXIMUM;
        } else {
            return (double)networkPower.longValue() * LTCompat.aePerEu();
        }
    }

    @Override
    public double extractAEPower(double amt, Actionable mode, PowerMultiplier pm) {
        return pm.divide(extractAEPower(pm.multiply(amt), mode));
    }

    private double extractAEPower(double amt, Actionable mode) {
        if (amt <= 0 || !(getLevel() instanceof ServerLevel)) return 0;
        if (amt <= buffer) {
            if (!mode.isSimulate()) {
                buffer -= amt;
                addToTickStatistic(amt);
            }
            return amt;
        }
        var network = getConnectedBeaconNetwork();
        if (network == null || !network.getActive()) return 0;
        var networkPower = network.getStoredPower();
        if (mode.isSimulate()) {
            if (networkPower.compareTo(MAX_SAFE_POWER) >= 0) {
                return amt;
            } else {
                double available = (double)networkPower.longValue() * LTCompat.aePerEu() + buffer;
                return Math.min(MAX_MAXIMUM, Math.min(amt, available));
            }
        }
        double euRequired = Math.ceil((amt - buffer) / LTCompat.aePerEu());
        var euToPull = networkPower.min(BigInteger.valueOf((long)euRequired));
        network.setStoredPower(networkPower.subtract(euToPull));
        buffer += (double)euToPull.longValue() * LTCompat.aePerEu();
        if (buffer >= amt) {
            buffer -= amt;
            addToTickStatistic(amt);
            return amt;
        }
        double result = buffer;
        buffer = 0;
        addToTickStatistic(result);
        return result;
    }

    @ApiStatus.Internal
    public void stockBuffer() {
        double toStock = Math.min(MAX_MAXIMUM, Math.max(usedLastTick, usedThisTick) * 3) - buffer;
        if (toStock <= 0) return;
        var network = getConnectedBeaconNetwork();
        if (network == null || !network.getActive()) return;
        var networkPower = network.getStoredPower();
        double euRequired = Math.ceil(toStock / LTCompat.aePerEu());
        var euToPull = networkPower.min(BigInteger.valueOf((long)euRequired));
        network.setStoredPower(networkPower.subtract(euToPull));
        buffer += (double)euToPull.longValue() * LTCompat.aePerEu();
    }

    private void addToTickStatistic(double amt) {
        if (lastTimeStamp == LueTech.tickCount - 1) {
            usedLastTick = usedThisTick;
            usedThisTick = 0L;
        } else if (lastTimeStamp < LueTech.tickCount) {
            usedLastTick = 0L;
            usedThisTick = 0L;
        }
        lastTimeStamp = LueTech.tickCount;
        usedThisTick += amt;
    }

    @Override
    public double injectAEPower(double amt, Actionable mode) {
        // Refuse to store any AE as the conversion EU->AE is one way.
        return amt;
    }

    @Override
    public int getPriority() {
        // Highest priority besides creative energy cells.
        return Integer.MAX_VALUE - 1;
    }

    @Override
    public void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        if (networkUUID != null) {
            data.putIntArray("beaconNetwork", UUIDUtil.uuidToIntArray(networkUUID));
        }
        if (buffer > 0) {
            data.putDouble("buffer", buffer);
        }
    }

    @Override
    public void loadTag(CompoundTag data, HolderLookup.Provider registries) {
        super.loadTag(data, registries);
        if (data.contains("beaconNetwork", Tag.TAG_INT_ARRAY)) {
            var intArray = data.getIntArray("beaconNetwork");
            if (intArray.length == 4) {
                networkUUID = UUIDUtil.uuidFromIntArray(intArray);
            }
        }
        if (data.contains("buffer", Tag.TAG_DOUBLE)) {
            buffer = Math.max(0, data.getDouble("buffer"));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel) {
            INSTANCES.add(this);
            if (networkUUID != null) {
                getMainNode().ifPresent(grid ->
                        grid.postEvent(new GridPowerStorageStateChanged(this,
                                GridPowerStorageStateChanged.PowerEventType.PROVIDE_POWER)));
            }
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (getLevel() instanceof ServerLevel) {
            INSTANCES.remove(this);
        }
    }

    @Override
    public boolean isBeaconConnected() {
        return true;
    }

    @Override
    @Nullable
    public BeaconNetwork getConnectedBeaconNetwork() {
        return networkUUID == null ? null : LueTech.savedData.dominanceBeacon.getNetwork(networkUUID);
    }

    @Override
    public void setBeaconNetwork(@Nullable UUID networkUUID) {
        this.networkUUID = networkUUID;
        if (networkUUID != null) {
            getMainNode().ifPresent(grid ->
                    grid.postEvent(new GridPowerStorageStateChanged(this, GridPowerStorageStateChanged.PowerEventType.PROVIDE_POWER)));
        }
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(20, 20, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        if (getLevel() instanceof ServerLevel) {
            var network = getConnectedBeaconNetwork();
            if (network != null && network.getActive() && network.getStoredPower().signum() > 0) {
                getMainNode().ifPresent(grid ->
                        grid.postEvent(new GridPowerStorageStateChanged(this, GridPowerStorageStateChanged.PowerEventType.PROVIDE_POWER)));
            }
        }
        return TickRateModulation.IDLE;
    }
}
