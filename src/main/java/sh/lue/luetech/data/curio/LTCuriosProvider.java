package sh.lue.luetech.data.curio;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sh.lue.luetech.LueTech;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class LTCuriosProvider extends CuriosDataProvider {
    public LTCuriosProvider(PackOutput output, ExistingFileHelper fileHelper,
                            CompletableFuture<HolderLookup.Provider> registries) {
        super(LueTech.MOD_ID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createSlot("battery_pack")
                .renderToggle(false);
        this.createEntities("entities")
                .addPlayer()
                .addSlots("battery_pack");
    }
}
