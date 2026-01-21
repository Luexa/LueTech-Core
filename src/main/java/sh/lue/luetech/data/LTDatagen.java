package sh.lue.luetech.data;

import com.tterrag.registrate.providers.ProviderType;
import sh.lue.luetech.data.lang.LangHandler;
import sh.lue.luetech.data.loot.LootHandler;
import sh.lue.luetech.data.recipe.RecipeHandler;
import sh.lue.luetech.data.tags.TagsHandler;

import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;

public class LTDatagen {
    static {
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagsHandler::initItem);
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagsHandler::initBlock);
        REGISTRATE.addDataGenerator(ProviderType.LOOT, LootHandler::init);
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeHandler::init);
        REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::init);
    }

    public static void init() {}
}
