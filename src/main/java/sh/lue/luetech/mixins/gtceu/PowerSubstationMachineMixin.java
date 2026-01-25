package sh.lue.luetech.mixins.gtceu;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.PowerSubstationMachine;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PowerSubstationMachine.class, remap = false)
public abstract class PowerSubstationMachineMixin extends WorkableMultiblockMachine {
    private PowerSubstationMachineMixin() {
        super(null);
    }

    @Definition(id = "getNumMaintenanceProblems", method = "Lcom/gregtechceu/gtceu/api/machine/feature/multiblock/IMaintenanceMachine;getNumMaintenanceProblems()I")
    @Definition(id = "maintenance", field = "Lcom/gregtechceu/gtceu/common/machine/multiblock/electric/PowerSubstationMachine;maintenance:Lcom/gregtechceu/gtceu/api/machine/feature/multiblock/IMaintenanceMachine;")
    @Expression("this.maintenance.getNumMaintenanceProblems()")
    @WrapOperation(method = "getPassiveDrain", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int luetech$fixDrainMultiplier(IMaintenanceMachine instance, Operation<Integer> original) {
        if (instance == null) return 0;
        return original.call(instance);
    }

    @Definition(id = "getDurationMultiplier", method = "Lcom/gregtechceu/gtceu/api/machine/feature/multiblock/IMaintenanceMachine;getDurationMultiplier()F")
    @Definition(id = "maintenance", field = "Lcom/gregtechceu/gtceu/common/machine/multiblock/electric/PowerSubstationMachine;maintenance:Lcom/gregtechceu/gtceu/api/machine/feature/multiblock/IMaintenanceMachine;")
    @Expression("this.maintenance.getDurationMultiplier()")
    @WrapOperation(method = "getPassiveDrain", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float luetech$fixDurationModifier(IMaintenanceMachine instance, Operation<Float> original) {
        if (instance == null) return 1.0f;
        return original.call(instance);
    }
}
