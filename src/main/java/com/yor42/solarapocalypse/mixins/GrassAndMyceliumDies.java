package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.MathUtils;
import com.yor42.solarapocalypse.SolApocalypseConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpreadableSnowyDirtBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpreadableSnowyDirtBlock.class)
public abstract class GrassAndMyceliumDies {

    @Inject(method = "canBeGrass", at = @At("RETURN"), cancellable = true)
    private static void onCanBeGrass(BlockState state, IWorldReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = pos.above();
        World realWorld = (World) world;
        if (!MathUtils.isWorldOldEnough(realWorld, MathUtils.STAGE.STAGE_1) || realWorld.isNight() || !realWorld.canSeeSky(blockPos)) return;
        cir.setReturnValue(false);
    }

}
