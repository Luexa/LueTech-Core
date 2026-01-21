package sh.lue.luetech.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import sh.lue.luetech.LueTech;

public class LTTags {
    public static final TagKey<Item> BATTERY_PACK_CURIO = ItemTags.create(ResourceLocation.parse("curios:battery_pack"));
    public static final TagKey<Item> UNIVERSAL_CIRCUITS = createModItemTag("universal_circuits");
    public static final TagKey<Item> REVIVE_TIMEWIND_GOO = createModItemTag("revive_timewind_goo");

    private static TagKey<Item> createModItemTag(String path) {
        return ItemTags.create(LueTech.id(path));
    }
}
