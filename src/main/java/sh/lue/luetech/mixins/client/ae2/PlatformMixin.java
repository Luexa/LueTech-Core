package sh.lue.luetech.mixins.client.ae2;

import appeng.api.config.PowerUnit;
import appeng.util.Platform;
import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Platform.class, remap = false)
public class PlatformMixin {
    @Definition(id = "AE", field = "Lappeng/api/config/PowerUnit;AE:Lappeng/api/config/PowerUnit;")
    @Definition(id = "convertTo", method = "Lappeng/api/config/PowerUnit;convertTo(Lappeng/api/config/PowerUnit;D)D")
    @Definition(id = "displayUnits", local = @Local(type = PowerUnit.class))
    @Definition(id = "p", local = @Local(type = double.class, argsOnly = true))
    @Expression("AE.convertTo(displayUnits, p)")
    @WrapOperation(method = "formatPower", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static double luetech$convertFEToEU(PowerUnit sourceUnit, PowerUnit targetUnit, double p, Operation<Double> originalOp) {
        double result = originalOp.call(sourceUnit, targetUnit, p);
        if (targetUnit == PowerUnit.FE) {
            result /= FeCompat.ratio(false);
        }
        return result;
    }

    @Definition(id = "displayUnits", local = @Local(type = PowerUnit.class))
    @Definition(id = "getSymbolName", method = "Lappeng/api/config/PowerUnit;getSymbolName()Ljava/lang/String;")
    @Expression("displayUnits.getSymbolName()")
    @WrapOperation(method = "formatPower", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static String luetech$setEUUnitName(PowerUnit unit, Operation<String> originalOp) {
        if (unit == PowerUnit.FE) {
            return "EU";
        } else {
            return originalOp.call(unit);
        }
    }
}
