package sh.lue.luetech.common.saveddata.beacon;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LueTech;

import java.math.BigInteger;
import java.util.*;

public class BeaconSavedData {
    @NotNull
    final Map<UUID, BeaconNetwork> networksByNetworkUUID;
    @NotNull
    final Map<UUID, BeaconNetwork> networksByPlayerUUID;
    @NotNull
    final Map<UUID, BeaconNetwork> networksByTeamUUID;

    @ApiStatus.Internal
    public BeaconSavedData() {
        this(Collections.emptyList());
    }

    private BeaconSavedData(Collection<BeaconNetwork> beaconNetworks) {
        networksByNetworkUUID = new Object2ObjectOpenHashMap<>();
        networksByPlayerUUID = new Object2ObjectOpenHashMap<>();
        networksByTeamUUID = new Object2ObjectOpenHashMap<>();

        for (var network : beaconNetworks) {
            networksByNetworkUUID.put(network.uuid, network);
            networksByPlayerUUID.put(network.playerUUID, network);
            if (network.teamUUID != null) {
                networksByTeamUUID.put(network.teamUUID, network);
            }
        }
    }

    @Nullable
    public BeaconNetwork teamNetwork(@Nullable UUID teamUUID) {
        if (teamUUID == null) return null;
        return networksByTeamUUID.get(teamUUID);
    }

    @Nullable
    public BeaconNetwork playerNetwork(@Nullable UUID playerUUID) {
        if (playerUUID == null) return null;
        return networksByPlayerUUID.get(playerUUID);
    }

    public void setNetworkPlayer(@NotNull BeaconNetwork network, @NotNull UUID playerUUID) {
        if (network.playerUUID.equals(playerUUID)) return;
        if (networksByPlayerUUID.get(network.playerUUID) == network) {
            networksByPlayerUUID.remove(network.playerUUID);
        }
        networksByPlayerUUID.put(playerUUID, network);
        network.setPlayerUUID(playerUUID);
    }

    public void setNetworkTeam(@NotNull BeaconNetwork network, @Nullable UUID teamUUID) {
        if (Objects.equals(network.teamUUID, teamUUID)) return;
        if (network.teamUUID != null && networksByTeamUUID.get(network.teamUUID) == network) {
            networksByTeamUUID.remove(network.teamUUID);
        }
        if (teamUUID != null) {
            networksByTeamUUID.put(teamUUID, network);
        }
        network.setTeamUUID(teamUUID);
    }

    public void deleteNetwork(@NotNull UUID networkUUID) {
        var network = networksByNetworkUUID.get(networkUUID);
        if (network == null) return;
        if (network.teamUUID != null) {
            var teamNetwork = networksByTeamUUID.get(network.teamUUID);
            if (teamNetwork == network) {
                networksByTeamUUID.remove(network.teamUUID);
            }
        }
        var playerNetwork = networksByPlayerUUID.get(network.playerUUID);
        if (playerNetwork == network) {
            networksByPlayerUUID.remove(network.playerUUID);
        }
        networksByNetworkUUID.remove(network.uuid);
        LueTech.savedData.setDirty();
    }

    @NotNull
    public BeaconNetwork createNetwork(@NotNull UUID playerUUID, @Nullable UUID teamUUID) {
        var network = new BeaconNetwork(UUID.randomUUID(), playerUUID, teamUUID, BigInteger.ZERO, BigInteger.ZERO, false);
        networksByNetworkUUID.put(network.uuid, network);
        networksByPlayerUUID.put(playerUUID, network);
        if (teamUUID != null) {
            networksByTeamUUID.put(teamUUID, network);
        }
        LueTech.savedData.setDirty();
        return network;
    }

    @Nullable
    public BeaconNetwork getNetwork(@NotNull UUID networkUUID) {
        return networksByNetworkUUID.get(networkUUID);
    }

    @Nullable
    public BeaconNetwork getNetworkForPlayer(@NotNull UUID playerUUID) {
        return networksByPlayerUUID.get(playerUUID);
    }

    @Nullable
    public BeaconNetwork getNetworkForTeam(@NotNull UUID teamUUID) {
        return networksByTeamUUID.get(teamUUID);
    }

    @NotNull
    public Collection<BeaconNetwork> allNetworks() {
        return networksByNetworkUUID.values();
    }

    @ApiStatus.Internal
    public static final Codec<BeaconSavedData> CODEC = BeaconNetwork.CODEC.listOf()
            .xmap(BeaconSavedData::new, b -> b.networksByNetworkUUID.values().stream().toList());
}
