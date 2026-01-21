package sh.lue.luetech.mixins.gtceu;

import com.gregtechceu.gtceu.data.fluid.GTFluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.lue.luetech.data.LTCreativeModeTabs;

import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;

@Mixin(value = GTFluids.class, remap = false)
public class GTFluidsMixin {
    @Inject(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;creativeModeTab(Lcom/tterrag/registrate/util/entry/RegistryEntry;)V",
                    ordinal = 0))
    private static void luetech$setMaterialFluidCreativeTab(CallbackInfo ci) {
        REGISTRATE.creativeModeTab(LTCreativeModeTabs.MATERIAL_FLUID);
    }
}
