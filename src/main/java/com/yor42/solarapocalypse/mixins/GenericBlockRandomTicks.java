package com.yor42.solarapocalypse.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class GenericBlockRandomTicks {

    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void onIsRandomlyTicking(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == Blocks.CLAY || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS_PATH || state.getBlock() == Blocks.COARSE_DIRT || state.getBlock() instanceof PumpkinBlock) cir.setReturnValue(true);
    }
}