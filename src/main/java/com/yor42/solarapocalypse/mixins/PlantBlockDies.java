package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.MathUtils;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(TallGrassBlock.class)
public abstract class PlantBlockDies extends Block {
    public PlantBlockDies(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public boolean isRandomlyTicking(BlockState p_149653_1_) {
        return true;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        BlockPos blockPos = pos.above();
        if (!MathUtils.isWorldOldEnough(world, MathUtils.STAGE.STAGE_2) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos)) return;
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
    }
}
