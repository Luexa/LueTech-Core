package sh.lue.luetech.integration.neoforge;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface IEnhancedConvertedEUReceiver {
    @Nullable IEnergyContainer getEnergyContainer(@Nullable Direction side);
}
