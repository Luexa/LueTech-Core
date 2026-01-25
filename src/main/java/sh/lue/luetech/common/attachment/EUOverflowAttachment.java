package sh.lue.luetech.common.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public class EUOverflowAttachment implements INBTSerializable<Tag> {
    public long eu = 0L;

    @Override
    public Tag serializeNBT(@NotNull HolderLookup.Provider provider) {
        if (eu > 0L) {
            return LongTag.valueOf(eu);
        }
        return null;
    }

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider provider, @NotNull Tag tag) {
        if (tag instanceof LongTag longTag) {
            eu = longTag.getAsLong();
        }
    }
}
