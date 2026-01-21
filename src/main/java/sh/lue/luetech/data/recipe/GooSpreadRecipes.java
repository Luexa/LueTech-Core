package sh.lue.luetech.data.recipe;

import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.data.LTBlocks;

public class GooSpreadRecipes {
    public static void init(RegistrateRecipeProvider provider) {
        gooRecipe(provider,
                "iesnium_to_elementium",
                OccultismBlocks.IESNIUM_BLOCK.get().defaultBlockState(),
                LTBlocks.ELEMENTIUM_ORE.getDefaultState(),
                5,
                4800);
    }

    private static void gooRecipe(RegistrateRecipeProvider provider,
                                  String name,
                                  BlockState input,
                                  BlockState output,
                                  int requiredTier,
                                  int craftingDuration
    ) {
        ResourceLocation id = LueTech.id("goospread/" + name);
        GooSpreadRecipe recipe = new GooSpreadRecipe(id, input, output, requiredTier, craftingDuration);
        provider.accept(id, recipe, null);
    }
}
