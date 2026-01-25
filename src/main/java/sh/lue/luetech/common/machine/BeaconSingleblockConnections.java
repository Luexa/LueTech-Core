package sh.lue.luetech.common.machine;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BeaconSingleblockConnections {
    public static Map<UUID, Set<IBeaconConnected>> NETWORK_CONNECTIONS = new Object2ObjectOpenHashMap<>();
    public static Map<IBeaconConnected, UUID> SINGLEBLOCK_NETWORKS = new Object2ObjectOpenHashMap<>();

    public static void register(@NotNull IBeaconConnected machine, @NotNull UUID networkUUID) {
        deregister(machine);
        SINGLEBLOCK_NETWORKS.put(machine, networkUUID);
        NETWORK_CONNECTIONS.computeIfAbsent(networkUUID, k -> new ObjectOpenHashSet<>())
                .add(machine);
    }

    public static void deregister(@NotNull IBeaconConnected machine) {
        var networkUUID = SINGLEBLOCK_NETWORKS.remove(machine);
        if (networkUUID != null) {
            NETWORK_CONNECTIONS.computeIfPresent(networkUUID, (k, v) -> {
                v.remove(machine);
                return v.isEmpty() ? null : v;
            });
        }
    }
}
