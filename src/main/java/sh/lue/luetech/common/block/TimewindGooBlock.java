package sh.lue.luetech.common.block;

import com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Base;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.common.blockentity.TimewindGooBlockEntity;
import sh.lue.luetech.data.LTTags;

public class TimewindGooBlock extends GooBlock_Base implements EntityBlock {
    public TimewindGooBlock() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TimewindGooBlockEntity(pos, state);
    }

    @Override
    protected boolean validRevivalItem(ItemStack itemStack) {
        return itemStack.is(LTTags.REVIVE_TIMEWIND_GOO);
    }
}
