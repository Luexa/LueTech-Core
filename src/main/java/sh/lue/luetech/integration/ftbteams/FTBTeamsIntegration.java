package sh.lue.luetech.integration.ftbteams;

import dev.ftb.mods.ftbteams.api.event.PlayerChangedTeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import org.jetbrains.annotations.NotNull;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.multiblock.UniqueMultiblockMachine;

import static sh.lue.luetech.LueTech.savedData;

public class FTBTeamsIntegration {
    public static void init() {
        TeamEvent.PLAYER_CHANGED.register(FTBTeamsIntegration::onPlayerChangeTeam);
    }

    private static void onPlayerChangeTeam(@NotNull PlayerChangedTeamEvent event) {
        var playerUUID = event.getPlayerId();
        var newTeam = event.getTeam();
        var oldTeam = event.getPreviousTeam().orElse(null);
        var newTeamUUID = newTeam.isPlayerTeam() ? null : newTeam.getTeamId();
        var oldTeamUUID = oldTeam == null || oldTeam.isPlayerTeam() ? null : oldTeam.getTeamId();

        var beaconNetwork = savedData.dominanceBeacon.getNetworkForPlayer(playerUUID);
        if (beaconNetwork != null) {
            var teamBeaconNetwork = newTeamUUID == null ? null : savedData.dominanceBeacon.getNetworkForTeam(newTeamUUID);
            if (teamBeaconNetwork == null) {
                savedData.dominanceBeacon.setNetworkTeam(beaconNetwork, newTeamUUID);
            } else {
                savedData.dominanceBeacon.setNetworkTeam(beaconNetwork, null);
                beaconNetwork.setActive(false);
            }
        }

        UniqueMultiblockMachine.handleTeamChange(savedData, playerUUID, oldTeamUUID, newTeamUUID);
    }
}
