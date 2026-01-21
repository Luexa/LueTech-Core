package sh.lue.luetech.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.ChatFormatting;

public class MiscLang {
    public static void init(RegistrateLangProvider provider) {
        provider.add("curios.identifier.battery_pack", "Battery Pack");
        provider.add("recipe_type.luetech.atomic_rewriter", "Atomic Rewriter");
        provider.add("luetech.ui.battery_pack_status.enabled", "Discharge: Enabled");
        provider.add("luetech.ui.battery_pack_status.disabled", "Discharge: Disabled");
        provider.add("luetech.ui.wireless_charger.remote_status", "Remote: %s");
        provider.add("luetech.ui.wireless_charger.remote_status_tier", "Remote: %s (Tier %s)");
        provider.add("luetech.ui.wireless_charger.remote_status.disconnected", "Disconnected");
        provider.add("luetech.ui.wireless_charger.remote_status.active", "Active");
        provider.add("luetech.ui.wireless_charger.remote_status.inactive", "Paused");
        provider.add("luetech.ui.bound_spirit.circuit", "%s is bound to this circuit.");
        provider.add("luetech.multiblock.unique_disabled", ChatFormatting.RED + "Duplicate of unique multiblock.");
        provider.add("luetech.multiblock.beacon.network_disabled", ChatFormatting.YELLOW + "Network disabled.");
        provider.add("luetech.multiblock.beacon.network_active", ChatFormatting.GREEN + "Network active.");
        provider.add("luetech.multiblock.beacon.network_inactive", ChatFormatting.AQUA + "Network inactive.");
        provider.add("luetech.multiblock.beacon.network_uuid", "Network UUID: %s");
    }
}
