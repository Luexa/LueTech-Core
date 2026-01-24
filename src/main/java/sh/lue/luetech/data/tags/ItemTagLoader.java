package sh.lue.luetech.data.tags;

import com.direwolf20.justdirethings.setup.Registration;
import com.gregtechceu.gtceu.data.tag.CustomTags;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.item.Item;
import sh.lue.luetech.LTCompat;
import sh.lue.luetech.data.LTItems;
import sh.lue.luetech.data.LTTags;

import java.util.Objects;

import static com.gregtechceu.gtceu.api.GTValues.ALL_TIERS;

public class ItemTagLoader {
    public static void init(RegistrateTagsProvider<Item> provider) {
        provider.addTag(CustomTags.ZPM_CIRCUITS)
                .addOptional(LTItems.SPECTRAL_PROCESSOR_ZPM.getId());
        provider.addTag(CustomTags.UV_CIRCUITS)
                .addOptional(LTItems.SPECTRAL_PROCESSOR_ASSEMBLY_UV.getId())
                .addOptional(LTItems.ELEMENTAL_PROCESSOR_UV.getId());
        provider.addTag(CustomTags.UHV_CIRCUITS)
                .addOptional(LTItems.SPECTRAL_PROCESSOR_SUPERCOMPUTER_UHV.getId())
                .addOptional(LTItems.ELEMENTAL_PROCESSOR_ASSEMBLY_UHV.getId())
                .addOptional(LTItems.TEMPORAL_PROCESSOR_UHV.getId());
        provider.addTag(CustomTags.UEV_CIRCUITS)
                .addOptional(LTItems.SPECTRAL_PROCESSOR_MAINFRAME_UEV.getId())
                .addOptional(LTItems.ELEMENTAL_PROCESSOR_SUPERCOMPUTER_UEV.getId())
                .addOptional(LTItems.TEMPORAL_PROCESSOR_ASSEMBLY_UEV.getId())
                .addOptional(LTItems.HYPERFOLDED_PROCESSOR_UEV.getId());
        provider.addTag(CustomTags.UIV_CIRCUITS)
                .addOptional(LTItems.ELEMENTAL_PROCESSOR_MAINFRAME_UIV.getId())
                .addOptional(LTItems.TEMPORAL_PROCESSOR_SUPERCOMPUTER_UIV.getId())
                .addOptional(LTItems.HYPERFOLDED_PROCESSOR_ASSEMBLY_UIV.getId());
        provider.addTag(CustomTags.UXV_CIRCUITS)
                .addOptional(LTItems.TEMPORAL_PROCESSOR_MAINFRAME_UXV.getId())
                .addOptional(LTItems.HYPERFOLDED_PROCESSOR_SUPERCOMPUTER_UXV.getId());
        provider.addTag(CustomTags.OpV_CIRCUITS)
                .addOptional(LTItems.HYPERFOLDED_PROCESSOR_MAINFRAME_OpV.getId());
        provider.addTag(CustomTags.MAX_CIRCUITS)
                .addOptional(LTItems.LUE_PROCESSOR_MAX.getId());

        var universalCircuitAppender = provider.addTag(LTTags.UNIVERSAL_CIRCUITS);
        for (int tier : ALL_TIERS) {
            var circuitId = Objects.requireNonNull(LTItems.UNIVERSAL_CIRCUITS[tier]).getId();
            universalCircuitAppender.addOptional(circuitId);
            provider.addTag(CustomTags.CIRCUITS_ARRAY[tier])
                    .addOptional(circuitId);
        }

        provider.addTag(LTTags.REVIVE_TIMEWIND_GOO)
                .addOptional(Registration.TimeCrystal.getId());
    }
}
