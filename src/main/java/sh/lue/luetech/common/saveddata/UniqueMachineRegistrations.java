package sh.lue.luetech.common.saveddata;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

class UniqueMachineRegistrations {
    final Map<UUID, Map<ResourceLocation, UUID>> playerActivatedMachines;
    final Map<UUID, Map<ResourceLocation, UUID>> teamPlayerDelegates;

    UniqueMachineRegistrations() {
        this(new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>());
    }

    private UniqueMachineRegistrations(@NotNull Map<UUID, Map<ResourceLocation, UUID>> playerActivatedMachines,
                                       @NotNull Map<UUID, Map<ResourceLocation, UUID>> teamPlayerDelegates) {
        this.playerActivatedMachines = playerActivatedMachines;
        this.teamPlayerDelegates = teamPlayerDelegates;
    }

    private static final Codec<Map<UUID, Map<ResourceLocation, UUID>>> ACTIVATED_MACHINES_CODEC = Codec.pair(
            UUIDUtil.CODEC.fieldOf("owner").codec(),
            Codec.unboundedMap(ResourceLocation.CODEC, UUIDUtil.CODEC)
                    .<Map<ResourceLocation, UUID>>xmap(Object2ObjectOpenHashMap::new, Function.identity())
                    .fieldOf("machines").codec()
    ).listOf().xmap(
            list -> {
                Map<UUID, Map<ResourceLocation, UUID>> map = new Object2ObjectOpenHashMap<>();
                for (var entry : list) {
                    map.put(entry.getFirst(), entry.getSecond());
                }
                return map;
            },
            map -> map.entrySet().stream()
                    .filter(entry -> !entry.getValue().isEmpty())
                    .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                    .toList()
    ).orElseGet(Object2ObjectOpenHashMap::new);

    static final MapCodec<UniqueMachineRegistrations> CODEC = RecordCodecBuilder.<UniqueMachineRegistrations>mapCodec(instance -> instance.group(
            ACTIVATED_MACHINES_CODEC.fieldOf("player")
                    .forGetter(sd -> sd.playerActivatedMachines),
            ACTIVATED_MACHINES_CODEC.fieldOf("team")
                    .forGetter(sd -> sd.teamPlayerDelegates)
    ).apply(instance, UniqueMachineRegistrations::new)).orElseGet(UniqueMachineRegistrations::new);
}
