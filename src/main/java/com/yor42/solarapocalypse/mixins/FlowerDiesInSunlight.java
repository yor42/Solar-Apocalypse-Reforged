package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
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
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        BlockPos blockPos = pos.above();
        if (!MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_2) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos) || state.getBlock() == Blocks.DEAD_BUSH) return;
        world.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(),2);
    }
}
