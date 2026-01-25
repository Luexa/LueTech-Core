package sh.lue.luetech.mixins.gtceu;

import com.gregtechceu.gtceu.common.machine.storage.QuantumTankMachine;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = QuantumTankMachine.class, remap = false)
public class QuantumTankMachineMixin {
    @Definition(id = "stored", field = "Lcom/gregtechceu/gtceu/common/machine/storage/QuantumTankMachine;stored:Lnet/neoforged/neoforge/fluids/FluidStack;")
    @Definition(id = "save", method = "Lnet/neoforged/neoforge/fluids/FluidStack;save(Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/nbt/Tag;")
    @Definition(id = "getCurrentBERegistries", method = "Lcom/gregtechceu/gtceu/core/MixinHelpers;getCurrentBERegistries()Lnet/minecraft/core/HolderLookup$Provider;")
    @Expression("this.stored.save(getCurrentBERegistries())")
    @WrapOperation(method = "saveCustomPersistedData", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Tag luetech$fixEmptyStack(FluidStack stored, HolderLookup.Provider registries,
                                      Operation<Tag> operation) {
        return stored.isEmpty() ? new CompoundTag() : operation.call(stored, registries);
    }
}
