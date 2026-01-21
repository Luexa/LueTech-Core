package sh.lue.luetech.mixins.gtceu;

import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.lue.luetech.data.dynamic.LootHelper;

import java.util.List;

@Mixin(value = ToolHelper.class, remap = false)
public class ToolHelperMixin {
    @Inject(method = "applyHammerDropConversion",
            at = @At(value = "INVOKE",
                     target = "Lcom/gregtechceu/gtceu/api/item/tool/ToolHelper;getSilkTouchDrop(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/List;",
                     ordinal = 0),
            cancellable = true)
    private static void luetech$applyCustomOreHammerDrops(ServerLevel world,
                                                          BlockPos pos,
                                                          ItemStack tool,
                                                          BlockState state,
                                                          List<ItemStack> drops,
                                                          int fortune,
                                                          float dropChance,
                                                          RandomSource random,
                                                          CallbackInfo ci) {
        if (state != null && LootHelper.isCustomOre(state)) {
            LootHelper.applyCustomOreHammerDrops(drops);
            ci.cancel();
        }
    }
}
