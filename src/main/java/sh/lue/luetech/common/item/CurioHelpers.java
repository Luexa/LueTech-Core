package sh.lue.luetech.common.item;

import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CurioHelpers {
    public static boolean equipCurioFromHand(ItemStack stack, Player player, InteractionHand hand) {
        var curio = CuriosApi.getCurio(stack).orElse(null);
        if (curio == null) return false;
        var handler = CuriosApi.getCuriosInventory(player).orElse(null);
        if (handler == null) return false;
        var curios = handler.getCurios();
        Tuple<IDynamicStackHandler, SlotContext> firstSlot = null;
        for (var entry : curios.entrySet()) {
            var stackHandler = entry.getValue().getStacks();
            var activeStates = entry.getValue().getActiveStates();
            for (int i = 0; i < stackHandler.getSlots(); ++i) {
                boolean active = activeStates.size() > i && activeStates.get(i);
                if (!active) continue;
                String id = entry.getKey();
                var renderStates = entry.getValue().getRenders();
                var slotContext = new SlotContext(id, player, i, false,
                        renderStates.size() > i && renderStates.get(i));
                if (stackHandler.isItemValid(i, stack)) {
                    ItemStack present = stackHandler.getStackInSlot(i);
                    if (present.isEmpty()) {
                        stackHandler.setStackInSlot(i, stack.copy());
                        curio.onEquipFromUse(slotContext);
                        if (!player.isCreative()) {
                            int count = stack.getCount();
                            stack.shrink(count);
                        }
                        return true;
                    } else if (firstSlot == null) {
                        if (stackHandler.extractItem(i, stack.getMaxStackSize(), true).getCount()
                                == stack.getCount()) {
                            firstSlot = new Tuple<>(stackHandler, slotContext);
                        }
                    }
                }
            }
        }
        if (firstSlot != null) {
            var stackHandler = firstSlot.getA();
            var slotContext = firstSlot.getB();
            int i = slotContext.index();
            ItemStack present = stackHandler.getStackInSlot(i);
            stackHandler.setStackInSlot(i, stack.copy());
            curio.onEquipFromUse(slotContext);
            player.setItemInHand(hand, present.copy());
            return true;
        }
        return false;
    }
}
