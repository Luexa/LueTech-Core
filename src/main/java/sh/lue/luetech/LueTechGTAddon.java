package sh.lue.luetech;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import sh.lue.luetech.common.registry.LTRegistration;
import sh.lue.luetech.data.recipe.AssemblyLineRecipes;
import sh.lue.luetech.data.recipe.AtomicRewriterRecipes;
import sh.lue.luetech.data.recipe.UniversalCircuitRecipes;

import java.util.function.Consumer;

@GTAddon(LueTech.MODID)
public class LueTechGTAddon implements IGTAddon {
    @Override
    public GTRegistrate getRegistrate() {
        return LTRegistration.REGISTRATE;
    }

    @Override
    public void gtInitComplete() {}

    @Override
    public void addRecipes(RecipeOutput consumer) {
        AssemblyLineRecipes.init(consumer);
        UniversalCircuitRecipes.init(consumer);
        AtomicRewriterRecipes.init(consumer);
    }

    @Override
    public void removeRecipes(Consumer<ResourceLocation> consumer) {
        for (var recipeId : new String[] {
                "gtceu:alloy_smelter/alloy_smelt_infused_entro_to_nugget",
                "gtceu:shaped/maintenance_hatch_cleaning",
        }) {
            consumer.accept(ResourceLocation.parse(recipeId));
        }
    }

    @Override
    public boolean requiresHighTier() {
        return true;
    }
}
