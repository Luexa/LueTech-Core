package sh.lue.luetech.mixins.client.gtceu;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.client.model.machine.MachineModel;
import com.gregtechceu.gtceu.client.model.machine.MachineRenderState;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = MachineModel.class, remap = false)
public class MachineModelMixin {
    @Shadow @Final private Map<MachineRenderState, BakedModel> modelsByState;
    @Shadow @Final private MachineDefinition definition;

    @Inject(method = "renderBaseModel", at = @At("HEAD"))
    private void luetech$fixInvisibleModel(List<BakedQuad> quads, @NotNull MachineRenderState renderState,
                                           @Nullable BlockState blockState, @Nullable Direction side, RandomSource rand,
                                           @NotNull ModelData modelData, @Nullable RenderType renderType, CallbackInfo ci,
                                           @Local(argsOnly = true) LocalRef<MachineRenderState> renderStateRef) {
        if (!modelsByState.containsKey(renderState)) {
            renderStateRef.set(definition.defaultRenderState());
        }
    }
}
