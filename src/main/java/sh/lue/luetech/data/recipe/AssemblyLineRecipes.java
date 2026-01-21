package sh.lue.luetech.data.recipe;

import com.gregtechceu.gtceu.data.tag.CustomTags;
import net.minecraft.data.recipes.RecipeOutput;
import sh.lue.luetech.LueTech;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.data.block.GTBlocks.*;
import static com.gregtechceu.gtceu.data.item.GTItems.*;
import static com.gregtechceu.gtceu.data.machine.GTMachines.*;
import static com.gregtechceu.gtceu.data.material.GTMaterials.*;
import static com.gregtechceu.gtceu.data.recipe.GTRecipeTypes.*;

public class AssemblyLineRecipes {
    public static void init(RecipeOutput consumer) {
        ASSEMBLY_LINE_RECIPES.recipeBuilder(LueTech.id("cleaning_maintenance_hatch"))
                .inputItems(AUTO_MAINTENANCE_HATCH.asStack())
                .inputItems(FILTER_CASING.asStack(), 16)
                .inputItems(CustomTags.LuV_CIRCUITS)
                .inputItems(ROBOT_ARM_IV, 4)
                .inputItems(wireFine, Electrum, 64)
                .inputFluids(SolderingAlloy.getFluid(L * 2))
                .inputFluids(Lubricant.getFluid(500))
                .outputItems(CLEANING_MAINTENANCE_HATCH.asStack())
                .scannerResearch(b -> b
                        .researchStack(AUTO_MAINTENANCE_HATCH.asStack())
                        .duration(2400)
                        .EUt(VA[IV]))
                .duration(600)
                .EUt(6000)
                .save(consumer);
    }
}
