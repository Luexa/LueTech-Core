package sh.lue.luetech.common.machine;

import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.common.saveddata.beacon.BeaconNetwork;

public interface IBeaconConnected {
    @Nullable
    BeaconNetwork getConnectedBeaconNetwork();
}
