package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.GameRegister;
import com.yor42.solarapocalypse.MathUtils;
import com.yor42.solarapocalypse.SolApocalypseConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerBurnsDuringTheDay extends LivingEntity {
    protected PlayerBurnsDuringTheDay(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }

    @Shadow
    public abstract boolean isCreative();

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void onAiStep(CallbackInfo ci) {
        boolean isDayOldEnough = MathUtils.isWorldOldEnough(this.level, MathUtils.STAGE.STAGE_3);

        if (isDayOldEnough && isAlive() && !isOnFire() && !this.level.isRaining() && !this.level.isNight() && !this.isCreative() && !this.level.isClientSide() && this.level.canSeeSky(new BlockPos(this.position())) && !hasEffect(GameRegister.SUNSCREEN)) {
            setSecondsOnFire(8);
        }
    }
}
