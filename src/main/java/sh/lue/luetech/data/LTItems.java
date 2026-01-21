package sh.lue.luetech.data;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.common.item.behavior.TooltipBehavior;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.client.item.behavior.BoundSpiritTooltipBehavior;
import sh.lue.luetech.common.item.behavior.BatteryPackBehavior;

import java.util.Locale;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.client.util.TooltipHelper.rainbowColor;
import static com.klikli_dev.occultism.util.TextUtil.SPIRIT_NAME_NOT_YET_KNOWN;
import static com.klikli_dev.occultism.registry.OccultismDataComponents.SPIRIT_NAME;
import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;

public class LTItems {
    static {
        REGISTRATE.creativeModeTab(LTCreativeModeTabs.ITEM);
    }

    public static final ItemEntry<Item> CHARGING_LENS = REGISTRATE
            .item("charging_lens", Item::new)
            .model(NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<ComponentItem> BATTERY_PACK = REGISTRATE
            .item("battery_pack", ComponentItem::new)
            .properties(p -> p.stacksTo(1)
                    .component(LTDataComponents.BATTERY_PACK_ACTIVE, false)
                    .component(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.EMPTY))
            .model((ctx, prov) ->
                    prov.generated(ctx, LueTech.id("item/curios/battery_pack")))
            .tag(LTTags.BATTERY_PACK_CURIO)
            .onRegister(c -> c.attachComponents(
                    new BatteryPackBehavior(BatteryPackBehavior.Variant.BATTERY_PACK)))
            .register();

    public static final ItemEntry<ComponentItem> WIRELESS_CHARGER_PACK = REGISTRATE
            .item("wireless_charger_pack", ComponentItem::new)
            .properties(p -> p.stacksTo(1)
                    .component(LTDataComponents.BATTERY_PACK_ACTIVE, false)
                    .component(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.EMPTY))
            .model((ctx, prov) ->
                    prov.generated(ctx, LueTech.id("item/curios/wireless_charger_pack")))
            .tag(LTTags.BATTERY_PACK_CURIO)
            .onRegister(c -> c.attachComponents(
                    new BatteryPackBehavior(BatteryPackBehavior.Variant.WIRELESS_CHARGER)))
            .register();

    static {
        REGISTRATE.creativeModeTab(LTCreativeModeTabs.CIRCUIT);
    }

    /* T8: Spectral */
    public static final ItemEntry<ComponentItem> SPECTRAL_PROCESSOR_ZPM = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("spectral_processor", ComponentItem::new)
                    .lang("Spectral Processor")
                    .properties(p -> p
                            .component(SPIRIT_NAME, SPIRIT_NAME_NOT_YET_KNOWN))
                    .model(NonNullBiConsumer.noop())
                    .onRegister(item -> item.attachComponents(new BoundSpiritTooltipBehavior("circuit",
                            "item.luetech.spectral_processor.tooltip_special")))
                    .register() : null;
    public static final ItemEntry<ComponentItem> SPECTRAL_PROCESSOR_ASSEMBLY_UV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("spectral_processor_assembly", ComponentItem::new)
                    .lang("Spectral Processor Assembly")
                    .properties(p -> p
                            .component(SPIRIT_NAME, SPIRIT_NAME_NOT_YET_KNOWN))
                    .model(NonNullBiConsumer.noop())
                    .onRegister(item -> item.attachComponents(new BoundSpiritTooltipBehavior("circuit",
                            "item.luetech.spectral_processor_assembly.tooltip_special")))
                    .register() : null;
    public static final ItemEntry<ComponentItem> SPECTRAL_PROCESSOR_SUPERCOMPUTER_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("spectral_processor_computer", ComponentItem::new)
                    .lang("Spectral Processor Supercomputer")
                    .properties(p -> p
                            .component(SPIRIT_NAME, SPIRIT_NAME_NOT_YET_KNOWN))
                    .model(NonNullBiConsumer.noop())
                    .onRegister(item -> item.attachComponents(new BoundSpiritTooltipBehavior("circuit",
                            "item.luetech.spectral_processor_computer.tooltip_special")))
                    .register() : null;
    public static final ItemEntry<ComponentItem> SPECTRAL_PROCESSOR_MAINFRAME_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("spectral_processor_mainframe", ComponentItem::new)
                    .lang("Spectral Processor Mainframe")
                    .properties(p -> p
                            .component(SPIRIT_NAME, SPIRIT_NAME_NOT_YET_KNOWN))
                    .model(NonNullBiConsumer.noop())
                    .onRegister(item -> item.attachComponents(new BoundSpiritTooltipBehavior("circuit",
                            "item.luetech.spectral_processor_mainframe.tooltip_special")))
                    .register() : null;

    /* T9: Elemental */
    public static final ItemEntry<Item> ELEMENTAL_PROCESSOR_UV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("elemental_processor", Item::new).lang("Elemental Processor")
                    .model(NonNullBiConsumer.noop())
                    .register() : null;
    public static final ItemEntry<Item> ELEMENTAL_PROCESSOR_ASSEMBLY_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("elemental_processor_assembly", Item::new).lang("Elemental Processor Assembly")
                    .model(NonNullBiConsumer.noop())
                    .register() : null;
    public static final ItemEntry<Item> ELEMENTAL_PROCESSOR_SUPERCOMPUTER_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("elemental_processor_computer", Item::new).lang("Elemental Processor Supercomputer")
                    .model(NonNullBiConsumer.noop())
                    .register() : null;
    public static final ItemEntry<Item> ELEMENTAL_PROCESSOR_MAINFRAME_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("elemental_processor_mainframe", Item::new).lang("Elemental Processor Mainframe")
                    .model(NonNullBiConsumer.noop())
                    .register() : null;

    /* T10: Temporal */
    public static final ItemEntry<Item> TEMPORAL_PROCESSOR_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("temporal_processor", Item::new).lang("Temporal Processor")
                    .model(NonNullBiConsumer.noop())
                    .register() : null;
    public static final ItemEntry<Item> TEMPORAL_PROCESSOR_ASSEMBLY_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("temporal_processor_assembly", Item::new).lang("Temporal Processor Assembly")
                    .model(NonNullBiConsumer.noop())
                    .register() : null;
    public static final ItemEntry<Item> TEMPORAL_PROCESSOR_SUPERCOMPUTER_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("temporal_processor_computer", Item::new).lang("Temporal Processor Supercomputer")
                    .model(NonNullBiConsumer.noop())
                    .register() : null;
    public static final ItemEntry<Item> TEMPORAL_PROCESSOR_MAINFRAME_UXV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("temporal_processor_mainframe", Item::new).lang("Temporal Processor Mainframe")
                    .model(NonNullBiConsumer.noop())
                    .register() : null;

    /* T11: Planetary */
    public static final ItemEntry<ComponentItem> PLANETARY_PROCESSOR_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("planetary_processor", ComponentItem::new)
                    .lang("Planetary Processor")
                    .model(NonNullBiConsumer.noop())
                    .onRegister(item -> item.attachComponents(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.luetech.planetary_processor.tooltip_special.0"));
                        lines.add(Component.translatable("item.luetech.planetary_processor.tooltip_special.1")
                                .withStyle(style -> style.withColor(rainbowColor(5.0f))));
                    })))
                    .register() : null;
    public static final ItemEntry<ComponentItem> PLANETARY_PROCESSOR_ASSEMBLY_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("planetary_processor_assembly", ComponentItem::new)
                    .lang("Planetary Processor Assembly")
                    .model(NonNullBiConsumer.noop())
                    .onRegister(item -> item.attachComponents(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.luetech.planetary_processor_assembly.tooltip_special.0"));
                        lines.add(Component.translatable("item.luetech.planetary_processor_assembly.tooltip_special.1")
                                .withStyle(style -> style.withColor(rainbowColor(5.0f))));
                    })))
                    .register() : null;
    public static final ItemEntry<ComponentItem> PLANETARY_PROCESSOR_SUPERCOMPUTER_UXV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("planetary_processor_computer", ComponentItem::new)
                    .lang("Planetary Processor Supercomputer")
                    .model(NonNullBiConsumer.noop())
                    .onRegister(item -> item.attachComponents(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.luetech.planetary_processor_computer.tooltip_special.0"));
                        lines.add(Component.translatable("item.luetech.planetary_processor_computer.tooltip_special.1")
                                .withStyle(style -> style.withColor(rainbowColor(5.0f))));
                    })))
                    .register() : null;
    public static final ItemEntry<ComponentItem> PLANETARY_PROCESSOR_MAINFRAME_OpV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("planetary_processor_mainframe", ComponentItem::new)
                    .lang("Planetary Processor Mainframe")
                    .model(NonNullBiConsumer.noop())
                    .onRegister(item -> item.attachComponents(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.luetech.planetary_processor_mainframe.tooltip_special.0"));
                        lines.add(Component.translatable("item.luetech.planetary_processor_mainframe.tooltip_special.1")
                                .withStyle(style -> style.withColor(rainbowColor(5.0f))));
                    })))
                    .register() : null;
    public static final ItemEntry<ComponentItem> PLANETARY_PROCESSOR_SINGULARITY_MAX = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("planetary_processor_singularity", ComponentItem::new)
                    .lang("Planetary Processor Singularity")
                    .model(NonNullBiConsumer.noop())
                    .onRegister(item -> item.attachComponents(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.luetech.planetary_processor_singularity.tooltip_special.0"));
                        lines.add(Component.translatable("item.luetech.planetary_processor_singularity.tooltip_special.1")
                                .withStyle(style -> style.withColor(rainbowColor(5.0f))));
                    })))
                    .register() : null;

    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_ULV = registerUniversalCircuit(ULV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_LV = registerUniversalCircuit(LV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_MV = registerUniversalCircuit(MV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_HV = registerUniversalCircuit(HV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_EV = registerUniversalCircuit(EV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_IV = registerUniversalCircuit(IV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_LuV = registerUniversalCircuit(LuV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_ZPM = registerUniversalCircuit(ZPM);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_UV = registerUniversalCircuit(UV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_UHV = registerUniversalCircuit(UHV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_UEV = registerUniversalCircuit(UEV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_UIV = registerUniversalCircuit(UIV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_UXV = registerUniversalCircuit(UXV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_OpV = registerUniversalCircuit(OpV);
    public static final ItemEntry<Item> UNIVERSAL_CIRCUIT_MAX = registerUniversalCircuit(MAX);

    @SuppressWarnings("unchecked")
    public static final ItemEntry<Item>[] UNIVERSAL_CIRCUITS = new ItemEntry[] {
            UNIVERSAL_CIRCUIT_ULV,
            UNIVERSAL_CIRCUIT_LV,
            UNIVERSAL_CIRCUIT_MV,
            UNIVERSAL_CIRCUIT_HV,
            UNIVERSAL_CIRCUIT_EV,
            UNIVERSAL_CIRCUIT_IV,
            UNIVERSAL_CIRCUIT_LuV,
            UNIVERSAL_CIRCUIT_ZPM,
            UNIVERSAL_CIRCUIT_UV,
            UNIVERSAL_CIRCUIT_UHV,
            UNIVERSAL_CIRCUIT_UEV,
            UNIVERSAL_CIRCUIT_UIV,
            UNIVERSAL_CIRCUIT_UXV,
            UNIVERSAL_CIRCUIT_OpV,
            UNIVERSAL_CIRCUIT_MAX,
    };

    @Nullable
    private static ItemEntry<Item> registerUniversalCircuit(int tier) {
        if (tier <= UHV || GTCEuAPI.isHighTier()) {
            return REGISTRATE.item("universal_" + VN[tier].toLowerCase(Locale.ROOT) + "_circuit", Item::new)
                    .lang("Universal " + VN[tier] + " Circuit")
                    .model((ctx, prov) ->
                            prov.generated(ctx, LueTech.id("item/circuits/universal/" + ctx.getName())))
                    .register();
        }
        return null;
    }

    public static void init() {}
}
