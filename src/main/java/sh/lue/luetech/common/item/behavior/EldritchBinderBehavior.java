package sh.lue.luetech.common.item.behavior;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.machine.electric.TransformerMachine;
import com.gregtechceu.gtceu.common.machine.owner.MachineOwner;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.IBeaconConnected;
import sh.lue.luetech.utils.TeamUtils;

public class EldritchBinderBehavior implements IInteractionItem {
    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof MetaMachineBlockEntity blockEntity) {
            var machine = blockEntity.getMetaMachine();
            var player = context.getPlayer();
            var level = context.getLevel();
            if (player == null || !MachineOwner.canOpenOwnerMachine(context.getPlayer(), machine)) {
                if (!level.isClientSide && player != null) {
                    player.displayClientMessage(Component.translatable("luetech.item.eldritch_binder.error_permission")
                            .withStyle(ChatFormatting.RED), true);
                }
                return InteractionResult.FAIL;
            }
            if (!(machine instanceof TieredEnergyMachine) || (machine instanceof IMultiController ||
                    machine instanceof IMultiPart || machine instanceof TransformerMachine ||
                    machine instanceof SimpleGeneratorMachine)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable("luetech.item.eldritch_binder.error_machine_type")
                            .withStyle(ChatFormatting.RED), true);
                }
                return InteractionResult.FAIL;
            }
            if (level instanceof ServerLevel) {
                var beaconConnected = (IBeaconConnected)machine;
                if (beaconConnected.isBeaconConnected()) {
                    beaconConnected.setBeaconNetwork(null);
                    player.displayClientMessage(Component.translatable("luetech.item.eldritch_binder.success_unlinked")
                            .withStyle(ChatFormatting.GREEN), true);
                } else {
                    var playerUUID = player.getUUID();
                    var teamUUID = TeamUtils.getInstance().getTeamUUID(playerUUID);
                    var network = teamUUID != null ? LueTech.savedData.dominanceBeacon.getNetworkForTeam(teamUUID) :
                            LueTech.savedData.dominanceBeacon.getNetworkForPlayer(playerUUID);
                    if (network == null) {
                        player.displayClientMessage(Component.translatable("luetech.item.eldritch_binder.error_no_network")
                                .withStyle(ChatFormatting.RED), true);
                    } else {
                        beaconConnected.setBeaconNetwork(network.getUUID());
                        player.displayClientMessage(Component.translatable("luetech.item.eldritch_binder.success")
                                .withStyle(ChatFormatting.GREEN), true);
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
