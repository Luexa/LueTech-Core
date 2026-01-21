package sh.lue.luetech.utils;

import com.google.common.base.Predicates;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LTCompat;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class TeamUtils {
    private TeamUtils() {}

    private static TeamUtils INSTANCE;
    private static final Predicate<Team> NOT_PLAYER_TEAM = Predicates.not(Team::isPlayerTeam);

    @NotNull
    public static TeamUtils getInstance() {
        if (INSTANCE == null) {
            if (LTCompat.FTBTEAMS_LOADED) {
                INSTANCE = new TeamsImpl();
            } else {
                INSTANCE = new NoImpl();
            }
        }
        return INSTANCE;
    }

    @Nullable
    public abstract UUID getTeamUUID(@Nullable UUID playerUUID);

    public boolean isInTeam(@Nullable UUID playerUUID, @Nullable UUID teamUUID) {
        return Objects.equals(teamUUID, getTeamUUID(playerUUID));
    }

    private static class NoImpl extends TeamUtils {
        @Nullable
        @Override
        public UUID getTeamUUID(@Nullable UUID playerUUID) {
            return null;
        }
    }

    private static class TeamsImpl extends TeamUtils {
        @Nullable
        @Override
        public UUID getTeamUUID(@Nullable UUID playerUUID) {
            var api = FTBTeamsAPI.api();
            if (api.isManagerLoaded()) {
                return api.getManager().getTeamForPlayerID(playerUUID)
                        .filter(NOT_PLAYER_TEAM)
                        .map(Team::getTeamId)
                        .orElse(null);
            } else {
                return null;
            }
        }
    }
}
