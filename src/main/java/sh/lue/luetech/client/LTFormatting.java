package sh.lue.luetech.client;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import sh.lue.luetech.LueTech;

import java.util.List;

import static com.gregtechceu.gtceu.data.datagen.lang.LangHandler.getMultiLang;

public class LTFormatting {
    private static final String ITEM_PREFIX = "item." + LueTech.MODID + ".";
    private static final String BLOCK_PREFIX = "block." + LueTech.MODID + ".";

    public static void appendTooltips(ItemStack stack, TooltipFlag flag, List<Component> tooltips) {
        String translationKey = stack.getDescriptionId();
        if (translationKey.startsWith(ITEM_PREFIX) || translationKey.startsWith(BLOCK_PREFIX)) {
            String tooltipKey = translationKey + ".tooltip";
            if (I18n.exists(tooltipKey)) {
                tooltips.add(tooltips.isEmpty() ? 0 : 1, Component.translatable(tooltipKey));
            } else {
                List<MutableComponent> multiLang = getMultiLang(tooltipKey);
                if (multiLang != null && !multiLang.isEmpty()) {
                    tooltips.addAll(tooltips.isEmpty() ? 0 : 1, multiLang);
                }
            }
        }
    }
}
