package sh.lue.luetech.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;

public class RecipeHandler {
    public static void init(RegistrateRecipeProvider provider) {
        GooSpreadRecipes.init(provider);
    }
}
