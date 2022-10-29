package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.SolApocalypseConfig;
import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.*;
import net.minecraftforge.fluids.IFluidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = WaterFluid.class)
public abstract class NoInfiniteWaterAndWaterEvaporates extends Fluid {
    @Inject(method = "canConvertToSource", at = @At("HEAD"), cancellable = true)
    private void isWaterInfinite(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(!SolApocalypseConfig.CONFIG.IsWaterfinite.get());
    }

    @Override
    protected boolean isRandomlyTicking() {
        return SolApocalypseConfig.CONFIG.IsWaterfinite.get();
    }

    @Override
    protected void randomTick(Level world, BlockPos pos, FluidState state, Random random) {
        BlockPos UpPos = pos.above();
        if(!MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_2) || !SolApocalypseConfig.CONFIG.IsWaterfinite.get() || state.getType() != Fluids.WATER || world.isNight() || world.isRaining() || !world.canSeeSky(UpPos)){
            return;
        }
        BlockState blockState = world.getBlockState(pos);
        Material material = blockState.getMaterial();
        if (blockState.getBlock() instanceof BucketPickup) {
            ((BucketPickup)blockState.getBlock()).pickupBlock(world, pos, blockState);
        } else if (blockState.getBlock() instanceof IFluidBlock) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
            BlockEntity TE = blockState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
            Block.dropResources(blockState, world, pos, TE);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
}
