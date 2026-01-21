package sh.lue.luetech.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import sh.lue.luetech.LueTech;

@EventBusSubscriber(modid = LueTech.MODID, value = Dist.CLIENT)
public class LTClientEvents {
    @SubscribeEvent()
    public static void onTooltipEvent(ItemTooltipEvent event) {
        LTFormatting.appendTooltips(event.getItemStack(), event.getFlags(), event.getToolTip());
    }
}
