package sh.lue.luetech.mixins.gtceu;

import com.gregtechceu.gtceu.data.datagen.lang.LangHandler;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = LangHandler.class, remap = false)
public interface LangHandlerInvoker {
    @Invoker("multilineLang")
    static void luetech$multilineLang(RegistrateLangProvider provider, String key, String multiline) {
        throw new AssertionError();
    }

    @Invoker("multiLang")
    static void luetech$multiLang(RegistrateLangProvider provider, String key, String... values) {
        throw new AssertionError();
    }
}
