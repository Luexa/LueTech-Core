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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LTCompat;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.api.IBeaconConnected;
import sh.lue.luetech.common.saveddata.beacon.BeaconNetwork;
import sh.lue.luetech.data.LTBlocks;

import java.math.BigInteger;
import java.util.UUID;

public class EldritchEnergyAcceptorBlockEntity extends AENetworkedBlockEntity implements IAEPowerStorage, IGridTickable, IBeaconConnected {
    @Nullable
    private UUID networkUUID;
    private int excess;
    private static final BigInteger MULTIPLIER = BigInteger.valueOf(10000L);
    private static final BigInteger MAX_SAFE_POWER = BigInteger.valueOf((long)StoredEnergyAmount.MAX_MAXIMUM);

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
        return StoredEnergyAmount.MAX_MAXIMUM;
    }

    @Override
    public double getAECurrentPower() {
        var network = getConnectedBeaconNetwork();
        if (network == null) return 0;
        return Math.min(StoredEnergyAmount.MAX_MAXIMUM, network.getStoredPower().min(MAX_SAFE_POWER).doubleValue() * LTCompat.aePerEu());
    }

    @Override
    public double extractAEPower(double amt, Actionable mode, PowerMultiplier pm) {
        return pm.divide(extractAEPower(pm.multiply(amt), mode));
    }

    private double extractAEPower(double amt, Actionable mode) {
        var network = getConnectedBeaconNetwork();
        if (network == null || !network.getActive()) return 0;
        if (amt > StoredEnergyAmount.MAX_MAXIMUM) {
            amt = StoredEnergyAmount.MAX_MAXIMUM;
        }
        var networkPowerMultiplied = network.getStoredPower().multiply(MULTIPLIER).add(BigInteger.valueOf(excess));
        var euToExtractMultiplied = BigInteger.valueOf((long)(amt / LTCompat.aePerEu() * 10000));
        boolean canExtractFullAmount = false;
        var extractedMultiplied = networkPowerMultiplied;
        if (networkPowerMultiplied.compareTo(euToExtractMultiplied) >= 0) {
            canExtractFullAmount = true;
            extractedMultiplied = euToExtractMultiplied;
            networkPowerMultiplied = networkPowerMultiplied.subtract(extractedMultiplied);
        } else {
            networkPowerMultiplied = BigInteger.ZERO;
        }
        var quotientAndRemainder = networkPowerMultiplied.divideAndRemainder(MULTIPLIER);
        if (!mode.isSimulate()) {
            network.setStoredPower(quotientAndRemainder[0]);
            excess = quotientAndRemainder[1].intValue();
        }
        return canExtractFullAmount ? amt : extractedMultiplied.doubleValue() / 10000 * LTCompat.aePerEu();
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
        if (excess > 0) {
            data.putInt("excess", excess);
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
        if (data.contains("excess", Tag.TAG_INT)) {
            excess = Math.max(0, data.getInt("excess"));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel && networkUUID != null) {
            getMainNode().ifPresent(grid ->
                    grid.postEvent(new GridPowerStorageStateChanged(this, GridPowerStorageStateChanged.PowerEventType.PROVIDE_POWER)));
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
