package sh.lue.luetech.mixins.jade;

import appeng.blockentity.powersink.AEBasePoweredBlockEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import snownee.jade.addon.universal.EnergyStorageProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;

@Mixin(value = EnergyStorageProvider.Extension.class, remap = false)
public class EnergyStorageProviderExtensionMixin {
    @WrapMethod(method = "shouldRequestData")
    private boolean luetech$suppressDuplicatePowerInfo(Accessor<?> accessor, Operation<Boolean> original) {
        if (accessor instanceof BlockAccessor blockAccessor) {
            if (blockAccessor.getBlockEntity() instanceof AEBasePoweredBlockEntity) {
                return false;
            }
        }
        return original.call(accessor);
    }
}
