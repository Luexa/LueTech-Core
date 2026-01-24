package sh.lue.luetech.data.dynamic.recipe;

import appeng.core.definitions.AEItems;
import com.glodblock.github.extendedae.common.EAESingletons;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.pedroksl.advanced_ae.common.definitions.AAEItems;
import sh.lue.luetech.LueTech;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.data.material.GTMaterials.*;
import static sh.lue.luetech.data.LTItems.*;
import static sh.lue.luetech.data.LTRecipeTypes.*;
import static sh.lue.luetech.data.LTMaterials.*;

public class AtomicRewriterRecipes {
    public static void init(RecipeOutput consumer) {
        /* AE2 */
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("charge_certus_quartz"))
                .notConsumable(CHARGING_LENS)
                .inputItems(gem, CertusQuartz)
                .outputItems(gem, ChargedCertusQuartz)
                .EUt(VA[ULV])
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("charge_book"))
                .notConsumable(CHARGING_LENS)
                .inputItems(Items.BOOK)
                .outputItems(AEItems.TABLET)
                .EUt(VA[ULV])
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("charge_compass"))
                .notConsumable(CHARGING_LENS)
                .inputItems(Items.COMPASS)
                .outputItems(AEItems.METEORITE_COMPASS)
                .EUt(VA[ULV])
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("inscribe_certus_quartz"))
                .notConsumable(AEItems.CALCULATION_PROCESSOR_PRESS)
                .inputItems(AEItems.CERTUS_QUARTZ_CRYSTAL)
                .outputItems(AEItems.CALCULATION_PROCESSOR_PRINT)
                .EUt(20)
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("inscribe_gold"))
                .notConsumable(AEItems.LOGIC_PROCESSOR_PRESS)
                .inputItems(Tags.Items.INGOTS_GOLD)
                .outputItems(AEItems.LOGIC_PROCESSOR_PRINT)
                .EUt(20)
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("inscribe_diamond"))
                .notConsumable(AEItems.ENGINEERING_PROCESSOR_PRESS)
                .inputItems(Tags.Items.GEMS_DIAMOND)
                .outputItems(AEItems.ENGINEERING_PROCESSOR_PRINT)
                .EUt(20)
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("clone_calculation_press"))
                .notConsumable(AEItems.CALCULATION_PROCESSOR_PRESS)
                .inputItems(Tags.Items.STORAGE_BLOCKS_IRON)
                .outputItems(AEItems.CALCULATION_PROCESSOR_PRESS)
                .EUt(20)
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("clone_logic_press"))
                .notConsumable(AEItems.LOGIC_PROCESSOR_PRESS)
                .inputItems(Tags.Items.STORAGE_BLOCKS_IRON)
                .outputItems(AEItems.LOGIC_PROCESSOR_PRESS)
                .EUt(20)
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("clone_engineering_press"))
                .notConsumable(AEItems.ENGINEERING_PROCESSOR_PRESS)
                .inputItems(Tags.Items.STORAGE_BLOCKS_IRON)
                .outputItems(AEItems.ENGINEERING_PROCESSOR_PRESS)
                .EUt(20)
                .duration(100)
                .save(consumer);

        /* ExtendedAE */
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("inscribe_entro"))
                .notConsumable(EAESingletons.CONCURRENT_PROCESSOR_PRESS)
                .inputItems(gem, Entro)
                .outputItems(EAESingletons.CONCURRENT_PROCESSOR_PRINT)
                .EUt(20)
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("clone_concurrent_press"))
                .notConsumable(EAESingletons.CONCURRENT_PROCESSOR_PRESS)
                .inputItems(Tags.Items.STORAGE_BLOCKS_IRON)
                .outputItems(EAESingletons.CONCURRENT_PROCESSOR_PRESS)
                .EUt(20)
                .duration(100)
                .save(consumer);

        /* AdvancedAE */
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("inscribe_quantum_alloy"))
                .notConsumable(AAEItems.QUANTUM_PROCESSOR_PRESS)
                .inputItems(AAEItems.QUANTUM_ALLOY)
                .outputItems(AAEItems.QUANTUM_PROCESSOR_PRINT)
                .EUt(20)
                .duration(100)
                .save(consumer);
        ATOMIC_REWRITER_RECIPES.recipeBuilder(LueTech.id("clone_quantum_press"))
                .notConsumable(AAEItems.QUANTUM_PROCESSOR_PRESS)
                .inputItems(Tags.Items.STORAGE_BLOCKS_IRON)
                .outputItems(AAEItems.QUANTUM_PROCESSOR_PRESS)
                .EUt(20)
                .duration(100)
                .save(consumer);
    }
}
