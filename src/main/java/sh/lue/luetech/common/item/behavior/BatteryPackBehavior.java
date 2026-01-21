package sh.lue.luetech.common.item.behavior;

import appeng.api.ids.AEComponents;
import appeng.core.definitions.AEItems;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IElectricItem;
import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.ToggleButtonWidget;
import com.gregtechceu.gtceu.api.item.component.*;
import com.gregtechceu.gtceu.api.item.component.forge.IComponentCapability;
import com.gregtechceu.gtceu.client.renderer.item.ToolChargeBarRenderer;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler;
import org.jetbrains.annotations.NotNull;
import sh.lue.luetech.common.machine.electric.ChargeTransmitterMachine;
import sh.lue.luetech.common.ui.EnhancedSlotWidget;
import sh.lue.luetech.common.ui.LockedSlotHandler;
import sh.lue.luetech.data.LTDataComponents;
import sh.lue.luetech.data.LTItems;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.math.BigInteger;
import java.util.List;
import java.util.OptionalLong;

public record BatteryPackBehavior(
        Variant variant
) implements IInteractionItem, IItemLifeCycle, IAddInformation, IItemUIFactory, IComponentCapability,
        IItemDecoratorComponent {
    @Override
    public ModularUI createUI(HeldItemUIFactory.HeldItemHolder holder, Player player) {
        final Inventory inventory = player.getInventory();
        final int lockedPlayerSlot = holder.hand == InteractionHand.OFF_HAND ? 40 : inventory.selected;
        final ItemStack stack = holder.getHeld();
        NonNullList<ItemStack> stackContents = NonNullList.withSize(variant.slotCount, ItemStack.EMPTY);
        stack.getOrDefault(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.EMPTY)
                .copyInto(stackContents);
        ContentsHandler handler = new ContentsHandler(stack, stackContents, variant);

        ModularUI ui = new ModularUI(176, 157, holder, player)
                .background(GuiTextures.BACKGROUND)
                .widget(new LabelWidget(5, 5, stack.getDescriptionId()))
                .widget(new ToggleButtonWidget(7, 60, 10, 10,
                        () -> isActive(stack),
                        active -> setActive(stack, active)))
                .widget(new LabelWidget(20, 60, Component.translatable(
                        "luetech.ui.battery_pack_status." + (isActive(stack) ? "enabled" : "disabled"))) {
                    @Override
                    public void detectAndSendChanges() {
                        this.component = Component.translatable(
                                "luetech.ui.battery_pack_status." +
                                        (BatteryPackBehavior.isActive(stack) ? "enabled" : "disabled"));
                        super.detectAndSendChanges();
                    }
                });

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int slot = col + (row + 1) * 9;
                ui.widget(new EnhancedSlotWidget(inventory, slot, col * 18 + 7, row * 18 + 75, lockedPlayerSlot)
                        .setBackgroundTexture(GuiTextures.SLOT)
                        .setLocationInfo(true, false));
            }
        }

        for (int slot = 0; slot < 9; ++slot) {
            boolean isLocked = slot == lockedPlayerSlot;
            var widget = isLocked ?
                    new EnhancedSlotWidget(new LockedSlotHandler(inventory), slot, slot * 18 + 7, 133, false, false) :
                    new EnhancedSlotWidget(inventory, slot, slot * 18 + 7, 133, lockedPlayerSlot);
            ui.widget(widget
                    .setBackgroundTexture(GuiTextures.SLOT)
                    .setLocationInfo(true, true));
        }

        if (variant.slotCount == 4) {
            for (int row = 0; row < 2; ++row) {
                for (int col = 0; col < 2; ++col) {
                    int slot = col + row * 2;
                    ui.widget(new EnhancedSlotWidget(handler, slot, col * 18 + 70, row * 18 + 20, lockedPlayerSlot)
                            .setBackgroundTexture(GuiTextures.SLOT));
                }
            }
        } else if (variant.slotCount == 1) {
            ui.widget(new EnhancedSlotWidget(handler, 0, 79, 29, lockedPlayerSlot)
                    .setBackgroundTexture(GuiTextures.SLOT));
        }

        return ui;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (variant == Variant.BATTERY_PACK) {
                if (isActive(stack)) {
                    tickBatteryPack(stack, player);
                }
            } else if (variant == Variant.WIRELESS_CHARGER) {
                var transmitter = tickTransmitterStatus(stack);
                if (transmitter != null && isActive(stack) && transmitter.isWorkingEnabled()) {
                    tickWirelessChargerPack(transmitter, player);
                }
            }
        }
    }

    private static void tickBatteryPack(ItemStack stack, Player player) {
        NonNullList<ItemStack> contents = NonNullList.withSize(Variant.BATTERY_PACK.slotCount, ItemStack.EMPTY);
        NonNullList<ItemStack> contentsCopy = NonNullList.createWithCapacity(Variant.BATTERY_PACK.slotCount);
        stack.getOrDefault(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.EMPTY)
                .copyInto(contents);
        for (var innerStack : contents) {
            contentsCopy.add(innerStack.copy());
        }
        var contentsHandler = new ContentsHandler(stack, contentsCopy, Variant.BATTERY_PACK);

        List<IElectricItem> electricItems = new ObjectArrayList<>(Variant.BATTERY_PACK.slotCount);
        LongList transferLimits = new LongArrayList(Variant.BATTERY_PACK.slotCount);
        BooleanList callContentsChanged = new BooleanArrayList(Variant.BATTERY_PACK.slotCount);
        {
            boolean foundUsefulElectricItem = false;
            for (int i = 0; i < Variant.BATTERY_PACK.slotCount; ++i) {
                var innerStack = contents.get(i);
                var electricItem = GTCapabilityHelper.getElectricItem(innerStack);
                if (electricItem != null && electricItem.getCharge() > 0) {
                    transferLimits.add(Math.min(electricItem.getCharge(), electricItem.getTransferLimit()));
                    foundUsefulElectricItem = true;
                } else {
                    transferLimits.add(0);
                }
                electricItems.add(electricItem);
                callContentsChanged.add(false);
            }
            if (!foundUsefulElectricItem) return;
        }

        var curios = CuriosApi.getCuriosInventory(player)
                .<IItemHandler>map(ICuriosItemHandler::getEquippedCurios)
                .orElse(EmptyItemHandler.INSTANCE);
        for (int i = 0; i < curios.getSlots(); ++i) {
            var itemInSlot = curios.getStackInSlot(i);
            dischargeFromBatteries(electricItems, transferLimits, callContentsChanged, itemInSlot);
        }

        var playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getContainerSize(); ++i) {
            var itemInSlot = playerInventory.getItem(i);
            dischargeFromBatteries(electricItems, transferLimits, callContentsChanged, itemInSlot);
        }

        for (int slot = 0; slot < callContentsChanged.size(); ++slot) {
            if (callContentsChanged.getBoolean(slot)) {
                contentsHandler.setStackInSlot(slot, contents.get(slot));
            }
        }
    }

    private static void dischargeFromBatteries(List<IElectricItem> sources,
                                               LongList transferLimits, BooleanList callContentsChanged,
                                               ItemStack target) {
        var targetElectricItem = GTCapabilityHelper.getElectricItem(target);
        int drainTierMin = 0;
        if (targetElectricItem != null && !targetElectricItem.canProvideChargeExternally()) {
            drainTierMin = targetElectricItem.getTier();
            for (int i = 0; i < sources.size(); ++i) {
                var source = sources.get(i);
                if (source != null && source.getTier() >= drainTierMin) {
                    long availableFromThisSource = Math.min(
                            transferLimits.getLong(i),
                            targetElectricItem.getTransferLimit());
                    long toDrain = targetElectricItem.charge(availableFromThisSource, drainTierMin, false, false);
                    if (toDrain == 0) continue;
                    transferLimits.set(i, transferLimits.getLong(i) - toDrain);
                    source.discharge(toDrain, source.getTier(), false, true, false);
                    callContentsChanged.set(i, true);
                }
            }
        } else if (ConfigHolder.INSTANCE.compat.energy.nativeEUToFE) {
            var feEnergyItem = GTCapabilityHelper.getForgeEnergyItem(target);
            if (feEnergyItem != null && feEnergyItem.canReceive() &&
                    feEnergyItem.getEnergyStored() < feEnergyItem.getMaxEnergyStored()) {
                for (int i = 0; i < sources.size(); ++i) {
                    var source = sources.get(i);
                    if (source != null) {
                        long availableFromThisSource = transferLimits.getLong(i);
                        long toDrain = FeCompat.insertEu(feEnergyItem, availableFromThisSource, false);
                        if (toDrain == 0) continue;
                        transferLimits.set(i, availableFromThisSource - toDrain);
                        source.discharge(toDrain, source.getTier(), false, true, false);
                        callContentsChanged.set(i, true);
                    }
                }
            }
        }
    }

    private static ChargeTransmitterMachine tickTransmitterStatus(ItemStack stack) {
        var frequencyOpt = getFrequency(stack);
        if (frequencyOpt.isEmpty()) {
            stack.remove(LTDataComponents.WIRELESS_CHARGER_CONNECTION);
            return null;
        }
        long frequency = frequencyOpt.getAsLong();
        var transmitter = ChargeTransmitterMachine.getFromFrequency(frequency);
        if (transmitter == null) {
            stack.remove(LTDataComponents.WIRELESS_CHARGER_CONNECTION);
            return null;
        }
        stack.set(LTDataComponents.WIRELESS_CHARGER_CONNECTION, new WirelessChargerConnectionComponent(
                transmitter.isWorkingEnabled(),
                transmitter.getTier()));
        return transmitter;
    }

    private static void tickWirelessChargerPack(ChargeTransmitterMachine transmitter, Player player) {
        int energyTier = transmitter.getTier();
        long availableEnergy = Math.min(transmitter.energyContainer.getEnergyStored(),
                GTValues.V[transmitter.getTier()] * ChargeTransmitterMachine.AMPS);
        long remainingEnergy = availableEnergy;

        var curios = CuriosApi.getCuriosInventory(player)
                .<IItemHandler>map(ICuriosItemHandler::getEquippedCurios)
                .orElse(EmptyItemHandler.INSTANCE);
        for (int i = 0; i < curios.getSlots(); ++i) {
            var itemInSlot = curios.getStackInSlot(i);
            remainingEnergy -= chargeFromEnergy(remainingEnergy, energyTier, itemInSlot);
        }

        var playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getContainerSize(); ++i) {
            var itemInSlot = playerInventory.getItem(i);
            remainingEnergy -= chargeFromEnergy(remainingEnergy, energyTier, itemInSlot);
        }

        transmitter.energyContainer.removeEnergy(availableEnergy - remainingEnergy);
    }

    private static long chargeFromEnergy(long availableEnergy, int energyTier, ItemStack target) {
        if (availableEnergy <= 0) return 0L;
        var targetElectricItem = GTCapabilityHelper.getElectricItem(target);
        if (targetElectricItem != null && !targetElectricItem.canProvideChargeExternally()) {
            int targetTier = targetElectricItem.getTier();
            if (targetTier > energyTier) return 0L;
            long availableToTarget = Math.min(availableEnergy,
                    ChargeTransmitterMachine.AMPS * targetElectricItem.getTransferLimit());
            return targetElectricItem.charge(availableToTarget, targetTier, true, false);
        }
        if (ConfigHolder.INSTANCE.compat.energy.nativeEUToFE) {
            var feEnergyItem = GTCapabilityHelper.getForgeEnergyItem(target);
            if (feEnergyItem != null && feEnergyItem.canReceive() &&
                feEnergyItem.getEnergyStored() < feEnergyItem.getMaxEnergyStored()) {
                return FeCompat.insertEu(feEnergyItem, availableEnergy, false);
            }
        }
        return 0L;
    }

    private static OptionalLong getFrequency(ItemStack stack) {
        var contents = stack.getOrDefault(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.EMPTY);
        if (contents.getSlots() == 0) return OptionalLong.empty();
        var frequency = contents.getStackInSlot(0).get(AEComponents.ENTANGLED_SINGULARITY_ID);
        return frequency == null ? OptionalLong.empty() : OptionalLong.of(frequency);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack item, Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        return IItemUIFactory.super.use(item, level, player, hand);
    }

    @Override
    public void attachCapabilities(RegisterCapabilitiesEvent event, Item item) {
        event.registerItem(CuriosCapability.ITEM, (stack, unused) -> new Curio(stack), item);
    }

    private static void setActive(ItemStack stack, boolean active) {
        stack.set(LTDataComponents.BATTERY_PACK_ACTIVE, active);
    }

    private static boolean isActive(ItemStack stack) {
        return !stack.isEmpty() && stack.getOrDefault(LTDataComponents.BATTERY_PACK_ACTIVE, false);
    }

    @Override
    public boolean render(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull ItemStack itemStack,
                          int xOffset, int yOffset) {
        if (itemStack.is(LTItems.BATTERY_PACK)) {
            NonNullList<ItemStack> contents = NonNullList.withSize(Variant.BATTERY_PACK.slotCount, ItemStack.EMPTY);
            itemStack.getOrDefault(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.EMPTY)
                    .copyInto(contents);
            double maxCharge = 0.0d;
            double currentCharge = 0.0d;
            for (var batteryStack : contents) {
                var electricItem = GTCapabilityHelper.getElectricItem(batteryStack);
                if (electricItem == null) continue;
                maxCharge += (double)electricItem.getMaxCharge();
                currentCharge += (double)electricItem.getCharge();
            }
            long renderCharge = (long)(1000000000d * (currentCharge / maxCharge));
            return ToolChargeBarRenderer.renderElectricBar(guiGraphics, renderCharge,
                    1000000000L, xOffset, yOffset, itemStack.isBarVisible());
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltips,
                                TooltipFlag isAdvanced) {
        if (stack.is(LTItems.BATTERY_PACK)) {
            NonNullList<ItemStack> contents = NonNullList.withSize(Variant.BATTERY_PACK.slotCount, ItemStack.EMPTY);
            stack.getOrDefault(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.EMPTY)
                    .copyInto(contents);
            BigInteger maxCharge = BigInteger.ZERO;
            BigInteger currentCharge = BigInteger.ZERO;
            int maxTier = 0;
            boolean foundBatteries = false;
            for (var batteryStack : contents) {
                var electricItem = GTCapabilityHelper.getElectricItem(batteryStack);
                if (electricItem == null) continue;
                maxCharge = maxCharge.add(BigInteger.valueOf(electricItem.getMaxCharge()));
                currentCharge = currentCharge.add(BigInteger.valueOf(electricItem.getCharge()));
                maxTier = Math.max(maxTier, electricItem.getTier());
                foundBatteries = true;
            }
            if (!foundBatteries) return;
            double chargePercentage = currentCharge.doubleValue() / maxCharge.doubleValue();
            ChatFormatting color = ChatFormatting.RED;
            if (chargePercentage > 0.5) {
                color = ChatFormatting.GREEN;
            } else if (chargePercentage > 0.3) {
                color = ChatFormatting.YELLOW;
            }
            tooltips.add(Component.translatable("metaitem.generic.electric_item.tooltip",
                    FormattingUtil.formatNumbers(currentCharge), FormattingUtil.formatNumbers(maxCharge),
                    GTValues.VNF[maxTier]).withStyle(color));
        } else if (stack.is(LTItems.WIRELESS_CHARGER_PACK)) {
            var contents = stack.getOrDefault(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.EMPTY);
            if (contents.getSlots() > 0 && !contents.getStackInSlot(0).isEmpty()) {
                var status = stack.get(LTDataComponents.WIRELESS_CHARGER_CONNECTION);
                if (status == null) {
                    tooltips.add(Component.translatable("luetech.ui.wireless_charger.remote_status",
                            Component.translatable("luetech.ui.wireless_charger.remote_status.disconnected")
                                    .withStyle(ChatFormatting.DARK_RED))
                            .withStyle(ChatFormatting.AQUA));
                } else {
                    String activeKey = "luetech.ui.wireless_charger.remote_status." +
                            (status.remoteActive ? "active" : "inactive");
                    var activeColor = status.remoteActive ? ChatFormatting.GREEN : ChatFormatting.YELLOW;
                    tooltips.add(Component.translatable("luetech.ui.wireless_charger.remote_status_tier",
                            Component.translatable(activeKey).withStyle(activeColor),
                            Component.literal(GTValues.VNF[Math.clamp(status.remoteTier, 0, GTValues.MAX)] +
                                    ChatFormatting.RESET))
                            .withStyle(ChatFormatting.AQUA));
                }
            }
        }
        if (stack.getOrDefault(LTDataComponents.BATTERY_PACK_ACTIVE, false)) {
            tooltips.add(Component.translatable("luetech.ui.battery_pack_status.enabled")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            tooltips.add(Component.translatable("luetech.ui.battery_pack_status.disabled")
                    .withStyle(ChatFormatting.RED));
        }
    }

    private record Curio(@NotNull ItemStack stack) implements ICurio {
        @Override
        public ItemStack getStack() {
            return stack;
        }
    }

    private static class ContentsHandler extends ItemStackHandler {
        final ItemStack container;
        final Variant variant;

        public ContentsHandler(ItemStack container, NonNullList<ItemStack> stacks,
                               Variant variant) {
            super(stacks);
            this.container = container;
            this.variant = variant;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            container.getOrDefault(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.EMPTY)
                    .copyInto(stacks);
            this.validateSlotIndex(slot);
            return stacks.get(slot);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            var contents = ItemContainerContents.fromItems(stacks);
            container.set(LTDataComponents.BATTERY_PACK_CONTENTS, ItemContainerContents.fromItems(stacks));
            contents.copyInto(stacks);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return variant.isItemValid(stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    }

    public enum Variant {
        BATTERY_PACK(4),
        WIRELESS_CHARGER(1);

        public final int slotCount;

        Variant(int slotCount) {
            this.slotCount = slotCount;
        }

        public boolean isItemValid(@NotNull ItemStack stack) {
            switch (this) {
                case BATTERY_PACK -> {
                    var electricItem = GTCapabilityHelper.getElectricItem(stack);
                    return electricItem != null && electricItem.canProvideChargeExternally();
                }
                case WIRELESS_CHARGER -> {
                    return stack.is(AEItems.QUANTUM_ENTANGLED_SINGULARITY.get());
                }
            }
            return false;
        }
    }

    public record WirelessChargerConnectionComponent(boolean remoteActive, int remoteTier) {
        public static final Codec<WirelessChargerConnectionComponent> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.BOOL.orElse(false).fieldOf("active")
                                .forGetter(WirelessChargerConnectionComponent::remoteActive),
                        Codec.INT.orElse(0).fieldOf("tier")
                                .forGetter(WirelessChargerConnectionComponent::remoteTier))
                        .apply(instance, WirelessChargerConnectionComponent::new));
        public static final StreamCodec<ByteBuf, WirelessChargerConnectionComponent> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.BOOL, WirelessChargerConnectionComponent::remoteActive,
                        ByteBufCodecs.INT, WirelessChargerConnectionComponent::remoteTier,
                        WirelessChargerConnectionComponent::new);
    }
}
