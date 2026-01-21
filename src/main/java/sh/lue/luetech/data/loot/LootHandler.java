package sh.lue.luetech.data.loot;

import com.gregtechceu.gtceu.api.material.ChemicalHelper;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sh.lue.luetech.data.LTBlocks;
import sh.lue.luetech.data.LTMaterials;

public class LootHandler {
    public static void init(RegistrateLootTableProvider provider) {
        provider.addLootAction(RegistrateLootTableProvider.LootType.BLOCK, tables -> {
            tables.add(LTBlocks.ELEMENTIUM_ORE.get(), tables.createSilkTouchDispatchTable(
                    LTBlocks.ELEMENTIUM_ORE.get(),
                    tables.applyExplosionDecay(
                            LTBlocks.ELEMENTIUM_ORE.get(),
                            LootItem.lootTableItem(ChemicalHelper.get(TagPrefix.rawOre, LTMaterials.Elementium).getItem())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                    )
            ));
        });
    }
}
