package sh.lue.luetech.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.*;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.machine.GTMachineUtils;
import com.gregtechceu.gtceu.data.medicalcondition.GTMedicalConditions;
import com.gregtechceu.gtceu.data.recipe.GTRecipeModifiers;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.multiblock.part.EldritchEnergyHatchPartMachine;

import java.util.Locale;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.data.machine.GTMachineUtils.ALL_TIERS;
import static com.gregtechceu.gtceu.data.machine.GTMachineUtils.ELECTRIC_TIERS;
import static com.gregtechceu.gtceu.data.machine.GTMachineUtils.defaultEnvironmentRequirement;
import static com.gregtechceu.gtceu.data.machine.GTMachineUtils.defaultTankSizeFunction;
import static com.gregtechceu.gtceu.data.machine.GTMachineUtils.workableTiered;
import static com.gregtechceu.gtceu.utils.FormattingUtil.toEnglishName;
import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;

public class LTMachineUtils {
    @NotNull
    public static MachineDefinition[] registerTieredMachines(
            @NotNull String name,
            @NotNull NonNullBiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
            @NotNull NonNullBiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
            int... tiers
    ) {
        boolean isHighTier = GTCEuAPI.isHighTier();
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            if (!isHighTier && tier >= GTValues.UHV) continue;
            var register = REGISTRATE
                    .machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                            holder -> factory.apply(holder, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    @NotNull
    public static MachineDefinition[] registerSimpleMachines(@NotNull String name, @NotNull GTRecipeType recipeType) {
        return registerSimpleMachines(name, recipeType, defaultTankSizeFunction);
    }

    @NotNull
    public static MachineDefinition[] registerSimpleMachines(
            @NotNull String name,
            @NotNull GTRecipeType recipeType,
            @NotNull Int2IntFunction tankScalingFunction
    ) {
        return registerSimpleMachines(name, recipeType, tankScalingFunction, false);
    }

    @NotNull
    public static MachineDefinition[] registerSimpleMachines(
            @NotNull String name,
            @NotNull GTRecipeType recipeType,
            @NotNull Int2IntFunction tankScalingFunction,
            boolean hasPollutionDebuff
    ) {
        return registerSimpleMachines(name, recipeType, tankScalingFunction, hasPollutionDebuff, ELECTRIC_TIERS);
    }

    @NotNull
    public static MachineDefinition[] registerSimpleMachines(
            @NotNull String name,
            @NotNull GTRecipeType recipeType,
            @NotNull Int2IntFunction tankScalingFunction,
            boolean hasPollutionDebuff,
            int... tiers
    ) {
        return registerTieredMachines(name, (holder, tier) -> new SimpleTieredMachine(holder, tier, tankScalingFunction),
                (tier, builder) -> {
                    if (hasPollutionDebuff) {
                        builder.recipeModifiers(GTRecipeModifiers.ENVIRONMENT_REQUIREMENT
                                .apply(GTMedicalConditions.CARBON_MONOXIDE_POISONING, 100 * tier),
                                GTRecipeModifiers.OC_NON_PERFECT)
                                .conditionalTooltip(defaultEnvironmentRequirement(),
                                        ConfigHolder.INSTANCE.gameplay.environmentalHazards);
                    } else {
                        builder.recipeModifier(GTRecipeModifiers.OC_NON_PERFECT);
                    }
                    return builder
                            .langValue("%s %s %s".formatted(VLVH[tier], toEnglishName(name), VLVT[tier]))
                            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(LueTech.id(name), recipeType))
                            .rotationState(RotationState.NON_Y_AXIS)
                            .recipeType(recipeType)
                            .workableTieredHullModel(LueTech.id("block/machines/" + name))
                            .tooltips(workableTiered(tier, V[tier], V[tier] * 64L, recipeType,
                                    tankScalingFunction.applyAsInt(tier), true))
                            .register();
                },
                tiers);
    }

    @NotNull
    public static MachineDefinition[] registerEldritchHatches(int amperage, boolean uplink) {
        String variant = uplink ? "uplink" : "downlink";
        String multiOrNot = variant + (amperage == 2 ? "" : "_multiamp");
        String id = "eldritch_energy_" + variant + "_hatch" + (amperage == 2 ? "" : "_" + amperage + "a");
        String nameSuffix = " Eldritch Energy " + (amperage == 2 ? "" : amperage + "A ") +
                (uplink ? "Up" : "Down") + "link Hatch";
        String overlayPath = "eldritch_" + (uplink ? "input" : "output") + "_" + amperage + "a";
        return registerTieredMachines(
                id,
                (holder, tier) -> new EldritchEnergyHatchPartMachine(holder, tier, uplink, amperage),
                (tier, builder) -> builder
                        .langValue(VNF[tier] + nameSuffix)
                        .rotationState(RotationState.ALL)
                        .abilities(uplink ? PartAbility.OUTPUT_ENERGY : PartAbility.INPUT_ENERGY)
                        .tooltips(Component.translatable("luetech.machine.eldritch_energy_hatch." + variant + ".voltage",
                                        FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                                Component.translatable("luetech.machine.eldritch_energy_hatch." + variant + ".amperage", amperage),
                                Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                                        FormattingUtil
                                                .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, amperage))),
                                Component.translatable("luetech.machine.eldritch_energy_hatch." + multiOrNot + ".tooltip"))
                        .overlayTieredHullModel(overlayPath)
                        .register(),
                ALL_TIERS);
    }
}
