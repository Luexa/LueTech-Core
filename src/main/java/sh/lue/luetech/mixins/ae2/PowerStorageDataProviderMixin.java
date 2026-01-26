package sh.lue.luetech.mixins.ae2;

import appeng.api.networking.energy.IAEPowerStorage;
import appeng.integration.modules.igtooltip.blocks.PowerStorageDataProvider;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sh.lue.luetech.common.blockentity.EldritchEnergyAcceptorBlockEntity;

@Mixin(value = PowerStorageDataProvider.class, remap = false)
public class PowerStorageDataProviderMixin {
    @Definition(id = "object", local = @Local(type = BlockEntity.class, argsOnly = true))
    @Definition(id = "IAEPowerStorage", type = IAEPowerStorage.class)
    @Expression("object instanceof IAEPowerStorage")
    @WrapOperation(method = "provideServerData(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean luetech$suppressDuplicatePowerInfo(Object object, Operation<Boolean> original) {
        return original.call(object) && !(object instanceof EldritchEnergyAcceptorBlockEntity);
    }
}
