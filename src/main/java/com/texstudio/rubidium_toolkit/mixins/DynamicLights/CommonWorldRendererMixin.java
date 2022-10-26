package com.texstudio.rubidium_toolkit.mixins.DynamicLights;

import com.texstudio.rubidium_toolkit.features.DynamicLights.DynamicLightsFeature;
import com.texstudio.rubidium_toolkit.features.DynamicLights.accessor.WorldRendererAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WorldRenderer.class, priority = 900)
public abstract class CommonWorldRendererMixin implements WorldRendererAccessor
{
    @Invoker("setSectionDirty")
    @Override
    public abstract void dynlights_setSectionDirty(int x, int y, int z, boolean important);



    @Inject(
            method = "getLightColor(Lnet/minecraft/world/IBlockDisplayReader;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At("TAIL"),
            cancellable = true
    )
    private static void onGetLightmapCoordinates(IBlockDisplayReader world, BlockState j, BlockPos pos, CallbackInfoReturnable<Integer> cir)
    {
        if (!world.getBlockState(pos).isSolidRender(world, pos) && DynamicLightsFeature.isEnabled())
        {
            int vanilla = cir.getReturnValue();
            int value = DynamicLightsFeature.getLightmapWithDynamicLight(pos, vanilla);

            cir.setReturnValue(value);
        }
    }
}