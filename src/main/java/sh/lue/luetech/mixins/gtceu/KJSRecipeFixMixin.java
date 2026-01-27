package sh.lue.luetech.mixins.gtceu;

import com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema;
import com.gregtechceu.gtceu.integration.kjs.recipe.components.CapabilityMap;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static sh.lue.luetech.mixins.gtceu.KJSRecipeAccessor.*;

@Mixin(value = GTRecipeSchema.GTKubeRecipe.class, remap = false)
public abstract class KJSRecipeFixMixin extends KubeRecipe {
//    @ModifyVariable(
//            method = "output(Lcom/gregtechceu/gtceu/api/capability/recipe/RecipeCapability;[Ljava/lang/Object;)Lcom/gregtechceu/gtceu/integration/kjs/recipe/GTRecipeSchema$GTKubeRecipe;",
//            at = @At("STORE"),
//            ordinal = 0
//    )
//    private RecipeKey<CapabilityMap> luetech$replaceKey(RecipeKey<CapabilityMap> key) {
//        if (key == luetech$getAllInputs()) return luetech$getAllOutputs();
//        return luetech$getAllTickOutputs();
//    }
}
