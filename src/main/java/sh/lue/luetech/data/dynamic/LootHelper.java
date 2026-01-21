package sh.lue.luetech.data.dynamic;

import com.direwolf20.justdirethings.setup.Registration;
import com.gregtechceu.gtceu.api.material.ChemicalHelper;
import com.gregtechceu.gtceu.api.material.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import sh.lue.luetech.data.LTBlocks;

import java.util.List;

public class LootHelper {
    private static final ReferenceSet<Block> CUSTOM_ORES = new ReferenceOpenHashSet<>();
    static {
        CUSTOM_ORES.add(Registration.RawFerricoreOre.get());
        CUSTOM_ORES.add(Registration.RawBlazegoldOre.get());
        CUSTOM_ORES.add(Registration.RawCelestigemOre.get());
        CUSTOM_ORES.add(Registration.RawEclipseAlloyOre.get());
        CUSTOM_ORES.add(LTBlocks.ELEMENTIUM_ORE.get());
        CUSTOM_ORES.add(OccultismBlocks.IESNIUM_ORE.get());
        CUSTOM_ORES.add(OccultismBlocks.IESNIUM_ORE_NATURAL.get());
    }

    public static boolean isCustomOre(BlockState state) {
        return CUSTOM_ORES.contains(state.getBlock());
    }

    public static void applyCustomOreHammerDrops(List<ItemStack> drops) {
        if (drops.isEmpty()) return;
        ItemStack rawMaterial = drops.getFirst();
        if (rawMaterial.isEmpty()) return;
        MaterialEntry entry = ChemicalHelper.getMaterialEntry(rawMaterial.getItem());
        if (entry.isEmpty()) return;
        ItemStack crushed = ChemicalHelper.get(TagPrefix.crushed, entry.material(), rawMaterial.getCount());
        if (crushed.isEmpty()) return;
        drops.set(0, crushed);
    }
}
