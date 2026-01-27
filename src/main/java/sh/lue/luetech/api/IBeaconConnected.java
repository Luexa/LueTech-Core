package sh.lue.luetech.api;

import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.common.saveddata.beacon.BeaconNetwork;

import java.util.UUID;

public interface IBeaconConnected {
    boolean isBeaconConnected();

    @Nullable
    BeaconNetwork getConnectedBeaconNetwork();

    void setBeaconNetwork(@Nullable UUID networkUUID);
}
