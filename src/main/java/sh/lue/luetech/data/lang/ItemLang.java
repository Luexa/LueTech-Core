package sh.lue.luetech.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import static sh.lue.luetech.data.lang.LangHandler.multilineLang;
import static sh.lue.luetech.data.lang.LangHandler.multiLang;
import static net.minecraft.ChatFormatting.*;

public class ItemLang {
    public static void init(RegistrateLangProvider provider) {
        /* T8: Spectral */
        provider.add("item.luetech.spectral_processor.tooltip_special",
                DARK_AQUA + "ZPM-Tier Circuit");
        provider.add("item.luetech.spectral_processor_assembly.tooltip_special",
                DARK_AQUA + "UV-Tier Circuit");
        provider.add("item.luetech.spectral_processor_computer.tooltip_special",
                DARK_AQUA + "UHV-Tier Circuit");
        provider.add("item.luetech.spectral_processor_mainframe.tooltip_special",
                DARK_AQUA + "UEV-Tier Circuit");
//        multiLang(provider, "item.luetech.spectral_processor.tooltip_special",
//                "§7Computing from the Other Side", DARK_AQUA + "ZPM-Tier Circuit");
//        multiLang(provider, "item.luetech.spectral_processor_assembly.tooltip_special",
//                "§7Computing from the Other Side", DARK_AQUA + "UV-Tier Circuit");
//        multiLang(provider, "item.luetech.spectral_processor_computer.tooltip_special",
//                "§7Computing from the Other Side", DARK_AQUA + "UHV-Tier Circuit");
//        multiLang(provider, "item.luetech.spectral_processor_mainframe.tooltip_special",
//                "§7Computing from the Other Side", DARK_AQUA + "UEV-Tier Circuit");

        /* T9: Elemental */
        multilineLang(provider, "item.luetech.elemental_processor.tooltip",
                "§7Indistinguishable from magic\n" + LIGHT_PURPLE + "UV-Tier Circuit");
        multilineLang(provider, "item.luetech.elemental_processor_assembly.tooltip",
                "§7Indistinguishable from magic\n" + LIGHT_PURPLE + "UHV-Tier Circuit");
        multilineLang(provider, "item.luetech.elemental_processor_computer.tooltip",
                "§7Indistinguishable from magic\n" + LIGHT_PURPLE + "UEV-Tier Circuit");
        multilineLang(provider, "item.luetech.elemental_processor_mainframe.tooltip",
                "§7Indistinguishable from magic\n" + LIGHT_PURPLE + "UIV-Tier Circuit");

        /* T10: Temporal */
        multilineLang(provider, "item.luetech.temporal_processor.tooltip",
                "§7Name subject to change\n" + WHITE + "UHV-Tier Circuit");
        multilineLang(provider, "item.luetech.temporal_processor_assembly.tooltip",
                "§7Causality processor\n" + WHITE + "UEV-Tier Circuit");
        multilineLang(provider, "item.luetech.temporal_processor_computer.tooltip",
                "§7Time leap machine\n" + WHITE + "UIV-Tier Circuit");
        multilineLang(provider, "item.luetech.temporal_processor_mainframe.tooltip",
                "§7Can solve the Y2K Problem\n" + WHITE + "UXV-Tier Circuit");

        /* T11: Planetary */
        multiLang(provider, "item.luetech.planetary_processor.tooltip_special",
                "§7Technically a dwarf planet", "UEV-Tier Circuit");
        multiLang(provider, "item.luetech.planetary_processor_assembly.tooltip_special",
                "§7Somehow fits in your inventory", "UIV-Tier Circuit");
        multiLang(provider, "item.luetech.planetary_processor_computer.tooltip_special",
                "§7Theoretically supports life", "UXV-Tier Circuit");
        multiLang(provider, "item.luetech.planetary_processor_mainframe.tooltip_special",
                "§7The Ultimate Question of Life, the Universe, and Everything", "OpV-Tier Circuit");
        multiLang(provider, "item.luetech.planetary_processor_singularity.tooltip_special",
                "§7The Ultimate Answer to Life, the Universe, and Everything", "MAX-Tier Circuit");

        /* Universal Circuits */
        multilineLang(provider, "item.luetech.universal_ulv_circuit.tooltip",
                "§7Generalized Circuit\n" + DARK_GRAY + "ULV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_lv_circuit.tooltip",
                "§7Generalized Circuit\n" + GRAY + "LV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_mv_circuit.tooltip",
                "§7Generalized Circuit\n" + AQUA + "MV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_hv_circuit.tooltip",
                "§7Generalized Circuit\n" + GOLD + "HV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_ev_circuit.tooltip",
                "§7Generalized Circuit\n" + DARK_PURPLE + "EV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_iv_circuit.tooltip",
                "§7Generalized Circuit\n" + BLUE + "IV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_luv_circuit.tooltip",
                "§7Generalized Circuit\n" + LIGHT_PURPLE + "LuV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_zpm_circuit.tooltip",
                "§7Generalized Circuit\n" + RED + "ZPM-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_uv_circuit.tooltip",
                "§7Generalized Circuit\n" + DARK_AQUA + "UV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_uhv_circuit.tooltip",
                "§7Generalized Circuit\n" + DARK_RED + "UHV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_uev_circuit.tooltip",
                "§7Generalized Circuit\n" + GREEN + "UEV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_uiv_circuit.tooltip",
                "§7Generalized Circuit\n" + DARK_GREEN + "UIV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_uxv_circuit.tooltip",
                "§7Generalized Circuit\n" + YELLOW + "UXV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_opv_circuit.tooltip",
                "§7Generalized Circuit\n" + BLUE + "OpV-Tier Circuit");
        multilineLang(provider, "item.luetech.universal_max_circuit.tooltip",
                "§7Generalized Circuit\n" + RED + "MAX-Tier Circuit");
    }
}
