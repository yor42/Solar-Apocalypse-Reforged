package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.gameobjects.GameRegister;
import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerBurnsDuringTheDay extends LivingEntity {
    protected PlayerBurnsDuringTheDay(EntityType<? extends LivingEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Shadow
    public abstract boolean isCreative();

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void onAiStep(CallbackInfo ci) {
        boolean isDayOldEnough = MathUtils.shouldExcuteStage(this.level, MathUtils.STAGE.STAGE_3);

        if (isDayOldEnough && isAlive() && !isOnFire() && !this.level.isRaining() && !this.level.isNight() && !this.isCreative() && !this.level.isClientSide() && this.level.canSeeSky(new BlockPos(this.position())) && !hasEffect(GameRegister.SUNSCREEN_REGISTRY.get())) {
            setSecondsOnFire(8);
        }
    }
}
