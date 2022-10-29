package com.yor42.solarapocalypse.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.yor42.solarapocalypse.Constants;
import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
@OnlyIn(Dist.CLIENT)
public class WorldRendererMixin {

    @Shadow
    @Final
    @Mutable
    private static ResourceLocation SUN_LOCATION;

    @Inject(method="renderSky", at = @At("HEAD"))
    private void onRendersky(PoseStack p_202424_, Matrix4f p_202425_, float p_202426_, Camera p_202427_, boolean p_202428_, Runnable p_202429_, CallbackInfo ci){
        if(MathUtils.LEVEL_STAGE != null){
            SUN_LOCATION = MathUtils.LEVEL_STAGE.getSuntexture();
        }
    }
}
