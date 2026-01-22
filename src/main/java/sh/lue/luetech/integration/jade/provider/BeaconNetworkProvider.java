package sh.lue.luetech.integration.jade.provider;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.jade.GTJadePlugin;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
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

import java.math.BigInteger;

public enum BeaconNetworkProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    public static final ResourceLocation UID = LueTech.id("beacon_network_info");
    private static final String storedPowerKey = "luetech:beacon_stored_power";
    private static final String maxPowerKey = "luetech:beacon_max_power";
    private static final BigInteger THRESHOLD = BigInteger.valueOf(1000000000000L);

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        MetaMachineBlockEntity machineBlockEntity = (MetaMachineBlockEntity) accessor.getBlockEntity();
        if (machineBlockEntity.getMetaMachine() instanceof IBeaconConnected machine) {
            var network = machine.getConnectedBeaconNetwork();
            if (network != null) {
                var storedPower = network.getStoredPower();
                var maxPower = network.getMaxPower();
                tag.putByteArray("luetech:beacon_stored_power", storedPower.toByteArray());
                tag.putByteArray("luetech:beacon_max_power", maxPower.toByteArray());
            }
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        var serverData = accessor.getServerData();
        if (!serverData.contains(storedPowerKey, CompoundTag.TAG_BYTE_ARRAY)) return;
        if (!serverData.contains(maxPowerKey, CompoundTag.TAG_BYTE_ARRAY)) return;
        var storedPowerBytes = serverData.getByteArray(storedPowerKey);
        var maxPowerBytes = serverData.getByteArray(maxPowerKey);
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
        tooltip.add(Component.translatable("luetech.jade.beacon_network").withStyle(ChatFormatting.AQUA));
        tooltip.add(
                helper.progress(
                        progress,
                        Component.translatable("gtceu.jade.energy_stored", storedPowerStr, maxPowerStr),
                        helper.progressStyle().color(0xFFEEE600, 0xFFEEE600).textColor(-1),
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
