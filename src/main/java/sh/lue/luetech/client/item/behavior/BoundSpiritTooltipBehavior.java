package sh.lue.luetech.client.item.behavior;

import com.gregtechceu.gtceu.api.item.component.IAddInformation;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.klikli_dev.occultism.registry.OccultismDataComponents.SPIRIT_NAME;
import static com.klikli_dev.occultism.util.ItemNBTUtil.getBoundSpiritName;
import static com.klikli_dev.occultism.util.TextUtil.formatDemonName;

public class BoundSpiritTooltipBehavior implements IAddInformation {
    private final NonNullList<Component> staticTooltips;
    private final String boundSpiritTranslationKey;

    public BoundSpiritTooltipBehavior(@NotNull String subkey) {
        this.staticTooltips = NonNullList.createWithCapacity(0);
        this.boundSpiritTranslationKey = "luetech.ui.bound_spirit." + subkey;
    }

    public BoundSpiritTooltipBehavior(@NotNull String subkey, String @NotNull ... staticTooltips) {
        this.staticTooltips = NonNullList.createWithCapacity(staticTooltips.length);
        for (var descriptionId : staticTooltips) {
            var staticTooltip = Component.translatable(descriptionId);
            this.staticTooltips.add(staticTooltip);
        }
        this.boundSpiritTranslationKey = "luetech.ui.bound_spirit." + subkey;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltips, TooltipFlag flag) {
        if (stack.has(SPIRIT_NAME)) {
                tooltips.add(Component.translatable(boundSpiritTranslationKey,
                        formatDemonName(getBoundSpiritName(stack))));
        }
        tooltips.addAll(staticTooltips);
    }
}
