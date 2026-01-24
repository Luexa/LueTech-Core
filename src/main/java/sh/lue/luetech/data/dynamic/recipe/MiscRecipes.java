package sh.lue.luetech.data.dynamic.recipe;

import com.gregtechceu.gtceu.api.material.ChemicalHelper;
import com.gregtechceu.gtceu.api.material.material.Material;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import com.gregtechceu.gtceu.common.recipe.builder.ShapedRecipeBuilder;
import com.gregtechceu.gtceu.data.material.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.GTRecipeTypes;
import com.gregtechceu.gtceu.data.tag.CustomTags;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.data.LTItems;
import sh.lue.luetech.data.LTRecipeTypes;

import java.util.Locale;

import static com.gregtechceu.gtceu.api.GTValues.*;

public class MiscRecipes {
    public static void init(RecipeOutput consumer) {
        // Battery Pack
        new ShapedRecipeBuilder(LueTech.id("battery_pack"))
                .define('C', CustomTags.ULV_CIRCUITS)
                .define('W', nonNullMaterialTag(TagPrefix.wireFine, GTMaterials.Copper))
                .define('L', Tags.Items.LEATHERS)
                .define('R', nonNullMaterialTag(TagPrefix.rod, GTMaterials.IronMagnetic))
                .pattern("CWC")
                .pattern("LRL")
                .pattern("LLL")
                .output(LTItems.BATTERY_PACK.asStack())
                .save(consumer);

        // Universal Circuits (Stonecutter & Atomic Rewriter)
        for (int tier : ALL_TIERS) {
            String vn = VN[tier].toLowerCase(Locale.ROOT);
            ResourceLocation stonecutterId = LueTech.id("stonecutter/universal_" + vn + "_circuit");
            consumer.accept(stonecutterId, new StonecutterRecipe(
                    "",
                    Ingredient.of(CustomTags.CIRCUITS_ARRAY[tier]),
                    LTItems.UNIVERSAL_CIRCUITS[tier].asStack()
            ), null);
            ResourceLocation rewriterId = LueTech.id("universal_" + vn + "_circuit");
            LTRecipeTypes.ATOMIC_REWRITER_RECIPES.recipeBuilder(rewriterId).duration(1).EUt(2)
                    .notConsumable(LTItems.HOMOGENIZING_LENS)
                    .inputItems(CustomTags.CIRCUITS_ARRAY[tier])
                    .outputItems(LTItems.UNIVERSAL_CIRCUITS[tier])
                    .save(consumer);
        }
    }

    @NotNull
    private static TagKey<Item> nonNullMaterialTag(@NotNull TagPrefix tagPrefix, @NotNull Material material) {
        var tag = ChemicalHelper.getTag(tagPrefix, material);
        if (tag == null) {
            throw new RuntimeException("No tag found for " + tagPrefix + ": " + material.getName());
        }
        return tag;
    }
}
