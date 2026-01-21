package sh.lue.luetech.common.machine.electric;

import appeng.api.ids.AEComponents;
import appeng.core.definitions.AEItems;
import com.google.common.base.Predicates;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LTCompat;

import java.util.OptionalLong;
import java.util.Set;

public class ChargeTransmitterMachine extends TieredEnergyMachine
                                              implements IControllable, IFancyUIMachine, IMachineLife {
    public static final long AMPS = 4L;

    @NotNull
    private static final Long2ObjectMap<Set<ChargeTransmitterMachine>> MACHINES_BY_FREQUENCY =
            new Long2ObjectOpenHashMap<>();

    @NotNull
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ChargeTransmitterMachine.class,
            TieredEnergyMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private boolean isWorkingEnabled;

    @Persisted
    @NotNull
    protected final CustomItemStackHandler singularityInventory;

    private long lastKnownFrequency = 0L;

    public ChargeTransmitterMachine(@NotNull IMachineBlockEntity holder, int tier,
                                    Object @NotNull ... ignoredArgs) {
        super(holder, tier);
        this.isWorkingEnabled = true;
        this.singularityInventory = createSingularityInventory();
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean isWorkingEnabled() {
        return isWorkingEnabled;
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        isWorkingEnabled = workingEnabled;
    }

    @NotNull
    public CustomItemStackHandler getSingularityInventory() {
        return singularityInventory;
    }

    @NotNull
    public OptionalLong getFrequency() {
        ItemStack stack = singularityInventory.getStackInSlot(0);
        Long frequency = stack.get(AEComponents.ENTANGLED_SINGULARITY_ID);
        return frequency == null ? OptionalLong.empty() : OptionalLong.of(frequency);
    }

    @Nullable
    public static ChargeTransmitterMachine getFromFrequency(long frequency) {
        var machinesForFrequency = MACHINES_BY_FREQUENCY.get(frequency);
        if (machinesForFrequency != null) {
            return machinesForFrequency.iterator().next();
        }
        return null;
    }

    private void addToFrequency(long frequency) {
        var machinesForFrequency = MACHINES_BY_FREQUENCY.computeIfAbsent(frequency, ignored -> new ReferenceOpenHashSet<>());
        machinesForFrequency.add(this);
    }

    private void removeFromFrequency(long frequency) {
        var machinesForFrequency = MACHINES_BY_FREQUENCY.get(frequency);
        if (machinesForFrequency != null) {
            machinesForFrequency.remove(this);
            if (machinesForFrequency.isEmpty()) {
                MACHINES_BY_FREQUENCY.remove(frequency);
            }
        }
    }

    @Override
    @NotNull
    protected NotifiableEnergyContainer createEnergyContainer(Object @NotNull ... ignoredArgs) {
        long tierVoltage = GTValues.V[getTier()];
        return new NotifiableEnergyContainer(
                this, tierVoltage * 16L * AMPS,
                tierVoltage, AMPS,
                0L, 0L);
    }

    @NotNull
    private CustomItemStackHandler createSingularityInventory() {
        var handler = new CustomItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
        handler.setOnContentsChanged(() -> {
            if (isRemote()) return;
            var newFrequencyOpt = getFrequency();
            if (newFrequencyOpt.isEmpty()) {
                if (lastKnownFrequency != 0L) {
                    this.removeFromFrequency(lastKnownFrequency);
                    lastKnownFrequency = 0L;
                }
            } else {
                long newFrequency = newFrequencyOpt.getAsLong();
                if (newFrequency != lastKnownFrequency) {
                    if (newFrequency != 0L) {
                        this.addToFrequency(newFrequency);
                    }
                    if (lastKnownFrequency != 0L) {
                        this.removeFromFrequency(lastKnownFrequency);
                    }
                    lastKnownFrequency = newFrequency;
                }
            }
        });
        handler.setFilter(stack -> stack.is(AEItems.QUANTUM_ENTANGLED_SINGULARITY.asItem()));
        return handler;
    }

    @Override
    public Widget createUIWidget() {
        var template = new WidgetGroup(0, 0, 26, 26);
        template.setBackground(GuiTextures.BACKGROUND_INVERSE);
        template.addWidget(new SlotWidget(singularityInventory, 0, 4, 4)
                .setBackground(GuiTextures.SLOT));

        var editableUI = createEnergyBar();
        var energyBar = editableUI.createDefault();

        var group = new WidgetGroup(0, 0,
                Math.max(energyBar.getSizeWidth() + template.getSizeWidth() + 12, 172),
                Math.max(energyBar.getSizeHeight(), template.getSizeHeight() + 8));
        var size = group.getSize();
        energyBar.setSelfPosition(3, (size.height - energyBar.getSizeHeight()) / 2);
        template.setSelfPosition(
                (size.width - energyBar.getSizeWidth() - 4 - template.getSizeWidth()) / 2 + 2 +
                        energyBar.getSizeWidth() + 2,
                (size.height - template.getSizeHeight()) / 2);
        group.addWidget(energyBar);
        group.addWidget(template);
        editableUI.setupUI(group, this);
        return group;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (isRemote()) return;
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, () ->
                    getFrequency().ifPresent(frequency -> {
                        lastKnownFrequency = frequency;
                        this.addToFrequency(frequency);
                    })));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        this.removeFromFrequency(lastKnownFrequency);
        getFrequency().ifPresent(this::removeFromFrequency);
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(singularityInventory);
    }
}
