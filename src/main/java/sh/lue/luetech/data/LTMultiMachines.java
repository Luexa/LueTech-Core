package sh.lue.luetech.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.RotationState;
import com.gregtechceu.gtceu.api.multiblock.FactoryBlockPattern;
import com.gregtechceu.gtceu.data.block.GTBlocks;
import com.gregtechceu.gtceu.data.recipe.GTRecipeTypes;
import sh.lue.luetech.common.machine.multiblock.electric.DominanceBeaconMachine;

import static com.gregtechceu.gtceu.api.GTValues.*;
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
                var casing = blocks(CASING_PTFE_INERT.get()).setMinGlobalLimited(10);
                return FactoryBlockPattern.start()
                        .aisle("XXX", "XXX", "XXX")
                        .aisle("XXX", "XXX", "XXX")
                        .aisle("XXX", "XSX", "XXX")
                        .where('S', controller(blocks(definition.getBlock())))
                        .where('X', casing)
                        .build();
            })
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"),
                    GTCEu.id("block/multiblock/large_chemical_reactor"))
            .register();

    public static void init() {}
}
