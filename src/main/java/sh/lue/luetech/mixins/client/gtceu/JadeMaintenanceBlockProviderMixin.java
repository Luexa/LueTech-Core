package sh.lue.luetech.mixins.client.gtceu;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.integration.jade.provider.MaintenanceBlockProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@Mixin(value = MaintenanceBlockProvider.class, remap = false)
public abstract class JadeMaintenanceBlockProviderMixin extends CapabilityBlockProvider<IMaintenanceMachine> {
    private JadeMaintenanceBlockProviderMixin() { super(null); }

    @Inject(method = "addTooltip", at = @At("HEAD"), cancellable = true)
    private void luetech$fixMaintenanceTooltip(CompoundTag tag, ITooltip tooltip, Player player, BlockAccessor accessor, BlockEntity blockEntity, IPluginConfig config, CallbackInfo ci) {
        if (!tag.contains("hasProblems", CompoundTag.TAG_BYTE)) {
            ci.cancel();
        }
    }
}
