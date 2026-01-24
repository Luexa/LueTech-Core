package sh.lue.luetech.common.machine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BeaconSingleblockConnections {
    public static Map<UUID, Set<MetaMachine>> NETWORK_CONNECTIONS = new Object2ObjectOpenHashMap<>();
    public static Map<MetaMachine, UUID> SINGLEBLOCK_NETWORKS = new Object2ObjectOpenHashMap<>();

    public static void register(@NotNull MetaMachine machine, @NotNull UUID networkUUID) {
        deregister(machine);
        SINGLEBLOCK_NETWORKS.put(machine, networkUUID);
        NETWORK_CONNECTIONS.computeIfAbsent(networkUUID, k -> new ObjectOpenHashSet<>())
                .add(machine);
    }

    public static void deregister(@NotNull MetaMachine machine) {
        var networkUUID = SINGLEBLOCK_NETWORKS.remove(machine);
        if (networkUUID != null) {
            var networkConnections = NETWORK_CONNECTIONS.get(networkUUID);
            if (networkConnections != null) {
                networkConnections.remove(machine);
            }
        }
    }
}
