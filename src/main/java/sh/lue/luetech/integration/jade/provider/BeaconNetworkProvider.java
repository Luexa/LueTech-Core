package sh.lue.luetech.integration.jade.provider;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.machine.IBeaconConnected;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.Tooltip;

import java.math.BigInteger;

public enum BeaconNetworkProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    public static final ResourceLocation UID = LueTech.id("beacon_network_info");
    private static final String INACTIVE = "inactive";
    private static final String STORED_POWER = "stored_power";
    private static final String MAX_POWER = "max_power";
    private static final BigInteger THRESHOLD = BigInteger.valueOf(1000000000000L);
    private static final ResourceLocation ELECTRIC_CONTAINER_UID = GTCEu.id("electric_container_provider");
    private static final ResourceLocation MAINTENANCE_UID = GTCEu.id("maintenance_info");
    private static final ResourceLocation STRUCTURE_FORMED_UID = GTCEu.id("multiblock_structure");

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        MetaMachineBlockEntity machineBlockEntity = (MetaMachineBlockEntity) accessor.getBlockEntity();
        if (machineBlockEntity.getMetaMachine() instanceof IBeaconConnected machine) {
            var network = machine.getConnectedBeaconNetwork();
            if (network != null) {
                if (network.getActive()) {
                    var storedPower = network.getStoredPower();
                    var maxPower = network.getMaxPower();
                    tag.putByteArray(STORED_POWER, storedPower.toByteArray());
                    tag.putByteArray(MAX_POWER, maxPower.toByteArray());
                } else {
                    tag.putByte(INACTIVE, (byte) 0);
                }
            } else {
                tag.putByte(INACTIVE, (byte) 1);
            }
        }
    }

    @Override
    public void appendTooltip(ITooltip itooltip, BlockAccessor accessor, IPluginConfig config) {
        Tooltip tooltip = (Tooltip) itooltip;
        var serverData = accessor.getServerData();
        if (!serverData.contains(INACTIVE, Tag.TAG_BYTE) && !(serverData.contains(STORED_POWER, Tag.TAG_BYTE_ARRAY) && serverData.contains(MAX_POWER, Tag.TAG_BYTE_ARRAY))) {
            return;
        }
        int indexToAdd = 0;
        for (int i = 0; i < tooltip.lines.size(); ++i) {
            indexToAdd = i + 1;
            var elements = tooltip.lines.get(i).sortedElements();
            if (elements.isEmpty()) continue;
            var tag = elements.getFirst().getTag();
            if (tag == null) continue;
            if (tag.equals(ELECTRIC_CONTAINER_UID)) {
                break;
            }
            if (tag.equals(MAINTENANCE_UID) || tag.equals(STRUCTURE_FORMED_UID)) {
                indexToAdd -= 1;
                break;
            }
        }

        if (serverData.contains(INACTIVE, Tag.TAG_BYTE)) {
            byte inactiveType = serverData.getByte(INACTIVE);
            if (inactiveType == 0) {
                tooltip.add(indexToAdd, Component.translatable("luetech.jade.beacon_inactive").withStyle(ChatFormatting.YELLOW));
            } else {
                tooltip.add(indexToAdd, Component.translatable("luetech.jade.beacon_not_connected").withStyle(ChatFormatting.RED));
            }
            return;
        }
        var storedPowerBytes = serverData.getByteArray(STORED_POWER);
        var maxPowerBytes = serverData.getByteArray(MAX_POWER);
        if (storedPowerBytes.length > 20 || maxPowerBytes.length > 20) return;
        var storedPower = new BigInteger(storedPowerBytes);
        var maxPower = new BigInteger(maxPowerBytes);
        var maxPowerRelativeToZero = maxPower.compareTo(BigInteger.ZERO);
        if (maxPowerRelativeToZero < 0) return;
        float progress = maxPowerRelativeToZero == 0 ? 1.0f :
                (float)(storedPower.min(maxPower).doubleValue() / maxPower.doubleValue());

        var storedPowerStr = FormattingUtil.formatNumberOrSic(storedPower, THRESHOLD);
        var maxPowerStr = FormattingUtil.formatNumberOrSic(maxPower, THRESHOLD);
        var helper = IElementHelper.get();
        tooltip.add(indexToAdd,
                helper.progress(
                        progress,
                        Component.translatable("gtceu.jade.energy_stored", storedPowerStr, maxPowerStr),
                        helper.progressStyle().color(0xFFFAF8FF, 0xFFFAF8FF).textColor(-1),
                        Util.make(BoxStyle.GradientBorder.DEFAULT_VIEW_GROUP,
                                style -> style.borderColor = new int[] { 0xFF555555, 0xFF555555, 0xFF555555,
                                        0xFF555555 }),
                        true));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
