package sh.lue.luetech.data;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.material.ChemicalHelper;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import com.gregtechceu.gtceu.data.misc.GTCreativeModeTabs.RegistrateDisplayItemsGenerator;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import sh.lue.luetech.LueTech;

import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;

@SuppressWarnings("Convert2MethodRef")
public class LTCreativeModeTabs {
    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_FLUID = REGISTRATE.defaultCreativeTab("material_fluid",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_fluid", REGISTRATE))
                            .icon(() -> LTMaterials.LutetiumElementiumQuantonide.getBucket().getDefaultInstance())
                            .title(REGISTRATE.addLang("itemGroup",
                                    LueTech.id("material_fluid"),
                                    LueTech.NAME + " Material Fluid Containers"))
                            .build())
            .register();

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEM = REGISTRATE.defaultCreativeTab("material_item",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_item", REGISTRATE))
                            .icon(() -> ChemicalHelper.get(TagPrefix.ingot, LTMaterials.LutetiumElementiumQuantonide))
                            .title(REGISTRATE.addLang("itemGroup",
                                    LueTech.id("material_item"),
                                    LueTech.NAME + " Material Items"))
                            .build())
            .register();

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_BLOCK = REGISTRATE.defaultCreativeTab("material_block",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_block", REGISTRATE))
                            .icon(() -> ChemicalHelper.get(TagPrefix.block, LTMaterials.LutetiumElementiumQuantonide))
                            .title(REGISTRATE.addLang("itemGroup",
                                    LueTech.id("material_block"),
                                    LueTech.NAME + " Material Blocks"))
                            .build())
            .register();

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_PIPE = REGISTRATE.defaultCreativeTab("material_pipe",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_pipe", REGISTRATE))
                            .icon(() -> ChemicalHelper.get(TagPrefix.wireGtDouble, LTMaterials.LutetiumElementiumQuantonide))
                            .title(REGISTRATE.addLang("itemGroup",
                                    LueTech.id("material_pipe"),
                                    LueTech.NAME + " Material Pipes"))
                            .build())
            .register();

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> DECORATION = REGISTRATE.defaultCreativeTab("decoration",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("decoration", REGISTRATE))
                            .icon(() -> LTBlocks.TIMEWIND_GOO.asStack())
                            .title(REGISTRATE.addLang("itemGroup",
                                    LueTech.id("decoration"),
                                    LueTech.NAME + " Decoration Blocks"))
                            .build())
            .register();

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MACHINE = REGISTRATE.defaultCreativeTab("machine",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("machine", REGISTRATE))
                            .icon(() -> LTMachines.CHARGE_TRANSMITTER[GTValues.IV].asStack())
                            .title(REGISTRATE.addLang("itemGroup",
                                    LueTech.id("machine"),
                                    LueTech.NAME + " Machines"))
                            .build())
            .register();

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> CIRCUIT = REGISTRATE.defaultCreativeTab("circuit",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("circuit", REGISTRATE))
                            .icon(() -> {
                                if (LTItems.UNIVERSAL_CIRCUIT_LV != null) {
                                    return LTItems.UNIVERSAL_CIRCUIT_LV.asStack();
                                } else if (LTItems.ELEMENTAL_PROCESSOR_MAINFRAME_UIV != null) {
                                    return LTItems.ELEMENTAL_PROCESSOR_MAINFRAME_UIV.asStack();
                                } else {
                                    return Items.BARRIER.getDefaultInstance();
                                }
                            })
                            .title(REGISTRATE.addLang("itemGroup",
                                    LueTech.id("circuit"),
                                    LueTech.NAME + " Circuits"))
                            .build())
            .register();

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> ITEM = REGISTRATE.defaultCreativeTab("item",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("item", REGISTRATE))
                            .icon(() -> LTItems.BATTERY_PACK.asStack())
                            .title(REGISTRATE.addLang("itemGroup",
                                    LueTech.id("item"),
                                    LueTech.NAME + " Items"))
                            .build())
            .register();

    public static void init() {}
}
