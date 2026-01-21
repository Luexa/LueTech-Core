package sh.lue.luetech.data;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.RotationState;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import net.minecraft.network.chat.Component;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.electric.*;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;
import static sh.lue.luetech.data.LTMachineUtils.*;

public class LTMachines {
    static {
        REGISTRATE.creativeModeTab(LTCreativeModeTabs.MACHINE);
    }

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
