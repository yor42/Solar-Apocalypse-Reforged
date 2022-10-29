package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(TallGrassBlock.class)
public abstract class PlantBlockDies extends Block {
    public PlantBlockDies(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        BlockPos blockPos = pos.above();
        if (!MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_2) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos)) return;
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
    }
}
