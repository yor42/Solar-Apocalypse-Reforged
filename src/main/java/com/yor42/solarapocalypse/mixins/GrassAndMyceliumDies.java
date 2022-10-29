package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpreadingSnowyDirtBlock.class)
public abstract class GrassAndMyceliumDies {

    @Inject(method = "canBeGrass", at = @At("RETURN"), cancellable = true)
    private static void onCanBeGrass(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = pos.above();
        Level realWorld = (Level) world;
        if (MathUtils.shouldExcuteStage(realWorld, MathUtils.STAGE.STAGE_1) && !realWorld.isNight() && realWorld.canSeeSky(blockPos)) {
            cir.setReturnValue(false);
        }
    }

}
