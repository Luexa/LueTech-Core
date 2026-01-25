package sh.lue.luetech.mixins.jdt;

import com.direwolf20.justdirethings.common.blockentities.*;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.BeaconSingleblockConnections;
import sh.lue.luetech.common.machine.IBeaconConnected;
import sh.lue.luetech.common.saveddata.beacon.BeaconNetwork;
import sh.lue.luetech.data.LTAttachments;
import sh.lue.luetech.integration.jdt.JDTNativeEUContainer;
import sh.lue.luetech.integration.neoforge.IEnhancedConvertedEUReceiver;

import java.util.UUID;

@Mixin(remap = false, value = {
        BlockBreakerT2BE.class,
        BlockPlacerT2BE.class,
        ClickerT2BE.class,
        SensorT2BE.class,
        DropperT2BE.class,
        BlockSwapperT2BE.class,
        FluidPlacerT2BE.class,
        FluidCollectorT2BE.class,
        ParadoxMachineBE.class,
})
@Implements({
        @Interface(iface = IEnhancedConvertedEUReceiver.class, prefix = "eureceiver$"),
        @Interface(iface = IBeaconConnected.class, prefix = "beacon$"),
})
public abstract class PoweredMachineSubclassesMixin extends BlockEntity {
    @Unique
    @Nullable
    private JDTNativeEUContainer luetech$container;

    private PoweredMachineSubclassesMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        throw new AssertionError();
    }

    @Inject(method = "getEnergyStorage", at = @At("HEAD"), cancellable = true)
    private void luetech$customEnergyStorage(CallbackInfoReturnable<MachineEnergyStorage> cir) {
        if (luetech$container == null) {
            luetech$container = new JDTNativeEUContainer(this);
        }
        cir.setReturnValue(luetech$container.getWrappedStorage());
    }

    public IEnergyContainer eureceiver$getEnergyContainer(@Nullable Direction side) {
        if (luetech$container == null) {
            luetech$container = new JDTNativeEUContainer(this);
        }
        return luetech$container;
    }

    public boolean beacon$isBeaconConnected() {
        return getData(LTAttachments.BEACON_NETWORK).network != null;
    }

    public void beacon$setBeaconNetwork(@Nullable UUID networkUUID) {
        getData(LTAttachments.BEACON_NETWORK).network = networkUUID;
        if (networkUUID != null) {
            BeaconSingleblockConnections.register((IBeaconConnected)this, networkUUID);
        } else {
            BeaconSingleblockConnections.deregister((IBeaconConnected)this);
        }
    }

    @Nullable
    public BeaconNetwork beacon$getConnectedBeaconNetwork() {
        UUID networkUUID = getData(LTAttachments.BEACON_NETWORK).network;
        return networkUUID == null ? null : LueTech.savedData.dominanceBeacon.getNetwork(networkUUID);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel) {
            var beaconConnected = (IBeaconConnected) this;
            var network = beaconConnected.getConnectedBeaconNetwork();
            if (network != null) {
                BeaconSingleblockConnections.register(beaconConnected, network.getUUID());
            }
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (getLevel() instanceof ServerLevel) {
            BeaconSingleblockConnections.deregister((IBeaconConnected) this);
        }
    }
}
