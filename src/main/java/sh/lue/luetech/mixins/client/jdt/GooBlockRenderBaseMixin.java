package sh.lue.luetech.mixins.client.jdt;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.GooBlockRender_Base;
import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sh.lue.luetech.data.LTTags;

@Mixin(value = GooBlockRender_Base.class, remap = false)
public abstract class GooBlockRenderBaseMixin<T extends GooBlockBE_Base> implements BlockEntityRenderer<T> {
    @ModifyVariable(method = "getNextItemFromTag", at = @At("STORE"))
    private TagKey<Item> luetech$injectCustomTierTag(TagKey<Item> original, @Local(argsOnly = true, ordinal = 0) int tier) {
        if (original != null) {
            return original;
        }
        if (tier == 5) {
            return LTTags.REVIVE_TIMEWIND_GOO;
        }
        return null;
    }
}
