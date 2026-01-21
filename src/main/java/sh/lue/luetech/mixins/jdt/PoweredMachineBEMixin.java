package sh.lue.luetech.mixins.jdt;

import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PoweredMachineBE.class, remap = false)
public interface PoweredMachineBEMixin {
    @Inject(method = "chargeItemStack", at = @At("TAIL"))
    private void luetech$chargeItemStack(ItemStack itemStack, CallbackInfo ci) {
        var electricItem = GTCapabilityHelper.getElectricItem(itemStack);
        if (electricItem != null) {
            int ratio = FeCompat.ratio(true);
            long toCharge = 5000L / ratio;
            long acceptedCharge = electricItem.charge(toCharge, electricItem.getTier(), false, true);
            var energyStorage = ((PoweredMachineBE)(Object)this).getEnergyStorage();
            int extractedFE = energyStorage.extractEnergy((int)(acceptedCharge * 4L), true);
            extractedFE -= extractedFE % ratio;
            if (extractedFE > 0) {
                energyStorage.extractEnergy(extractedFE, false);
                electricItem.charge(extractedFE / ratio, electricItem.getTier(), true, false);
            }
        }
    }
}
