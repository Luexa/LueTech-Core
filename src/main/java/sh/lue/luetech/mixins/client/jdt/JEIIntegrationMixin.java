package sh.lue.luetech.mixins.client.jdt;

import com.direwolf20.justdirethings.client.jei.GooSpreadRecipeCategory;
import com.direwolf20.justdirethings.client.jei.GooSpreadRecipeTagCategory;
import com.direwolf20.justdirethings.client.jei.JEIIntegration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.lue.luetech.data.LTBlocks;

@Mixin(value = JEIIntegration.class, remap = false)
public abstract class JEIIntegrationMixin implements IModPlugin {
    @Inject(method = "registerRecipeCatalysts", at = @At("TAIL"))
    private void luetech$injectCustomCatalyst(IRecipeCatalystRegistration registry, CallbackInfo ci) {
        registry.addRecipeCatalyst(LTBlocks.TIMEWIND_GOO.asStack(), GooSpreadRecipeCategory.TYPE);
        registry.addRecipeCatalyst(LTBlocks.TIMEWIND_GOO.asStack(), GooSpreadRecipeTagCategory.TYPE);
    }
}
