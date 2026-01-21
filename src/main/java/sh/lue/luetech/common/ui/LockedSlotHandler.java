package sh.lue.luetech.common.ui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public record LockedSlotHandler(Inventory inventory) implements IItemHandlerModifiable {
    @Override
    public void setStackInSlot(int i, @NotNull ItemStack itemStack) {
    }

    @Override
    public int getSlots() {
        return inventory.getContainerSize();
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int i) {
        return inventory.getItem(i);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int i, @NotNull ItemStack itemStack, boolean b) {
        return itemStack;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int i, int i1, boolean b) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int i) {
        return 0;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack itemStack) {
        return false;
    }
}
