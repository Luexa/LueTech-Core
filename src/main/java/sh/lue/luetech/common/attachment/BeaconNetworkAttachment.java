package sh.lue.luetech.common.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

public class BeaconNetworkAttachment implements INBTSerializable<Tag> {
    @Nullable
    public UUID network;

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider provider, @NotNull Tag tag) {
        if (tag instanceof IntArrayTag intArrayTag && intArrayTag.size() == 4) {
            network = UUIDUtil.uuidFromIntArray(intArrayTag.getAsIntArray());
        }
    }

    @Override
    @Nullable
    public Tag serializeNBT(@NotNull HolderLookup.Provider provider) {
        return network == null ? null : new IntArrayTag(UUIDUtil.uuidToIntArray(network));
    }
}
