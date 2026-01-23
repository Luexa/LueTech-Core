package sh.lue.luetech.mixins;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import sh.lue.luetech.LueTech;

import java.util.List;
import java.util.Set;

public class LTMixinPlugin implements IMixinConfigPlugin {
    private static final String MIXIN_PACKAGE = "sh.lue.luetech.mixins.";
    private static final String CLIENT_MIXIN_PACKAGE = MIXIN_PACKAGE + "client.";

    private boolean inSubpackage(String subpackage, String className) {
        return className.startsWith(MIXIN_PACKAGE + subpackage) || className.startsWith(CLIENT_MIXIN_PACKAGE + subpackage);
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (inSubpackage("neoforge", mixinClassName)) {
            return true;
        }
        if (inSubpackage("gtceu.Jade", mixinClassName)) {
            return isModLoaded("gtceu") && isModLoaded("jade");
        }
        if (inSubpackage("gtceu.KJS", mixinClassName)) {
            return isModLoaded("gtceu") && isModLoaded("kubejs");
        }
        if (inSubpackage("gtceu", mixinClassName)) {
            return isModLoaded("gtceu");
        }
        if (inSubpackage("jdt.JEI", mixinClassName)) {
            return isModLoaded("justdirethings") && isModLoaded("jei");
        }
        if (inSubpackage("jdt", mixinClassName)) {
            return isModLoaded("justdirethings");
        }
        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    private static boolean isModLoaded(String modId) {
        var modList = ModList.get();
        if (modList == null) {
            return LoadingModList.get().getModFileById(modId) != null;
        }
        return modList.isLoaded(modId);
    }
}
