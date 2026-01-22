package sh.lue.luetech.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.ChatFormatting;

public class MiscLang {
    public static void init(RegistrateLangProvider provider) {
        // Integration Tooltips
        provider.add("curios.identifier.battery_pack", "Battery Pack");
        provider.add("luetech.ui.bound_spirit.circuit", "%s is bound to this circuit.");

        // EMI Translations
        provider.add("recipe_type.luetech.atomic_rewriter", "Atomic Rewriter");

        // Jade Translations
        provider.add("luetech.jade.beacon_network", "Beacon Network Power");

        // Battery Pack & Wireless Charger
        provider.add("luetech.ui.battery_pack_status.enabled", "Discharge: Enabled");
        provider.add("luetech.ui.battery_pack_status.disabled", "Discharge: Disabled");
        provider.add("luetech.ui.wireless_charger.remote_status", "Remote: %s");
        provider.add("luetech.ui.wireless_charger.remote_status_tier", "Remote: %s (Tier %s)");
        provider.add("luetech.ui.wireless_charger.remote_status.disconnected", "Disconnected");
        provider.add("luetech.ui.wireless_charger.remote_status.active", "Active");
        provider.add("luetech.ui.wireless_charger.remote_status.inactive", "Paused");

        // Unique Multiblocks
        provider.add("luetech.multiblock.unique_disabled", ChatFormatting.RED + "Duplicate of unique multiblock.");

        // Beacon of Dominance
        provider.add("luetech.multiblock.beacon.network_disabled", ChatFormatting.YELLOW + "Network disabled.");
        provider.add("luetech.multiblock.beacon.network_active", ChatFormatting.GREEN + "Network active.");
        provider.add("luetech.multiblock.beacon.network_inactive", ChatFormatting.AQUA + "Network inactive.");

        // Eldritch Hatches
        provider.add("luetech.machine.eldritch_energy_hatch.uplink.voltage",
                ChatFormatting.GREEN + "Voltage UPLOAD: " + ChatFormatting.RESET + "%s EU/t (%s" + ChatFormatting.RESET + ")");
        provider.add("luetech.machine.eldritch_energy_hatch.uplink.amperage",
                ChatFormatting.YELLOW + "Amperage UPLOAD: " + ChatFormatting.RESET + "%sA");
        provider.add("luetech.machine.eldritch_energy_hatch.uplink.tooltip",
                "Energy Uploader for Multiblocks");
        provider.add("luetech.machine.eldritch_energy_hatch.uplink_multiamp.tooltip",
                "Multiple Ampere Energy Uploader for Multiblocks");
        provider.add("luetech.machine.eldritch_energy_hatch.downlink.voltage",
                ChatFormatting.GREEN + "Voltage DOWNLOAD: " + ChatFormatting.RESET + "%s EU/t (%s" + ChatFormatting.RESET + ")");
        provider.add("luetech.machine.eldritch_energy_hatch.downlink.amperage",
                ChatFormatting.YELLOW + "Amperage DOWNLOAD: " + ChatFormatting.RESET + "%sA");
        provider.add("luetech.machine.eldritch_energy_hatch.downlink.tooltip",
                "Energy Downloader for Multiblocks");
        provider.add("luetech.machine.eldritch_energy_hatch.downlink_multiamp.tooltip",
                "Multiple Ampere Energy Downloader for Multiblocks");
    }
}
