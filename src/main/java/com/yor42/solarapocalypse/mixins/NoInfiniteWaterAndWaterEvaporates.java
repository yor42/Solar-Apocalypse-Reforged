package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.utils.MathUtils;
import com.yor42.solarapocalypse.SolApocalypseConfig;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    protected void randomTick(World world, BlockPos pos, FluidState state, Random random) {
        BlockPos UpPos = pos.above();
        if(!MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_2) || !SolApocalypseConfig.CONFIG.IsWaterfinite.get() || state.getType() != Fluids.WATER || world.isNight() || world.isRaining() || !world.canSeeSky(UpPos)){
            return;
        }
        BlockState blockState = world.getBlockState(pos);
        Material material = blockState.getMaterial();
        if (blockState.getBlock() instanceof IBucketPickupHandler) {
            ((IBucketPickupHandler)blockState.getBlock()).takeLiquid(world, pos, blockState);
        } else if (blockState.getBlock() instanceof FlowingFluidBlock) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
            TileEntity TE = blockState.hasTileEntity() ? world.getBlockEntity(pos) : null;
            Block.dropResources(blockState, world, pos, TE);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
}
