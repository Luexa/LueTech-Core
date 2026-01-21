package sh.lue.luetech.common.block;

import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseRawOre;
import net.minecraft.world.level.block.SoundType;

public class ElementiumOreBlock extends BaseRawOre {
    public ElementiumOreBlock() {
        super(Properties.of()
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .strength(5.0F, 6.0F)
        );
    }
}
