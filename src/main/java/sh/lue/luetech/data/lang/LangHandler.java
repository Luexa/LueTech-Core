package sh.lue.luetech.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import sh.lue.luetech.mixins.gtceu.LangHandlerInvoker;

public class LangHandler {
    public static void init(RegistrateLangProvider provider) {
        ItemLang.init(provider);
        MiscLang.init(provider);
    }

    static void multilineLang(RegistrateLangProvider provider, String key, String multiline) {
        LangHandlerInvoker.luetech$multilineLang(provider, key, multiline);
    }

    static void multiLang(RegistrateLangProvider provider, String key, String... values) {
        LangHandlerInvoker.luetech$multiLang(provider, key, values);
    }

    static void replace(RegistrateLangProvider provider, String key, String value) {
        com.gregtechceu.gtceu.data.datagen.lang.LangHandler.replace(provider, key, value);
    }
}
