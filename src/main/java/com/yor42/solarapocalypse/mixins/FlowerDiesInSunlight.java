package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(BushBlock.class)
public abstract class FlowerDiesInSunlight extends Block {

    public FlowerDiesInSunlight(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        BlockPos blockPos = pos.above();
        if (!MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_2) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos) || state.getBlock() == Blocks.DEAD_BUSH) return;
        world.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(),2);
    }
}
