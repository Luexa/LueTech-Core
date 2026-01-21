package sh.lue.luetech.data;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.direwolf20.justdirethings.setup.Registration;
import com.glodblock.github.extendedae.common.EAESingletons;
import com.gregtechceu.gtceu.api.fluid.FluidBuilder;
import com.gregtechceu.gtceu.api.material.material.Material;
import com.gregtechceu.gtceu.api.material.material.properties.BlastProperty.GasTier;
import com.gregtechceu.gtceu.api.material.material.properties.IngotProperty;
import com.gregtechceu.gtceu.api.material.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.klikli_dev.occultism.registry.OccultismItems;
import sh.lue.luetech.LTCompat;
import sh.lue.luetech.LueTech;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.data.material.GTMaterials.*;

public class LTMaterials {
    private static final TagPrefix[] ALL_ORE_PREFIXES = new TagPrefix[] {
            TagPrefix.ore,
            TagPrefix.oreAndesite,
            TagPrefix.oreBasalt,
            TagPrefix.oreDiorite,
            TagPrefix.oreBlackstone,
            TagPrefix.oreDeepslate,
            TagPrefix.oreEndstone,
            TagPrefix.oreGranite,
            TagPrefix.oreGravel,
            TagPrefix.oreMarble,
            TagPrefix.oreRedGranite,
            TagPrefix.oreRedSand,
            TagPrefix.oreSand,
            TagPrefix.oreTuff,
            TagPrefix.oreNetherrack,
    };

    /* Element Materials */
    public static final Material Elementium = new Material.Builder(LueTech.id("elementium"))
            .ingot().liquid(new FluidBuilder().temperature(1811)).ore()
            .element(LTElements.E)
            .color(0xff82d1).iconSet(BRIGHT)
            .appendFlags(STD_METAL, GENERATE_LONG_ROD, GENERATE_FINE_WIRE, GENERATE_SPRING, GENERATE_FOIL,
                    GENERATE_FRAME)
            .cableProperties(V[UEV], 2, 2)
            .buildAndRegister();

    public static final Material Quantonium = new Material.Builder(LueTech.id("quantonium"))
            .ingot().liquid(new FluidBuilder().temperature(100000))
            .element(LTElements.Qt)
            .color(0x9273ff).iconSet(RADIOACTIVE)
            .radioactiveHazard(10F)
            .flags(GENERATE_LONG_ROD)
            .buildAndRegister();

    /* First-degree Materials */
    public static final Material QuantoniumMagnetic = new Material.Builder(LueTech.id("magnetic_quantonium"))
            .ingot()
            .components(Quantonium, 1)
            .color(0x7856f5).iconSet(MAGNETIC)
            .flags(IS_MAGNETIC, GENERATE_LONG_ROD)
            .ingotSmeltInto(Quantonium)
            .arcSmeltInto(Quantonium)
            .macerateInto(Quantonium)
            .buildAndRegister();
    static {
        Quantonium.getProperty(PropertyKey.INGOT).setMagneticMaterial(QuantoniumMagnetic);
    }

    public static final Material Syphite = new Material.Builder(LueTech.id("syphite"))
            .dust()
            .components(Sulfur, 1, Yttrium, 1, Phosphorus, 1, Hydrogen, 1)
            .color(0xffffff)
            .ore()
            .buildAndRegister();

    /* Second-degree Materials */
    public static final Material SyphiridiumSolution = new Material.Builder(LueTech.id("syphiridium_solution"))
            .liquid()
            .components(Syphite, 1, Iridium, 1)
            .color(0x96ffe7)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static final Material LutetiumElementiumQuantonide = new Material.Builder(LueTech.id("lutetium_elementium_quantonide"))
            .ingot().liquid(new FluidBuilder().temperature(69420))
            .components(Lutetium, 1, Elementium, 1, Quantonium, 1)
            .color(0xff0374).iconSet(METALLIC)
            .appendFlags(STD_METAL, DECOMPOSITION_BY_CENTRIFUGING, GENERATE_FINE_WIRE)
            .cableProperties(V[MAX], 134217727, 0, true, 3)
            .blast(b -> b.temp(10800, GasTier.HIGHEST)
                    .blastStats(VA[UXV], 1000)
                    .vacuumStats(VA[UXV], 200))
            .removeHazard()
            .buildAndRegister();

