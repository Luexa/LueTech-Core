package sh.lue.luetech.integration.jdt;

import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.setup.Registration;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import sh.lue.luetech.common.attachment.EUOverflowAttachment;
import sh.lue.luetech.data.LTAttachments;

public class JDTNativeEUContainer implements IEnergyContainer {
    private final EUOverflowAttachment overflow;
    private final WrappedStorage wrappedStorage;

    public JDTNativeEUContainer(@NotNull BlockEntity blockEntity) {
        wrappedStorage = this.new WrappedStorage(blockEntity.getData(Registration.ENERGYSTORAGE_MACHINES));
        overflow = blockEntity.getData(LTAttachments.EU_OVERFLOW);
    }

    @Override
    public long acceptEnergyFromNetwork(Direction side, long voltage, long amperage) {
        wrappedStorage.refill();
        if (overflow.eu > 0L || voltage <= 0L || amperage <= 0L) {
            return 0L;
        }
        long insertedEU = FeCompat.insertEu(wrappedStorage.wrapped, voltage * amperage, false);
        long insertedAmps = Math.ceilDiv(insertedEU, voltage);
        overflow.eu = insertedAmps * voltage - insertedEU;
        return insertedAmps;
    }

    @Override
    public boolean inputsEnergy(Direction side) {
        return true;
    }

    @Override
    public long changeEnergy(long differenceAmount) {
        return 0L;
    }

    @Override
    public long getEnergyStored() {
        return overflow.eu;
    }

    @Override
    public long getEnergyCapacity() {
        return GTValues.V[GTValues.MAX];
    }

    @Override
    public long getInputAmperage() {
        return 1L;
    }

    @Override
    public long getInputVoltage() {
        return GTValues.V[GTValues.MAX];
    }

    @Override
    public boolean isOneProbeHidden() {
        return true;
    }

    public MachineEnergyStorage getWrappedStorage() {
        return wrappedStorage;
    }

    private class WrappedStorage extends MachineEnergyStorage {
        private final MachineEnergyStorage wrapped;

        private WrappedStorage(MachineEnergyStorage wrapped) {
            super(wrapped.getMaxEnergyStored());
            this.wrapped = wrapped;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            int energy = wrapped.getEnergyStored();
            int extracted = Math.clamp(toExtract, 0, energy);
            if (!simulate) {
                wrapped.setEnergy(energy - extracted);
                refill();
            }
            return extracted;
        }

        @Override
        public void setEnergy(int energy) {
            wrapped.setEnergy(energy);
            overflow.eu = 0L;
        }

        @Override
        public int getEnergyStored() {
            return wrapped.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            return wrapped.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return wrapped.canExtract();
        }

        @Override
        public boolean canReceive() {
            return wrapped.canReceive();
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            return wrapped.receiveEnergy(toReceive, simulate);
        }

        private void refill() {
            long ratio = FeCompat.ratio(false);
            long energy = wrapped.getEnergyStored();
            long capacity = wrapped.getMaxEnergyStored();
            long toRefillFE = capacity - energy;
            long toRefillEU = Math.ceilDiv(toRefillFE, ratio);
            long availableEU = Math.min(toRefillEU, overflow.eu);
            overflow.eu -= availableEU;
            energy = Math.max(0L, Math.min(capacity, energy + availableEU * ratio));
            wrapped.setEnergy((int)energy);
        }
    }
}
