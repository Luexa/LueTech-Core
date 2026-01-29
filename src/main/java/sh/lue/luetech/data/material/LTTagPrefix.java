package sh.lue.luetech.data.material;

import com.gregtechceu.gtceu.api.material.material.info.MaterialIconType;
import com.gregtechceu.gtceu.api.tag.TagPrefix;

import static sh.lue.luetech.data.material.LTMaterialFlags.*;

public class LTTagPrefix {
    public static final TagPrefix crystalSeed = new TagPrefix("crystalSeed")
            .idPattern("%s_crystal_seed").defaultTagPath("crystal_seeds/%s").unformattedTagPath("crystal_seeds")
            .langValue("%s Crystal Seed").unificationEnabled(true).generateItem(true)
            .materialIconType(MaterialIconType.gem)
            .generationCondition(m -> m.hasFlag(GENERATE_CRYSTAL_SEED));
}
