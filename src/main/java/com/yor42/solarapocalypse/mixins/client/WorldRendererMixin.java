package com.yor42.solarapocalypse.mixins.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.solarapocalypse.Constants;
import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
@OnlyIn(Dist.CLIENT)
public class WorldRendererMixin {

    @Shadow
    @Final
    @Mutable
    private static ResourceLocation SUN_LOCATION;

    @Inject(method="renderSky", at = @At("HEAD"))
    private void onRendersky(MatrixStack p_228424_1_, float p_228424_2_, CallbackInfo ci){
        if(MathUtils.LEVEL_STAGE != null){
            SUN_LOCATION = MathUtils.LEVEL_STAGE.getSuntexture();
        }
    }
}
