package sh.lue.luetech.common.saveddata;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.saveddata.beacon.BeaconSavedData;

import java.util.*;

public final class LTSavedData extends SavedData {
    @NotNull
    private final UniqueMachineRegistrations uniqueMachineRegistrations;

    @NotNull
    public final BeaconSavedData dominanceBeacon;

    @ApiStatus.Internal
    @NotNull
    public static LTSavedData getInstance(@NotNull ServerLevel serverLevel) {
        return serverLevel.getServer().overworld().getDataStorage()
                .computeIfAbsent(new Factory<>(LTSavedData::new, LTSavedData::load), "luetech");
    }

    public Map<UUID, Map<ResourceLocation, UUID>> getPlayerActivatedMachines() {
        return uniqueMachineRegistrations.playerActivatedMachines;
    }

    public Map<UUID, Map<ResourceLocation, UUID>> getTeamPlayerDelegates() {
        return uniqueMachineRegistrations.teamPlayerDelegates;
    }

    public boolean attemptUniqueActivation(@NotNull ResourceLocation multiblockType, @Nullable UUID machineUUID,
                                           @Nullable UUID ownerUUID, @Nullable UUID teamUUID) {
        if (ownerUUID == null || machineUUID == null) return false;
        var playerActivatedMachines = uniqueMachineRegistrations.playerActivatedMachines
                .computeIfAbsent(ownerUUID, k -> new Object2ObjectOpenHashMap<>());
        var existingActivation = playerActivatedMachines.get(multiblockType);
        if (teamUUID != null) {
            var teamPlayerDelegates = uniqueMachineRegistrations.teamPlayerDelegates
                    .computeIfAbsent(teamUUID, k -> new Object2ObjectOpenHashMap<>());
            UUID currentDelegate = teamPlayerDelegates.get(multiblockType);
            if (currentDelegate == null) {
                if (existingActivation == null) {
                    playerActivatedMachines.put(multiblockType, machineUUID);
                    teamPlayerDelegates.put(multiblockType, ownerUUID);
                    this.setDirty();
                    return true;
                } else if (existingActivation.equals(machineUUID)) {
                    teamPlayerDelegates.put(multiblockType, ownerUUID);
                    this.setDirty();
                    return true;
                }
                return false;
            } else if (currentDelegate.equals(ownerUUID)) {
                if (existingActivation == null) {
                    playerActivatedMachines.put(multiblockType, machineUUID);
                    this.setDirty();
                    return true;
                } else {
                    return existingActivation.equals(machineUUID);
                }
            } else {
                if (existingActivation != null && existingActivation.equals(machineUUID)) {
                    playerActivatedMachines.remove(multiblockType);
                    this.setDirty();
                }
                return false;
            }
        } else {
            if (existingActivation == null) {
                playerActivatedMachines.put(multiblockType, machineUUID);
                this.setDirty();
                return true;
            } else {
                return existingActivation.equals(machineUUID);
            }
        }
    }

    public void attemptUniqueDeactivation(@NotNull ResourceLocation multiblockType, @Nullable UUID machineUUID,
                                          @Nullable UUID ownerUUID, @Nullable UUID teamUUID) {
        if (ownerUUID == null || machineUUID == null) return;
        var playerActivatedMachines = uniqueMachineRegistrations.playerActivatedMachines.get(ownerUUID);
        if (playerActivatedMachines == null) return;
        playerActivatedMachines.computeIfPresent(multiblockType, (k, v) -> {
            if (v.equals(machineUUID)) {
                this.setDirty();
                return null;
            }
            return v;
        });
        var teamPlayerDelegates = teamUUID == null ? null : uniqueMachineRegistrations.teamPlayerDelegates
                .get(teamUUID);
        if (teamPlayerDelegates != null) {
            teamPlayerDelegates.computeIfPresent(multiblockType, (k, v) -> {
                if (v.equals(ownerUUID)) {
                    this.setDirty();
                    return null;
                }
                return v;
            });
        }
    }

    public void modifyTeamDelegate(@NotNull ResourceLocation multiblockType, @Nullable UUID machineUUID,
                                   @Nullable UUID ownerUUID, @Nullable UUID oldTeamUUID, @Nullable UUID newTeamUUID) {
        if (machineUUID == null || ownerUUID == null) return;
        var playerActivatedMachines = uniqueMachineRegistrations.playerActivatedMachines.get(ownerUUID);
        if (playerActivatedMachines == null || !Objects.equals(playerActivatedMachines.get(multiblockType), machineUUID)) return;
        var oldTeamPlayerDelegates = oldTeamUUID == null ? null :
                uniqueMachineRegistrations.teamPlayerDelegates.get(oldTeamUUID);
        if (oldTeamPlayerDelegates != null) {
            oldTeamPlayerDelegates.computeIfPresent(multiblockType, (k, v) -> {
                if (v.equals(ownerUUID)) {
                    this.setDirty();
                    return null;
                }
                return v;
            });
        }
        var newTeamPlayerDelegates = newTeamUUID == null ? null :
                uniqueMachineRegistrations.teamPlayerDelegates
                        .computeIfAbsent(newTeamUUID, k -> new Object2ObjectOpenHashMap<>());
        if (newTeamPlayerDelegates != null) {
            newTeamPlayerDelegates.compute(multiblockType, (k, v) -> {
                if (v == null) {
                    this.setDirty();
                    return ownerUUID;
                }
                return v;
            });
        }
    }

