package sh.lue.luetech.mixins.gtceu;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.BeaconSingleblockConnections;
import sh.lue.luetech.common.machine.IBeaconConnected;
import sh.lue.luetech.common.saveddata.beacon.BeaconNetwork;

import java.util.UUID;

@Mixin(value = MetaMachine.class, remap = false)
@Implements(@Interface(iface = IBeaconConnected.class, prefix = "beacon$"))
public abstract class MetaMachineMixin {
    @Unique
    @Nullable
    private UUID luetech$beaconNetworkUUID;

    public boolean beacon$isBeaconConnected() {
        return luetech$beaconNetworkUUID != null;
    }

    @Nullable
    public BeaconNetwork beacon$getConnectedBeaconNetwork() {
        return luetech$beaconNetworkUUID == null ? null :
                LueTech.savedData.dominanceBeacon.getNetwork(luetech$beaconNetworkUUID);
    }

    public void beacon$setBeaconNetwork(@Nullable UUID networkUUID) {
        var machine = (IBeaconConnected)this;
        luetech$beaconNetworkUUID = networkUUID;
        if (networkUUID != null) {
            BeaconSingleblockConnections.register(machine, networkUUID);
        } else {
            BeaconSingleblockConnections.deregister(machine);
        }
    }

    @Inject(method = "onLoad", at = @At("TAIL"))
    private void luetech$registerBeaconNetwork(CallbackInfo ci) {
        MetaMachine machine = (MetaMachine)(Object)this;
        if (luetech$beaconNetworkUUID != null && machine.getLevel() instanceof ServerLevel) {
            BeaconSingleblockConnections.register((IBeaconConnected)machine, luetech$beaconNetworkUUID);
        }
    }

    @Inject(method = "onUnload", at = @At("TAIL"))
    private void luetech$deregisterBeaconNetwork(CallbackInfo ci) {
        MetaMachine machine = (MetaMachine)(Object)this;
        if (machine.getLevel() instanceof ServerLevel) {
            BeaconSingleblockConnections.deregister((IBeaconConnected)machine);
        }
    }

    @Inject(method = "loadCustomPersistedData", at = @At("TAIL"))
    private void luetech$loadBeaconNetwork(@NotNull CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("luetechBeaconNetwork", Tag.TAG_INT_ARRAY)) {
            UUID networkUUID;
            try {
                networkUUID = UUIDUtil.uuidFromIntArray(tag.getIntArray("luetechBeaconNetwork"));
            } catch (Exception ignored) {
                return;
            }
            luetech$beaconNetworkUUID = networkUUID;
        }
    }

    @Inject(method = "saveCustomPersistedData", at = @At("TAIL"))
    private void luetech$saveBeaconNetwork(@NotNull CompoundTag tag, boolean forDrop, CallbackInfo ci) {
        if (forDrop) return;
        if (luetech$beaconNetworkUUID != null) {
            tag.putIntArray("luetechBeaconNetwork", UUIDUtil.uuidToIntArray(luetech$beaconNetworkUUID));
        }
    }
}
