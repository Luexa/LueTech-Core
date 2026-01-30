package sh.lue.luetech;

import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.material.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifierList;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.data.machine.GTMultiMachines;
import com.gregtechceu.gtceu.data.recipe.GTRecipeModifiers;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.CrashReportCallables;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import sh.lue.luetech.api.BeaconSingleblockConnections;
import sh.lue.luetech.api.IEnergyStorageProvider;
import sh.lue.luetech.commands.BigIntegerArgumentType;
import sh.lue.luetech.common.blockentity.EldritchEnergyAcceptorBlockEntity;
import sh.lue.luetech.common.machine.multiblock.electric.DominanceBeaconMachine;
import sh.lue.luetech.common.saveddata.LTSavedData;
import sh.lue.luetech.data.*;
import sh.lue.luetech.data.curio.LTCuriosProvider;
import sh.lue.luetech.integration.ftbteams.FTBTeamsIntegration;
import sh.lue.luetech.utils.BigIntegerUtils;

import java.math.BigInteger;

@Mod(LueTech.MOD_ID)
@EventBusSubscriber(modid = LueTech.MOD_ID)
public class LueTech {
    @ApiStatus.Internal
    public static IEventBus modEventBus;

    @ApiStatus.Internal
    public static LTSavedData savedData;

    public static long tickCount = 0L;

    public static final String MOD_ID = "luetech";
    public static final String NAME = "LueTech";
    public static final Logger LOGGER = LogUtils.getLogger();

    @NotNull
    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public LueTech(IEventBus modEventBus, ModContainer modContainer) {
        LueTech.modEventBus = modEventBus;
        LTDatagen.init();

        LTDataComponents.DATA_COMPONENTS.register(modEventBus);
        LTAttachments.ATTACHMENT_TYPES.register(modEventBus);
        LTBlocks.init();
        LTItems.init();

        if (LTCompat.FTBTEAMS_LOADED) {
            FTBTeamsIntegration.init();
        }

        CrashReportCallables.registerHeader(() -> "NOTICE: Instance contains LueTech, which extensively mixins certain mods!");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void registerEarly(@NotNull RegisterEvent event) {
        event.register(GTRegistries.ELEMENT_REGISTRY, registry -> {
            LTElements.init();
        });
        event.register(GTRegistries.MATERIAL_REGISTRY, registry -> {
            LTMaterials.init();
        });
        event.register(GTRegistries.MACHINE_REGISTRY, registry -> {
            LTMachines.init();
        });
    }

    @SubscribeEvent
    static void register(@NotNull RegisterEvent event) {
        event.register(GTRegistries.MACHINE_REGISTRY, registry -> {
            GTMultiMachines.ASSEMBLY_LINE.setRecipeModifier(new RecipeModifierList(
                    GTRecipeModifiers.DEFAULT_ENVIRONMENT_REQUIREMENT,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK)));
        });
        event.register(Registries.COMMAND_ARGUMENT_TYPE, registry -> {
            registry.register(LueTech.id("big_integer"), ArgumentTypeInfos.registerByClass(
                    BigIntegerArgumentType.class,
                    new BigIntegerArgumentType.Info()
            ));
        });
    }

    @SubscribeEvent
    static void modifyMaterials(PostMaterialEvent event) {
        LTMaterials.modifyMaterials();
    }

    @SubscribeEvent
    static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeServer(),
                new LTCuriosProvider(
                        event.getGenerator().getPackOutput(),
                        event.getExistingFileHelper(),
                        event.getLookupProvider()
                )
        );
    }

    @SubscribeEvent
    static void onLevelLoad(LevelEvent.Load event) {
        if (savedData == null && event.getLevel() instanceof ServerLevel serverLevel) {
            savedData = LTSavedData.getInstance(serverLevel);
        }
    }

    @SubscribeEvent
    static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            if (serverLevel.getServer().overworld() == serverLevel) {
                savedData = null;
            }
        }
    }

    @SubscribeEvent
    static void attachCapabilities(RegisterCapabilitiesEvent event) {
        LTBlocks.attachCapabilities(event);
    }

    @SubscribeEvent
    static void onPostServerTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel && serverLevel.getServer().overworld() == serverLevel) {
            tickCount += 1;
            for (var networkEntries : BeaconSingleblockConnections.NETWORK_CONNECTIONS.entrySet()) {
                var networkUUID = networkEntries.getKey();
                var network = savedData.dominanceBeacon.getNetwork(networkUUID);
                if (network == null || !network.getActive()) continue;
                for (var machine : networkEntries.getValue()) {
                    if (machine instanceof TieredEnergyMachine energyMachine) {
                        var networkPower = network.getStoredPower();
                        long voltage = energyMachine.getMaxVoltage();
                        long amps = BigIntegerUtils.saturatedLong(networkPower) / voltage;
                        if (amps > 0) {
                            long acceptedAmps = energyMachine.energyContainer.acceptEnergyFromNetwork(null, voltage, amps);
                            if (acceptedAmps > 0) {
                                network.setStoredPower(networkPower.subtract(BigInteger.valueOf(acceptedAmps * voltage)));
                            }
                        }
                    } else if (machine instanceof IEnergyStorageProvider energyStorageProvider) {
                        var energyStorage = energyStorageProvider.luetech$getEnergyStorage(null);
                        if (energyStorage != null) {
                            int ratio = FeCompat.ratio(false);
                            var networkPower = network.getStoredPower();
                            long availableEU = BigIntegerUtils.saturatedInt(networkPower);
                            int availableFE = (int) Math.min(Integer.MAX_VALUE, availableEU * ratio);
                            int acceptedFE = energyStorage.receiveEnergy(availableFE, false);
                            if (acceptedFE > 0) {
                                network.setStoredPower(networkPower.subtract(BigInteger.valueOf(Math.ceilDiv(acceptedFE, ratio))));
                            }
                        }
                    }
                }
            }
            for (var beacon : DominanceBeaconMachine.ALL_INSTANCES) {
                beacon.beaconTick();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void onPostServerTickLate(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel && serverLevel.getServer().overworld() == serverLevel) {
            for (var eldritchAcceptor : EldritchEnergyAcceptorBlockEntity.INSTANCES) {
                eldritchAcceptor.stockBuffer();
            }
        }
    }
}
