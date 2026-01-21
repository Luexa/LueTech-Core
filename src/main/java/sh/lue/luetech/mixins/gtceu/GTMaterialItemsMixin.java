package sh.lue.luetech.mixins.gtceu;

import com.gregtechceu.gtceu.data.block.GTBlocks;
import com.gregtechceu.gtceu.data.item.GTMaterialItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.lue.luetech.data.LTCreativeModeTabs;

import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;

@Mixin(value = GTMaterialItems.class, remap = false)
public class GTMaterialItemsMixin {
    @Inject(method = "generateMaterialItems",
            at = @At(value = "INVOKE",
                    target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;creativeModeTab(Lcom/tterrag/registrate/util/entry/RegistryEntry;)V",
                    ordinal = 0))
    private static void luetech$setMaterialItemCreativeTab(CallbackInfo ci) {
        REGISTRATE.creativeModeTab(LTCreativeModeTabs.MATERIAL_ITEM);
    }
}
