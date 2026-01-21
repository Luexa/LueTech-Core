package sh.lue.luetech.data.recipe;

import com.gregtechceu.gtceu.api.material.ChemicalHelper;
import com.gregtechceu.gtceu.api.material.material.Material;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import com.gregtechceu.gtceu.data.material.GTMaterials;
import com.gregtechceu.gtceu.data.tag.CustomTags;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.data.LTItems;

public class ShapedRecipes {
    public static void init(RegistrateRecipeProvider provider) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LTItems.BATTERY_PACK)
                .unlockedBy("dummy", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .define('C', CustomTags.ULV_CIRCUITS)
                .define('W', nonNullMaterialTag(TagPrefix.wireFine, GTMaterials.Copper))
                .define('L', Tags.Items.LEATHERS)
                .define('R', nonNullMaterialTag(TagPrefix.rod, GTMaterials.IronMagnetic))
                .pattern("CWC")
                .pattern("LRL")
                .pattern("LLL")
                .save(provider, LueTech.id("battery_pack"));
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
