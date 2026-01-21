package sh.lue.luetech.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.lue.luetech.common.machine.multiblock.UniqueMultiblockMachine;
import sh.lue.luetech.common.saveddata.beacon.BeaconNetwork;

import java.util.List;

import static sh.lue.luetech.LueTech.savedData;

public class DominanceBeaconMachine extends UniqueMultiblockMachine {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            DominanceBeaconMachine.class,
            UniqueMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Nullable
    private BeaconNetwork network;

    public DominanceBeaconMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (getLevel() instanceof ServerLevel && allowedToRun) {
            loadNetwork();
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        if (getLevel() instanceof ServerLevel && network != null) {
            network.setActive(false);
        }
    }

    @Override
    protected void onUpdateStatus() {
        if (!allowedToRun) {
            network = null;
        } else {
            loadNetwork();
        }
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        super.setWorkingEnabled(isWorkingAllowed);
        if (allowedToRun && network != null) {
            network.setActive(isWorkingEnabled());
        }
    }

    @Override
    public void onMachineRemoved() {
        super.onMachineRemoved();
        if (getLevel() instanceof ServerLevel && network != null) {
            network.setActive(false);
        }
    }

    private void loadNetwork() {
        var playerUUID = getOwnerUUID();
        if (playerUUID == null) {
            network = null;
            return;
        }
        var teamNetwork = savedData.dominanceBeacon.teamNetwork(teamUUID);
        var playerNetwork = savedData.dominanceBeacon.playerNetwork(playerUUID);
        if (teamNetwork != null) {
            network = teamNetwork;
            if (playerNetwork != null && playerNetwork != teamNetwork) {
                savedData.dominanceBeacon.deleteNetwork(playerNetwork.getUUID());
            }
            savedData.dominanceBeacon.setNetworkPlayer(network, playerUUID);
            network.setActive(isFormed() && isWorkingEnabled());
            return;
        }
        if (playerNetwork != null) {
            network = playerNetwork;
            savedData.dominanceBeacon.setNetworkTeam(network, teamUUID);
        }
        if (network == null) {
            network = savedData.dominanceBeacon.createNetwork(playerUUID, teamUUID);
        }
        network.setActive(isFormed() && isWorkingEnabled());
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117)
                .setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150)
                        .clickHandler(this::handleDisplayClick)));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        if (isFormed()) {
            if (!isWorkingEnabled()) {
                if (allowedToRun) {
                    textList.add(Component.translatable("luetech.multiblock.beacon.network_disabled"));
                } else {
                    textList.add(Component.translatable("luetech.multiblock.unique_disabled"));
                }
            } else {
                if (network != null) {
                    textList.add(Component.translatable("luetech.multiblock.beacon.network_uuid",
                            network.getUUID().toString()));
                }
                textList.add(Component.translatable("luetech.multiblock.beacon.network_active"));
            }
        } else if (!allowedToRun) {
            textList.add(Component.translatable("luetech.multiblock.unique_disabled"));
        }
        getDefinition().getAdditionalDisplay().accept(this, textList);
    }
}
