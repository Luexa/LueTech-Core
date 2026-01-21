package sh.lue.luetech.common.blockentity;

import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sh.lue.luetech.data.LTBlocks;

public class TimewindGooBlockEntity extends GooBlockBE_Base {
    public TimewindGooBlockEntity(BlockPos pos, BlockState state) {
        super(LTBlocks.TIMEWIND_GOO_ENTITY.get(), pos, state);
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public int counterReducer() {
        return 20;
    }
}
