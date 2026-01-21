package sh.lue.luetech.data.tags;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import sh.lue.luetech.data.LTBlocks;

public class BlockTagLoader {
    public static void init(RegistrateTagsProvider<Block> provider) {
        provider.addTag(BlockTags.MINEABLE_WITH_SHOVEL)
                .addOptional(LTBlocks.TIMEWIND_GOO.getId());
        provider.addTag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addOptional(LTBlocks.ELEMENTIUM_ORE.getId());
        provider.addTag(Tags.Blocks.ORES)
                .addOptional(LTBlocks.ELEMENTIUM_ORE.getId());
    }
}
