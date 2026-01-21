package sh.lue.luetech.mixins.client.jdt;

import com.direwolf20.justdirethings.client.jei.GooSpreadRecipeTagCategory;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipeTag;
import com.llamalad7.mixinextras.sugar.Local;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.lue.luetech.data.LTBlocks;

import java.util.List;

@Mixin(value = GooSpreadRecipeTagCategory.class, remap = false)
public abstract class JEIGooSpreadRecipeTagCategoryMixin implements IRecipeCategory<GooSpreadRecipeTag> {
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/direwolf20/justdirethings/datagen/recipes/GooSpreadRecipeTag;Lmezz/jei/api/recipe/IFocusGroup;)V",
            at = @At(value = "INVOKE",
                    target = "Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;addSlot(Lmezz/jei/api/recipe/RecipeIngredientRole;II)Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;",
                    ordinal = 1))
    private void luetech$injectCustomCatalyst(IRecipeLayoutBuilder builder,
                                              GooSpreadRecipeTag recipe,
                                              IFocusGroup focuses,
                                              CallbackInfo ci,
                                              @Local(name = "catalystlist") List<ItemStack> catalystList) {
        if (recipe.getTierRequirement() <= 5)
            catalystList.add(LTBlocks.TIMEWIND_GOO.asStack());
    }
}
