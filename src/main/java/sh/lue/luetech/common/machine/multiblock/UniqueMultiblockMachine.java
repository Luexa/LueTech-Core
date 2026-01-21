package sh.lue.luetech.common.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.saveddata.LTSavedData;
import sh.lue.luetech.utils.TeamUtils;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class UniqueMultiblockMachine extends WorkableElectricMultiblockMachine
        implements IMachineLife {
    protected static final Map<UUID, UniqueMultiblockMachine> ALL_INSTANCES = new Object2ObjectOpenHashMap<>();

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            UniqueMultiblockMachine.class,
            WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    protected UUID machineUUID;

    @Persisted
    @Nullable
    protected UUID teamUUID;

    protected boolean allowedToRun = false;

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public UniqueMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onMachineRemoved() {
        if (getLevel() instanceof ServerLevel) {
            var savedData = LueTech.savedData;
            UUID ownerUUID = getOwnerUUID();
            ResourceLocation multiblockID = getDefinition().getId();
            savedData.attemptUniqueDeactivation(multiblockID, machineUUID, ownerUUID, teamUUID);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel) {
            if (machineUUID == null) {
                machineUUID = UUID.randomUUID();
            }
            ALL_INSTANCES.put(machineUUID, this);
            var savedData = LueTech.savedData;
            var multiblockType = getDefinition().getId();
            var newTeamUUID = TeamUtils.getInstance().getTeamUUID(getOwnerUUID());
            var ownerUUID = getOwnerUUID();
            if (!Objects.equals(teamUUID, newTeamUUID)) {
                savedData.modifyTeamDelegate(multiblockType, machineUUID, ownerUUID, teamUUID, newTeamUUID);
                teamUUID = newTeamUUID;
            }
            allowedToRun = savedData.attemptUniqueActivation(multiblockType, machineUUID, ownerUUID, teamUUID);
            if (!allowedToRun) {
                getRecipeLogic().setStatus(RecipeLogic.Status.SUSPEND);
            }
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (getLevel() instanceof ServerLevel) {
            ALL_INSTANCES.remove(machineUUID);
        }
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        if (machineUUID != null && !allowedToRun && getLevel() instanceof ServerLevel) {
            var multiblockType = getDefinition().getId();
            var savedData = LueTech.savedData;
            var toDeactivate = savedData.activateNewMachine(multiblockType, machineUUID, getOwnerUUID(), teamUUID);
            for (var deactivateUUID : toDeactivate) {
                var deactivateMachine = ALL_INSTANCES.get(deactivateUUID);
                if (deactivateMachine != null) {
                    deactivateMachine.allowedToRun = false;
                    deactivateMachine.getRecipeLogic().setStatus(RecipeLogic.Status.SUSPEND);
                    deactivateMachine.onUpdateStatus();
                }
            }
            allowedToRun = true;
            onUpdateStatus();
        }
        super.setWorkingEnabled(allowedToRun && isWorkingAllowed);
    }

    @ApiStatus.Internal
    public static void handleTeamChange(@NotNull LTSavedData savedData, @NotNull UUID ownerUUID,
                                        @Nullable UUID oldTeamUUID, @Nullable UUID newTeamUUID) {
        var affectedMachines = savedData.reassignDelegate(ownerUUID, oldTeamUUID, newTeamUUID);
        for (var machineUUID : affectedMachines) {
            var machine = ALL_INSTANCES.get(machineUUID);
            if (machine == null) continue;
            var multiblockType = machine.getDefinition().getId();
            machine.teamUUID = newTeamUUID;
            machine.allowedToRun = savedData.attemptUniqueActivation(multiblockType, machineUUID, ownerUUID, newTeamUUID);
            if (!machine.allowedToRun) {
                machine.getRecipeLogic().setStatus(RecipeLogic.Status.SUSPEND);
            }
            machine.onUpdateStatus();
        }
    }

    protected void onUpdateStatus() {}
}
