package sh.lue.luetech.data;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sh.lue.luetech.LueTech;
import sh.lue.luetech.common.attachment.BeaconNetworkAttachment;
import sh.lue.luetech.common.attachment.EUOverflowAttachment;

import java.util.function.Supplier;

public class LTAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, LueTech.MOD_ID);

    public static final Supplier<AttachmentType<EUOverflowAttachment>> EU_OVERFLOW =
            ATTACHMENT_TYPES.register("eu_overflow",
                    () -> AttachmentType.serializable(EUOverflowAttachment::new).build());

    public static final Supplier<AttachmentType<BeaconNetworkAttachment>> BEACON_NETWORK =
            ATTACHMENT_TYPES.register("beacon_network",
                    () -> AttachmentType.serializable(BeaconNetworkAttachment::new).build());
}
