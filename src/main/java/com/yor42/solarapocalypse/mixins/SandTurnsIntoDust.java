package com.yor42.solarapocalypse.mixins;


import com.yor42.solarapocalypse.gameobjects.GameRegister;
import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(SandBlock.class)
public class SandTurnsIntoDust extends Block {
    public SandTurnsIntoDust(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        BlockPos blockPos = pos.above();
        if (!MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_2) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos)) return;
        world.setBlock(pos, GameRegister.DUSTBLOCK_REGISTRY.get().defaultBlockState(),2);
    }

}
