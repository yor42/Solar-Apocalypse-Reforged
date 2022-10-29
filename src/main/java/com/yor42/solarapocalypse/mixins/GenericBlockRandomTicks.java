package com.yor42.solarapocalypse.mixins;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class GenericBlockRandomTicks {

    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void onisRandomlyTicking(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == Blocks.CLAY || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.DIRT_PATH || state.getBlock() == Blocks.COARSE_DIRT || state.getBlock() instanceof PumpkinBlock) cir.setReturnValue(true);
    }
}