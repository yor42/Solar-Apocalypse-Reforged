package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.MathUtils;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BushBlock.class)
public abstract class FlowerDiesInSunlight extends Block {

    public FlowerDiesInSunlight(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public boolean isRandomlyTicking(BlockState p_149653_1_) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random p_225542_4_) {
        super.randomTick(state, world, pos, p_225542_4_);
        BlockPos blockPos = pos.above();
        if (!MathUtils.isWorldOldEnough(world, MathUtils.STAGE.STAGE_2) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos) || state.getBlock() == Blocks.DEAD_BUSH) return;
        world.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(),2);
    }
}
