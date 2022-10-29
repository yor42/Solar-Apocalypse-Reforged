package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.utils.MathUtils;
import com.yor42.solarapocalypse.SolApocalypseConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(BlockBehaviour.class)
public class BlockModifiedInSunlight {

    @Mutable
    @Final
    @Shadow
    protected boolean isRandomlyTicking;
    @Final
    @Shadow
    protected Material material;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(BlockBehaviour.Properties properties, CallbackInfo ci) {
        if (material.isFlammable()) isRandomlyTicking = true;
    }

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void onRandomTick(BlockState state, ServerLevel world, BlockPos pos, Random random, CallbackInfo ci){
        BlockPos blockPos = pos.above();
        if (world.isNight() || world.isRaining() || !world.canSeeSky(blockPos)) return;
        if (MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_2)) {
            if (state.getMaterial().isFlammable() && world.getBlockState(blockPos).isAir()) {
                BlockState blockState = BaseFireBlock.getState(world, blockPos);
                world.setBlock(blockPos, blockState, 2 | 8);
            }
            if (state.getBlock() == Blocks.CLAY) {
                world.setBlock(pos, Blocks.TERRACOTTA.defaultBlockState(), 2);
            } else if (state.getBlock() == Blocks.DIRT) {
                world.setBlock(pos, Blocks.COARSE_DIRT.defaultBlockState(), 2);
            } else if (state.getBlock() == Blocks.FARMLAND) {
                FarmBlock.turnToDirt(state, world, pos);
            } else if (state.getBlock() instanceof PumpkinBlock) {
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            }
        }
        if (state.getBlock() == Blocks.COARSE_DIRT && SolApocalypseConfig.CONFIG.STAGE_3_5.get()>=0.0) {
            if (MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_3_5)) {
                world.setBlock(pos, Blocks.SAND.defaultBlockState(),2);
            }
        }
    }

}
