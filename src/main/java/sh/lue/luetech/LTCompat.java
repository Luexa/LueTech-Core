package sh.lue.luetech;

import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Contract;

public class LTCompat {
    public static final boolean FTBTEAMS_LOADED = ModList.get().isLoaded("ftbteams");

    @Contract(pure = true)
    public static double aePerEu() {
        if (AE_PER_EU == -1) {
            AE_PER_EU = (double)FeCompat.ratio(false) / 2.0;
        }
        return AE_PER_EU;
    }

    private static double AE_PER_EU = -1;
}
