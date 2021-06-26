package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.MathUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassPathBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(GrassPathBlock.class)
public abstract class GrassPathTurnsIntoDirtInSunlight extends Block {

    public GrassPathTurnsIntoDirtInSunlight(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos blockPos = pos.above();
        if (!MathUtils.isWorldOldEnough(world, MathUtils.STAGE.STAGE_2) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos)) return;
        super.randomTick(state, world, pos, random);
    }

}
