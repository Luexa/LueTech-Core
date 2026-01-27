package sh.lue.luetech.api;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public interface IEnergyStorageProvider {
    @Nullable
    IEnergyStorage luetech$getEnergyStorage(@Nullable Direction side);
}
