package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LeavesBlock.class)
public class LeaveDecay {
    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void onisRandomlyTicking(BlockState state, CallbackInfoReturnable<Boolean> cir) { cir.setReturnValue(true); }

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void hookRandomTick(BlockState state, ServerLevel world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockPos blockPos = pos.above();
        if (!MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_2) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos) || !state.getMaterial().isFlammable()) return;
        world.removeBlock(pos, false);
    }
}