    /* Mod Integration Materials */
    public static final Material ChargedCertusQuartz = new Material.Builder(LueTech.id("charged_certus_quartz"))
            .gem().dust()
            .components(CertusQuartz, 1)
            .color(0x9fd5e8).iconSet(CERTUS)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister()
            .setFormula("SiO2");

    public static final Material Fluix = new Material.Builder(LueTech.id("fluix"))
            .gem().dust()
            .components(CertusQuartz, 1, Redstone, 1, NetherQuartz, 1)
            .color(0x7f5bb3).iconSet(CERTUS)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister()
            .setFormula("(SiO2)2(Si(FeS2)5(CrAl2O3)Hg3)");
    static {
        TagPrefix.block.modifyMaterialAmount(Fluix, 4);
    }

    public static final Material Entro = new Material.Builder(LueTech.id("entro"))
            .gem().dust()
            .components(Fluix, 1, EnderPearl, 3, Redstone, 3, Glowstone, 3)
            .color(0x3ed19b).iconSet(CERTUS)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister()
            .setFormula("(SiO2)3(BeK4N5)(Si(FeS2)5(CrAl2O3)Hg3)3Au?");
    static {
        TagPrefix.block.modifyMaterialAmount(Entro, 4);
    }

    public static final Material InfusedEntro = new Material.Builder(LueTech.id("infused_entro"))
            .ingot()
            .components(Entro, 1, Gold, 1, Lapis, 1)
            .color(0x3d19b).iconSet(BRIGHT)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static final Material Iesnium = new Material.Builder(LueTech.id("iesnium"))
            .ingot().ore()
            .color(0x87c2d8).iconSet(BRIGHT)
            .buildAndRegister();

    public static final Material Ferricore = new Material.Builder(LueTech.id("ferricore"))
            .ingot().ore()
            .color(0xb1e5db).iconSet(METALLIC)
            .buildAndRegister();

    public static final Material Blazegold = new Material.Builder(LueTech.id("blazegold"))
            .ingot().ore()
            .color(0xe68e52).iconSet(METALLIC)
            .buildAndRegister();

    public static final Material Celestigem = new Material.Builder(LueTech.id("celestigem"))
            .gem().ore()
            .color(0x53dfcf).iconSet(DIAMOND)
            .buildAndRegister();

    public static final Material EclipseAlloy = new Material.Builder(LueTech.id("eclipse_alloy"))
            .ingot().ore()
            .color(0x586e75).iconSet(METALLIC)
            .buildAndRegister();

    public static void init() {}

