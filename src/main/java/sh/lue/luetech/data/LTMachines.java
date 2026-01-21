package sh.lue.luetech.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.RotationState;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import net.minecraft.network.chat.Component;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.electric.*;
import sh.lue.luetech.common.machine.multiblock.part.EldritchEnergyHatchPartMachine;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.data.machine.GTMachineUtils.ALL_TIERS;
import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;
import static sh.lue.luetech.data.LTMachineUtils.*;

public class LTMachines {
    static {
        REGISTRATE.creativeModeTab(LTCreativeModeTabs.MACHINE);
    }

    public static final MachineDefinition[] ELDRITCH_ENERGY_UPLINK_HATCH = registerTieredMachines(
            "eldritch_energy_uplink_hatch",
            (holder, tier) -> new EldritchEnergyHatchPartMachine(holder, tier, true, 2),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " Eldritch Energy Uplink Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_out",
                                    FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtceu.universal.tooltip.amperage_out", 2),
                            Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))),
                            Component.translatable("gtceu.machine.energy_hatch.output.tooltip"))
                    .overlayTieredHullModel(GTCEu.id("block/machine/part/energy_output_hatch"))
                    .register(),
            ALL_TIERS);

    public static final MachineDefinition[] ELDRITCH_ENERGY_DOWNLINK_HATCH = registerTieredMachines(
            "eldritch_energy_downlink_hatch",
            (holder, tier) -> new EldritchEnergyHatchPartMachine(holder, tier, false, 2),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " Eldritch Energy Downlink Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.INPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_in",
                                    FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtceu.universal.tooltip.amperage_in", 2),
                            Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))),
                            Component.translatable("gtceu.machine.energy_hatch.input.tooltip"))
                    .overlayTieredHullModel(GTCEu.id("block/machine/part/energy_input_hatch"))
                    .register(),
            ALL_TIERS);

    public static final MachineDefinition[] CHARGE_TRANSMITTER = registerTieredMachines(
            "charge_transmitter", ChargeTransmitterMachine::new,
            (tier, builder) -> builder
                    .rotationState(RotationState.NONE)
                    .workableTieredHullModel(LueTech.id("block/machines/charge_transmitter"))
                    .langValue("%s Charge Transmitter %s".formatted(VLVH[tier], VLVT[tier]))
                    .tooltips(
                            Component.translatable("gtceu.universal.tooltip.voltage_in_out",
                                    FormattingUtil.formatNumbers(V[tier]),
                                    VNF[tier]),
                            Component.translatable("gtceu.universal.tooltip.amperage_in_till",
                                    ChargeTransmitterMachine.AMPS)
                    )
                    .register(),
            tiersBetween(IV, MAX));

    public static final MachineDefinition[] ATOMIC_REWRITER = registerSimpleMachines("atomic_rewriter",
            LTRecipeTypes.ATOMIC_REWRITER_RECIPES);

    public static void init() {
        LTMultiMachines.init();
    }
}
