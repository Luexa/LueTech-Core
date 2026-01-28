package sh.lue.luetech.common.registry;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import sh.lue.luetech.LueTech;

public class LTRegistration {
    public static final GTRegistrate REGISTRATE = GTRegistrate.create(LueTech.MOD_ID);

    private LTRegistration() {}
}
