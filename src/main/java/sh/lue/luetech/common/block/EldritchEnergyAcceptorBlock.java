package sh.lue.luetech.common.block;

import appeng.block.AEBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.blockentity.EldritchEnergyAcceptorBlockEntity;
import sh.lue.luetech.utils.TeamUtils;

public class EldritchEnergyAcceptorBlock extends Block implements EntityBlock {
    public EldritchEnergyAcceptorBlock() {
        super(AEBaseBlock.glassProps());
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EldritchEnergyAcceptorBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                            @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.getBlockEntity(pos) instanceof EldritchEnergyAcceptorBlockEntity blockEntity &&
                level instanceof ServerLevel && placer instanceof Player player) {
            var playerUUID = player.getUUID();
            var teamUUID = TeamUtils.getInstance().getTeamUUID(playerUUID);
            var network = teamUUID == null ?
                    LueTech.savedData.dominanceBeacon.getNetworkForPlayer(playerUUID) :
                    LueTech.savedData.dominanceBeacon.getNetworkForTeam(teamUUID);
            if (network != null) {
                blockEntity.setBeaconNetwork(network.getUUID());
            }
        }
    }
}
