package sh.lue.luetech.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.RotationState;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.material.ChemicalHelper;
import com.gregtechceu.gtceu.api.multiblock.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.multiblock.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import com.gregtechceu.gtceu.data.material.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.GTRecipeTypes;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import sh.lue.luetech.common.machine.multiblock.electric.DominanceBeaconMachine;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.api.multiblock.Predicates.*;
import static com.gregtechceu.gtceu.data.block.GTBlocks.*;
import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;

public class LTMultiMachines {
    static {
        REGISTRATE.creativeModeTab(LTCreativeModeTabs.MACHINE);
    }

    public static final MultiblockMachineDefinition BEACON_OF_DOMINANCE = REGISTRATE
            .multiblock("beacon_of_dominance", DominanceBeaconMachine::new)
            .langValue("Beacon of Dominance")
            .rotationState(RotationState.NON_Y_AXIS)
            .allowExtendedFacing(false)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .appearance(CASING_PTFE_INERT::getDefaultState)
            .pattern(definition -> {
                var casing = blocks(CASING_PTFE_INERT.get());
                Set<Block> forbiddenHatches = new ObjectOpenHashSet<>();
                for (var hatchDefinitions : new MachineDefinition[][]{
                        LTMachines.ELDRITCH_DOWNLINK_HATCH,
                        LTMachines.ELDRITCH_DOWNLINK_HATCH_4A,
                        LTMachines.ELDRITCH_DOWNLINK_HATCH_16A,
                }) {
                    for (var hatchDefinition : hatchDefinitions) {
                        if (hatchDefinition != null) {
                            forbiddenHatches.add(hatchDefinition.getBlock());
                        }
                    }
                }
                var casingOrHatch = blocks(Stream.of(
                        PartAbility.INPUT_ENERGY,
                        PartAbility.SUBSTATION_INPUT_ENERGY,
                        PartAbility.INPUT_LASER)
                        .map(PartAbility::getAllBlocks)
                        .flatMap(Collection::stream)
                        .filter(b -> !forbiddenHatches.contains(b))
                        .toArray(Block[]::new)).or(casing);
                return FactoryBlockPattern.start()
                        .aisle(
                                "F     F",
                                "F     F",
                                "F     F",
                                "F     F",
                                " FFFFF ",
                                "       ",
                                "       ",
                                "       ",
                                "       ",
                                "       ",
                                "       ",
                                "       ")
                        .aisle(
                                "   C   ",
                                "   C   ",
                                "   C   ",
                                "   C   ",
                                "FCCCCCF",
                                " F   F ",
                                " F   F ",
                                " F   F ",
                                " F   F ",
                                " FFFFF ",
                                "       ",
                                "       ")
                        .aisle(
                                "  CCC  ",
                                "  GAG  ",
                                "  GAG  ",
                                "  GAG  ",
                                "FCCCCCF",
                                "  XXX  ",
                                "  XXX  ",
                                "  XXX  ",
                                "  XXX  ",
                                " FIIIF ",
                                "  FGF  ",
                                "   F   ")
                        .aisle(
                                " CCCCC ",
                                " CALAC ",
                                " CALAC ",
                                " CALAC ",
                                "FCCLCCF",
                                "  XLX  ",
                                "  XLX  ",
                                "  XLX  ",
                                "  XLX  ",
                                " FIIIF ",
                                "  GBG  ",
                                "  FFF  ")
                        .aisle(
                                "  CCC  ",
                                "  GAG  ",
                                "  GAG  ",
                                "  GAG  ",
                                "FCCCCCF",
                                "  XXX  ",
                                "  XXX  ",
                                "  XXX  ",
                                "  XXX  ",
                                " FIIIF ",
                                "  FGF  ",
                                "   F   ")
                        .aisle(
                                "   C   ",
                                "   S   ",
                                "   C   ",
                                "   C   ",
                                "FCCCCCF",
                                " F   F ",
                                " F   F ",
                                " F   F ",
                                " F   F ",
                                " FFFFF ",
                                "       ",
                                "       ")
                        .aisle(
                                "F     F",
                                "F     F",
                                "F     F",
                                "F     F",
                                " FFFFF ",
                                "       ",
                                "       ",
                                "       ",
                                "       ",
                                "       ",
                                "       ",
                                "       ")
                        .where(' ', any())
                        .where('S', controller(blocks(definition.getBlock())))
                        .where('C', casing)
                        .where('X', casingOrHatch)
                        .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Neutronium)))
                        .where('G', blocks(FUSION_GLASS.get()))
                        .where('A', air())
                        .where('L', blocks(SUPERCONDUCTING_COIL.get()))
                        .where('I', blocks(OccultismBlocks.IESNIUM_BLOCK.get()))
                        .where('B', blocks(Blocks.BEACON))
                        .build();
            })
            .shapeInfo(definition -> MultiblockShapeInfo.builder()
                    .aisle(
                            "F     F",
                            "F     F",
                            "F     F",
                            "F     F",
                            " FFFFF ",
                            "       ",
                            "       ",
                            "       ",
                            "       ",
                            "       ",
                            "       ",
                            "       ")
                    .aisle(
                            "   C   ",
                            "   S   ",
                            "   C   ",
                            "   C   ",
                            "FCCCCCF",
                            " F   F ",
                            " F   F ",
                            " F   F ",
                            " F   F ",
                            " FFFFF ",
                            "       ",
                            "       ")
                    .aisle(
                            "  CCC  ",
                            "  GAG  ",
                            "  GAG  ",
                            "  GAG  ",
                            "FCCCCCF",
                            "  CCC  ",
                            "  CCC  ",
                            "  CCC  ",
                            "  CCC  ",
                            " FIIIF ",
                            "  FGF  ",
                            "   F   ")
                    .aisle(
                            " CCCCC ",
                            " CALAC ",
                            " CALAC ",
                            " CALAC ",
                            "FCCLCCF",
                            "  CLC  ",
                            "  CLC  ",
                            "  CLC  ",
                            "  CLC  ",
                            " FIIIF ",
                            "  GBG  ",
                            "  FFF  ")
                    .aisle(
                            "  CCC  ",
                            "  GAG  ",
                            "  GAG  ",
                            "  GAG  ",
                            "FCCCCCF",
                            "  CCC  ",
                            "  CCC  ",
                            "  CCC  ",
                            "  CCC  ",
                            " FIIIF ",
                            "  FGF  ",
                            "   F   ")
                    .aisle(
                            "   C   ",
                            "   C   ",
                            "   C   ",
                            "   C   ",
                            "FCCCCCF",
                            " F   F ",
                            " F   F ",
                            " F   F ",
                            " F   F ",
                            " FFFFF ",
                            "       ",
                            "       ")
                    .aisle(
                            "F     F",
                            "F     F",
                            "F     F",
                            "F     F",
                            " FFFFF ",
                            "       ",
                            "       ",
                            "       ",
                            "       ",
                            "       ",
                            "       ",
                            "       ")
                    .where('S', definition, Direction.NORTH)
                    .where('C', CASING_PTFE_INERT.getDefaultState())
                    .where('F', Objects.requireNonNull(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Neutronium))
                            .defaultBlockState())
                    .where('G', FUSION_GLASS.getDefaultState())
                    .where('A', Blocks.AIR.defaultBlockState())
                    .where('L', SUPERCONDUCTING_COIL.getDefaultState())
                    .where('I', OccultismBlocks.IESNIUM_BLOCK.get().defaultBlockState())
                    .where('B', Blocks.BEACON.defaultBlockState())
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"),
                    GTCEu.id("block/multiblock/large_chemical_reactor"))
            .register();

    public static void init() {}
}