    /** Reassign a player from one team to another.
     * @return List of machines affected by this operation.
     */
    @NotNull
    public List<UUID> reassignDelegate(@NotNull UUID playerUUID, @Nullable UUID oldTeamUUID,
                                       @Nullable UUID newTeamUUID) {
        List<UUID> affectedMachines = new ObjectArrayList<>();
        var playerActivatedMachines = uniqueMachineRegistrations.playerActivatedMachines.get(playerUUID);
        if (playerActivatedMachines == null) return affectedMachines;
        affectedMachines.addAll(playerActivatedMachines.values());
        var oldTeamDelegates = oldTeamUUID == null ? null : uniqueMachineRegistrations.teamPlayerDelegates.get(oldTeamUUID);
        var newTeamDelegates = newTeamUUID == null ? null : uniqueMachineRegistrations.teamPlayerDelegates
                .computeIfAbsent(newTeamUUID, k -> new Object2ObjectOpenHashMap<>());
        if (oldTeamDelegates != null) {
            oldTeamDelegates.values().removeIf(uuid -> uuid.equals(playerUUID));
        }
        if (newTeamDelegates != null) {
            for (var entry : playerActivatedMachines.entrySet()) {
                var multiblockType = entry.getKey();
                var machineUUID = entry.getValue();
                newTeamDelegates.compute(multiblockType, (k, v) -> {
                    if (v == null) {
                        this.setDirty();
                        return playerUUID;
                    }
                    return v;
                });
            }
        }
        return affectedMachines;
    }

    /** Activate a new machine, potentially replacing a player or team's existing activation.
     * @return List of machine UUIDs to deactivate.
     */
    @NotNull
    public List<UUID> activateNewMachine(@NotNull ResourceLocation multiblockType, @Nullable UUID machineUUID,
                                         @Nullable UUID ownerUUID, @Nullable UUID teamUUID) {
        List<UUID> toDeactivate = new ObjectArrayList<>();
        if (machineUUID == null || ownerUUID == null) return toDeactivate;
        this.setDirty();
        var playerActivatedMachines = uniqueMachineRegistrations.playerActivatedMachines
                .computeIfAbsent(ownerUUID, k -> new Object2ObjectOpenHashMap<>());
        var previousPlayerMachineUUID = playerActivatedMachines.put(multiblockType, machineUUID);
        if (previousPlayerMachineUUID != null) {
            toDeactivate.add(previousPlayerMachineUUID);
        }
        var teamPlayerDelegates = teamUUID == null ? null : uniqueMachineRegistrations.teamPlayerDelegates.get(teamUUID);
        if (teamPlayerDelegates != null) {
            var previousDelegate = teamPlayerDelegates.put(multiblockType, ownerUUID);
            if (previousDelegate != null && !previousDelegate.equals(ownerUUID)) {
                var previousActivatedMachines = uniqueMachineRegistrations.playerActivatedMachines.get(previousDelegate);
                if (previousActivatedMachines != null) {
                    var previousTeamMachineUUID = previousActivatedMachines.get(multiblockType);
                    if (previousTeamMachineUUID != null) {
                        toDeactivate.add(previousTeamMachineUUID);
                    }
                }
            }
        }
        return toDeactivate;
    }

    private LTSavedData(@NotNull UniqueMachineRegistrations uniqueMachineRegistrations,
                        @NotNull BeaconSavedData dominanceBeacon) {
        this.uniqueMachineRegistrations = uniqueMachineRegistrations;
        this.dominanceBeacon = dominanceBeacon;
    }

    private LTSavedData() {
        this(new UniqueMachineRegistrations(), new BeaconSavedData());
        this.setDirty();
    }

    @NotNull
    private static LTSavedData load(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        return CODEC.decode(NbtOps.INSTANCE, tag)
                .resultOrPartial(LueTech.LOGGER::error)
                .map(Pair::getFirst)
                .orElseGet(LTSavedData::new);
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        var encoded = CODEC.encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(LueTech.LOGGER::error)
                .orElse(tag);
        return (CompoundTag)encoded;
    }

    private static final Codec<LTSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UniqueMachineRegistrations.CODEC.codec().optionalFieldOf("unique_machines")
                    .xmap(a -> a.orElseGet(UniqueMachineRegistrations::new), Optional::of)
                    .forGetter(sd -> sd.uniqueMachineRegistrations),
            BeaconSavedData.CODEC.optionalFieldOf("beacon_networks")
                    .xmap(a -> a.orElseGet(BeaconSavedData::new), Optional::of)
                    .forGetter(sd -> sd.dominanceBeacon)
    ).apply(instance, LTSavedData::new));
}
