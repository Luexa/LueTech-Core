package sh.lue.luetech.data;

import com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Base;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Item;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import sh.lue.luetech.client.blockentityrender.TimewindGooRender;
import sh.lue.luetech.common.block.ElementiumOreBlock;
import sh.lue.luetech.common.block.TimewindGooBlock;
import sh.lue.luetech.common.blockentity.TimewindGooBlockEntity;

import static sh.lue.luetech.common.registry.LTRegistration.REGISTRATE;

public class LTBlocks {
    /* JDT */
    static {
        REGISTRATE.creativeModeTab(LTCreativeModeTabs.DECORATION);
    }
    public static final BlockEntry<TimewindGooBlock> TIMEWIND_GOO = REGISTRATE.block("timewind_goo", properties -> new TimewindGooBlock())
            .blockstate((ctx, prov) -> {
                final String baseTexturePath = ctx.getId().getPath();
                prov.getVariantBuilder(ctx.getEntry()).forAllStates(blockState -> {
                    boolean alive = blockState.getValue(GooBlock_Base.ALIVE);
                    String texturePath = alive ? baseTexturePath : baseTexturePath + "_dead";
                    return ConfiguredModel.builder()
                            .modelFile(prov.models().cubeAll(texturePath, prov.modLoc("block/" + texturePath)))
                            .build();
                });
            })
            .item(GooBlock_Item::new)
            .model((ctx, prov) -> {
                var path = ctx.getId().getPath();
                var deadLoc = prov.modLoc("block/" + path + "_dead");
                prov.withExistingParent(path, deadLoc);
            })
            .build()
            .register();
    public static final BlockEntityEntry<TimewindGooBlockEntity> TIMEWIND_GOO_ENTITY = REGISTRATE.<TimewindGooBlockEntity>blockEntity("timewind_goo", (type, pos, state) -> new TimewindGooBlockEntity(pos, state))
            .validBlock(TIMEWIND_GOO)
            .renderer(() -> TimewindGooRender::new)
            .register();

    static {
        REGISTRATE.creativeModeTab(LTCreativeModeTabs.MATERIAL_BLOCK);
    }
    public static final BlockEntry<ElementiumOreBlock> ELEMENTIUM_ORE = REGISTRATE.block("elementium_ore", properties -> new ElementiumOreBlock())
            .lang("Raw Elementium Ore")
            .blockstate(NonNullBiConsumer.noop())
            .item(BlockItem::new)
            .build()
            .register();

    public static void init() {
    }
}
