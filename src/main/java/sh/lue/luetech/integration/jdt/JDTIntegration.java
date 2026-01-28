package sh.lue.luetech.integration.jdt;

import com.direwolf20.justdirethings.setup.Registration;
import com.gregtechceu.gtceu.api.capability.GTCapability;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.api.IEnhancedConvertedEUReceiver;

@EventBusSubscriber(modid = LueTech.MOD_ID)
public class JDTIntegration {
    @SubscribeEvent
    static void attachCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(GTCapability.CAPABILITY_ENERGY_CONTAINER, (level, pos, state, blockEntity, side) -> {
            if (blockEntity instanceof IEnhancedConvertedEUReceiver enhancedReceiver) {
                return enhancedReceiver.luetech$getEnergyContainer(side);
            }
            return null;
        },
                Registration.BlockBreakerT2.get(),
                Registration.BlockPlacerT2.get(),
                Registration.ClickerT2.get(),
                Registration.SensorT2.get(),
                Registration.DropperT2.get(),
                Registration.BlockSwapperT2.get(),
                Registration.FluidPlacerT2.get(),
                Registration.FluidCollectorT2.get(),
                Registration.ParadoxMachine.get());
    }
}
