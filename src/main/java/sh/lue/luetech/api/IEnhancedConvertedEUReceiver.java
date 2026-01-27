package sh.lue.luetech.api;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface IEnhancedConvertedEUReceiver {
    @Nullable
    IEnergyContainer luetech$getEnergyContainer(@Nullable Direction side);
}
