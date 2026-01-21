package sh.lue.luetech.data;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeSerializer;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.data.sound.GTSoundEntries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sh.lue.luetech.LueTech;

import static com.gregtechceu.gtceu.data.recipe.GTRecipeTypes.ELECTRIC;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

public class LTRecipeTypes {
    public static final GTRecipeType ATOMIC_REWRITER_RECIPES = register("atomic_rewriter", ELECTRIC)
            .setMaxIOSize(6, 1, 2, 1).setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ELECTROLYZER);

    @NotNull
    public static GTRecipeType register(@NotNull String name, @NotNull String group,
                                        RecipeType<?> @NotNull ... proxyRecipes) {
        var recipeType = new GTRecipeType(LueTech.id(name), group, proxyRecipes);
        GTRegistries.register(BuiltInRegistries.RECIPE_TYPE, recipeType.registryName, recipeType);
        recipeType.serializer = GTRegistries.register(BuiltInRegistries.RECIPE_SERIALIZER, recipeType.registryName,
                new GTRecipeSerializer());
        return recipeType;
    }

    public static void init() {}
}
