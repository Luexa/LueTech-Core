package sh.lue.luetech.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import sh.lue.luetech.LTCompat;

public class RecipeHandler {
    public static void init(RegistrateRecipeProvider provider) {
        ShapedRecipes.init(provider);
        GooSpreadRecipes.init(provider);
    }
}
