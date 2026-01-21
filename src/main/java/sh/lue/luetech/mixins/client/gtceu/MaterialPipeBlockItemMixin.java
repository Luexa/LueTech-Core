package sh.lue.luetech.mixins.client.gtceu;

import com.gregtechceu.gtceu.api.item.MaterialPipeBlockItem;
import net.minecraft.util.FastColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaterialPipeBlockItem.class)
public class MaterialPipeBlockItemMixin {
    @Inject(method = "lambda$tintColor$0", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private static void luetech$fixColor(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(FastColor.ARGB32.color(255, cir.getReturnValue()));
    }
}
