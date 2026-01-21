package sh.lue.luetech.common.saveddata.beacon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.multiblock.part.EldritchEnergyHatchPartMachine;
import sh.lue.luetech.utils.BigIntegerUtils;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BeaconNetwork {
    @NotNull
    UUID uuid;

    @NotNull
    UUID playerUUID;

    @Nullable
    UUID teamUUID;

    @NotNull
    BigInteger storedPower;

    @NotNull
    BigInteger maxPower;

    boolean active;

    BeaconNetwork(@NotNull UUID uuid, @NotNull UUID playerUUID, @Nullable UUID teamUUID,
                  @NotNull BigInteger storedPower, @NotNull BigInteger maxPower, boolean active) {
        if (maxPower.compareTo(BigInteger.ZERO) < 0) {
            maxPower = BigInteger.ZERO;
        }

        if (storedPower.compareTo(BigInteger.ZERO) < 0) {
            storedPower = BigInteger.ZERO;
        } else if (storedPower.compareTo(maxPower) > 0) {
            storedPower = maxPower;
        }

        this.uuid = uuid;
        this.playerUUID = playerUUID;
        this.teamUUID = teamUUID;
        this.storedPower = storedPower;
        this.maxPower = maxPower;
        this.active = active;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Blame codecs
    private BeaconNetwork(@NotNull UUID uuid, @NotNull UUID playerUUID, @NotNull Optional<UUID> teamUUID,
                          @NotNull BigInteger storedPower, @NotNull BigInteger maxPower, boolean active) {
        this(uuid, playerUUID, teamUUID.orElse(null), storedPower, maxPower, active);
    }

    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    @NotNull
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Nullable
    public UUID getTeamUUID() {
        return teamUUID;
    }

    @NotNull
    public BigInteger getStoredPower() {
        return storedPower;
    }

    @NotNull
    public BigInteger getMaxPower() {
        return maxPower;
    }

    public boolean getActive() {
        return active;
    }

    public void setPlayerUUID(@NotNull UUID playerUUID) {
        if (!Objects.equals(playerUUID, this.playerUUID)) {
            this.playerUUID = playerUUID;
            LueTech.savedData.setDirty();
        }
    }

    public void setTeamUUID(@Nullable UUID teamUUID) {
        if (!Objects.equals(teamUUID, this.teamUUID)) {
            this.teamUUID = teamUUID;
            LueTech.savedData.setDirty();
        }
    }

    public void setStoredPower(@NotNull BigInteger storedPower) {
        if (!Objects.equals(storedPower, this.storedPower)) {
            this.storedPower = storedPower;
            LueTech.savedData.setDirty();
        }
    }

    public void setMaxPower(@NotNull BigInteger maxPower) {
        if (!Objects.equals(maxPower, this.maxPower)) {
            this.maxPower = maxPower;
            LueTech.savedData.setDirty();
        }
    }

    public void setActive(boolean active) {
        if (active != this.active) {
            this.active = active;
            LueTech.savedData.setDirty();
            var networkHatches = EldritchEnergyHatchPartMachine.NETWORK_MEMBERS.get(uuid);
            if (networkHatches != null) {
                for (var hatch : networkHatches) {
                    hatch.onNetworkActiveChange();
                }
            }
        }
    }

    static final Codec<BeaconNetwork> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(n -> n.uuid),
            UUIDUtil.CODEC.fieldOf("player").forGetter(n -> n.playerUUID),
            UUIDUtil.CODEC.optionalFieldOf("team")
                    .forGetter(n -> Optional.ofNullable(n.teamUUID)),
            BigIntegerUtils.CODEC.optionalFieldOf("stored_power", BigInteger.ZERO)
                    .forGetter(n -> n.storedPower),
            BigIntegerUtils.CODEC.optionalFieldOf("max_power", BigInteger.ZERO)
                    .forGetter(n -> n.maxPower),
            Codec.BOOL.optionalFieldOf("active", false)
                    .forGetter(n -> n.active)
    ).apply(instance, BeaconNetwork::new));
}
