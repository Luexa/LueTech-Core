package sh.lue.luetech;

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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import sh.lue.luetech.commands.BigIntegerArgumentType;
import sh.lue.luetech.common.saveddata.LTSavedData;
import sh.lue.luetech.data.*;
import sh.lue.luetech.data.curio.LTCuriosProvider;
import sh.lue.luetech.integration.ftbteams.FTBTeamsIntegration;

@Mod(LueTech.MODID)
@EventBusSubscriber(modid = LueTech.MODID)
public class LueTech {
    @ApiStatus.Internal
    public static IEventBus modEventBus;

    @ApiStatus.Internal
    public static LTSavedData savedData;

    public static final String MODID = "luetech";
    public static final String NAME = "LueTech";
    public static final Logger LOGGER = LogUtils.getLogger();

    @NotNull
    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public LueTech(IEventBus modEventBus, ModContainer modContainer) {
        LueTech.modEventBus = modEventBus;
        LTDatagen.init();

        LTDataComponents.DATA_COMPONENTS.register(modEventBus);
        LTBlocks.init();
        LTItems.init();

        if (LTCompat.FTBTEAMS_LOADED) {
            FTBTeamsIntegration.init();
        }
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
        LTElements.init();
        LTMaterials.init();
        LTMachines.init();
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
}
