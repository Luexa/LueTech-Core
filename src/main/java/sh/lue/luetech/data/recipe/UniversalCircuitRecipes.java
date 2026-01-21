package sh.lue.luetech.data.recipe;

import com.gregtechceu.gtceu.data.recipe.GTRecipeTypes;
import com.gregtechceu.gtceu.data.tag.CustomTags;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.data.LTItems;

import java.util.Locale;

import static com.gregtechceu.gtceu.api.GTValues.*;

public class UniversalCircuitRecipes {
    public static void init(RecipeOutput consumer) {
        for (int tier : ALL_TIERS) {
            String vn = VN[tier].toLowerCase(Locale.ROOT);
            ResourceLocation stonecutterId = LueTech.id("stonecutter/universal_" + vn + "_circuit");
            consumer.accept(stonecutterId, new StonecutterRecipe(
                    "",
                    Ingredient.of(CustomTags.CIRCUITS_ARRAY[tier]),
                    LTItems.UNIVERSAL_CIRCUITS[tier].asStack()
            ), null);
            ResourceLocation assemblerId = LueTech.id("universal_" + vn + "_circuit");
            GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder(assemblerId).duration(1).EUt(2)
                    .inputItems(CustomTags.CIRCUITS_ARRAY[tier])
                    .circuitMeta(32)
                    .outputItems(LTItems.UNIVERSAL_CIRCUITS[tier])
                    .save(consumer);
        }
    }
}