    public static void modifyMaterials() {
        Lutetium.setProperty(PropertyKey.INGOT, new IngotProperty());
        for (var prefix : ALL_ORE_PREFIXES) {
            prefix.setIgnored(Elementium);
            prefix.setIgnored(Ferricore);
            prefix.setIgnored(Blazegold);
            prefix.setIgnored(Celestigem);
            prefix.setIgnored(EclipseAlloy);
            if (prefix != TagPrefix.ore && prefix != TagPrefix.oreNetherrack) {
                prefix.setIgnored(Iesnium);
            }
        }

        /* Occultism */
        TagPrefix.ore.setIgnored(Iesnium, () -> OccultismBlocks.IESNIUM_ORE);
        TagPrefix.oreNetherrack.setIgnored(Iesnium, () -> OccultismBlocks.IESNIUM_ORE_NATURAL);
        TagPrefix.ingot.setIgnored(Iesnium, () -> OccultismItems.IESNIUM_INGOT);
        TagPrefix.nugget.setIgnored(Iesnium, () -> OccultismItems.IESNIUM_NUGGET);
        TagPrefix.dust.setIgnored(Iesnium, () -> OccultismItems.IESNIUM_DUST);
        TagPrefix.block.setIgnored(Iesnium, () -> OccultismBlocks.IESNIUM_BLOCK);
        TagPrefix.rawOre.setIgnored(Iesnium, () -> OccultismItems.RAW_IESNIUM);
        TagPrefix.rawOreBlock.setIgnored(Iesnium, () -> OccultismBlocks.RAW_IESNIUM_BLOCK);

        /* AE2 */
        TagPrefix.gem.setIgnored(CertusQuartz, () -> AEItems.CERTUS_QUARTZ_CRYSTAL);
        TagPrefix.dust.setIgnored(CertusQuartz, () -> AEItems.CERTUS_QUARTZ_DUST);
        TagPrefix.block.setIgnored(CertusQuartz, () -> AEBlocks.QUARTZ_BLOCK);

        TagPrefix.gem.setIgnored(ChargedCertusQuartz, () -> AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED);
        TagPrefix.dust.setIgnored(ChargedCertusQuartz);
        TagPrefix.block.setIgnored(ChargedCertusQuartz);

        TagPrefix.gem.setIgnored(Fluix, () -> AEItems.FLUIX_CRYSTAL);
        TagPrefix.dust.setIgnored(Fluix, () -> AEItems.FLUIX_DUST);
        TagPrefix.block.setIgnored(Fluix, () -> AEBlocks.FLUIX_BLOCK);

        for (var material : new Material[]{ ChargedCertusQuartz, Fluix }) {
            TagPrefix.dustSmall.setIgnored(material);
            TagPrefix.dustTiny.setIgnored(material);
            TagPrefix.gemChipped.setIgnored(material);
            TagPrefix.gemFlawed.setIgnored(material);
            TagPrefix.gemFlawless.setIgnored(material);
            TagPrefix.gemExquisite.setIgnored(material);
        }

        /* ExtendedAE */
        TagPrefix.gem.setIgnored(Entro, () -> EAESingletons.ENTRO_CRYSTAL);
        TagPrefix.dust.setIgnored(Entro, () -> EAESingletons.ENTRO_DUST);
        TagPrefix.block.setIgnored(Entro, () -> EAESingletons.ENTRO_BLOCK);

        TagPrefix.ingot.setIgnored(InfusedEntro, () -> EAESingletons.ENTRO_INGOT);
        TagPrefix.dust.setIgnored(InfusedEntro);
        TagPrefix.block.setIgnored(InfusedEntro);
        TagPrefix.nugget.setIgnored(InfusedEntro);

        for (var material : new Material[]{ Entro, InfusedEntro }) {
            TagPrefix.dustSmall.setIgnored(material);
            TagPrefix.dustTiny.setIgnored(material);
            TagPrefix.gemChipped.setIgnored(material);
            TagPrefix.gemFlawed.setIgnored(material);
            TagPrefix.gemFlawless.setIgnored(material);
            TagPrefix.gemExquisite.setIgnored(material);
        }

        /* JDT */
        TagPrefix.ingot.setIgnored(Ferricore, () -> Registration.FerricoreIngot::get);
        TagPrefix.block.setIgnored(Ferricore, Registration.FerricoreBlock);
        TagPrefix.rawOre.setIgnored(Ferricore, () -> Registration.RawFerricore::get);

        TagPrefix.ingot.setIgnored(Blazegold, () -> Registration.BlazegoldIngot::get);
        TagPrefix.block.setIgnored(Blazegold, Registration.BlazeGoldBlock);
        TagPrefix.rawOre.setIgnored(Blazegold, () -> Registration.RawBlazegold::get);

        TagPrefix.gem.setIgnored(Celestigem, () -> Registration.Celestigem::get);
        TagPrefix.block.setIgnored(Celestigem, Registration.CelestigemBlock);

        TagPrefix.ingot.setIgnored(EclipseAlloy, () -> Registration.EclipseAlloyIngot::get);
        TagPrefix.block.setIgnored(EclipseAlloy, Registration.EclipseAlloyBlock);
        TagPrefix.rawOre.setIgnored(EclipseAlloy, () -> Registration.RawEclipseAlloy::get);
    }
}
