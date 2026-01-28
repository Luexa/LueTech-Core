package sh.lue.luetech.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.item.behavior.BatteryPackBehavior.WirelessChargerConnectionComponent;

public class LTDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
            .createDataComponents(Registries.DATA_COMPONENT_TYPE, LueTech.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> BATTERY_PACK_ACTIVE =
            DATA_COMPONENTS.registerComponentType("battery_pack_active",
                    builder -> builder.persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> BATTERY_PACK_CONTENTS =
            DATA_COMPONENTS.registerComponentType("battery_pack_contents",
                    builder -> builder.persistent(ItemContainerContents.CODEC)
                            .networkSynchronized(ItemContainerContents.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WirelessChargerConnectionComponent>> WIRELESS_CHARGER_CONNECTION =
            DATA_COMPONENTS.registerComponentType("wireless_charger_connection",
                    builder -> builder.persistent(WirelessChargerConnectionComponent.CODEC)
                            .networkSynchronized(WirelessChargerConnectionComponent.STREAM_CODEC));
}
