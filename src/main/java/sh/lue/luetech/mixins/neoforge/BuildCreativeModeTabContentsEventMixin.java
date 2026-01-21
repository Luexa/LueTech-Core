package sh.lue.luetech.mixins.neoforge;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BuildCreativeModeTabContentsEvent.class, remap = false)
public abstract class BuildCreativeModeTabContentsEventMixin extends Event implements IModBusEvent, CreativeModeTab.Output {
    @WrapMethod(method = "accept")
    private void luetech$guardAccept(ItemStack newEntry, CreativeModeTab.TabVisibility visibility, Operation<Void> original) {
        try {
            original.call(newEntry, visibility);
        } catch (RuntimeException ignored) {}
    }
}
