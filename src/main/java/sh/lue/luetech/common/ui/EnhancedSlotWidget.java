package sh.lue.luetech.common.ui;

import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.Optional;

public class EnhancedSlotWidget extends SlotWidget {
    private final Integer lockedPlayerSlot;

    public EnhancedSlotWidget(Inventory inventory, int slot, int xPosition, int yPosition) {
        this(inventory, slot, xPosition, yPosition, true, true);
    }

    public EnhancedSlotWidget(Inventory inventory, int slot, int xPosition, int yPosition, boolean canTake, boolean canPut) {
        super(inventory, slot, xPosition, yPosition, canTake, canPut);
        lockedPlayerSlot = null;
    }

    public EnhancedSlotWidget(Inventory inventory, int slot, int xPosition, int yPosition, int lockedPlayerSlot) {
        this(inventory, slot, xPosition, yPosition, lockedPlayerSlot, true, true);
    }

    public EnhancedSlotWidget(Inventory inventory, int slot, int xPosition, int yPosition, int lockedPlayerSlot, boolean canTake, boolean canPut) {
        super(inventory, slot, xPosition, yPosition);
        this.lockedPlayerSlot = lockedPlayerSlot;
    }

    public EnhancedSlotWidget(IItemHandlerModifiable inventory, int slot, int xPosition, int yPosition) {
        this(inventory, slot, xPosition, yPosition, true, true);
    }

    public EnhancedSlotWidget(IItemHandlerModifiable inventory, int slot, int xPosition, int yPosition, boolean canTake, boolean canPut) {
        super(inventory, slot, xPosition, yPosition, canTake, canPut);
        lockedPlayerSlot = null;
    }

    public EnhancedSlotWidget(IItemHandlerModifiable inventory, int slot, int xPosition, int yPosition, int lockedPlayerSlot) {
        this(inventory, slot, xPosition, yPosition, lockedPlayerSlot, true, true);
    }

    public EnhancedSlotWidget(IItemHandlerModifiable inventory, int slot, int xPosition, int yPosition, int lockedPlayerSlot, boolean canTake, boolean canPut) {
        super(inventory, slot, xPosition, yPosition);
        this.lockedPlayerSlot = lockedPlayerSlot;
    }

    @Override
    public ItemStack slotClick(int button, ClickType clickTypeIn, Player player) {
        if (clickTypeIn == ClickType.SWAP && lockedPlayerSlot != null && button == lockedPlayerSlot) {
            return ItemStack.EMPTY;
        }
        return null;
    }
}
