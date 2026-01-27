package sh.lue.luetech.mixins.gtceu;

import com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema;
import com.gregtechceu.gtceu.integration.kjs.recipe.components.CapabilityMap;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GTRecipeSchema.class, remap = false)
public interface KJSRecipeAccessor {
//    @Accessor("ALL_INPUTS")
//    static RecipeKey<CapabilityMap> luetech$getAllInputs() {
//        throw new AssertionError();
//    }
//    @Accessor("ALL_OUTPUTS")
//    static RecipeKey<CapabilityMap> luetech$getAllOutputs() {
//        throw new AssertionError();
//    }
//    @Accessor("ALL_TICK_OUTPUTS")
//    static RecipeKey<CapabilityMap> luetech$getAllTickOutputs() {
//        throw new AssertionError();
//    }
}
