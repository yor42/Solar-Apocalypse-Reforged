package com.yor42.solarapocalypse.mixins;

import com.yor42.solarapocalypse.gameobjects.GameRegister;
import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = Mob.class, priority = 1001)
public abstract class MixinAllMobBurnsInSunlight extends LivingEntity {

    protected MixinAllMobBurnsInSunlight(EntityType<? extends LivingEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Inject(method = "isSunBurnTick", at = @At("RETURN"), cancellable = true)
    private void onisSunBurnTick(CallbackInfoReturnable<Boolean> cir) {
        if (MathUtils.shouldExcuteStage(this.level , MathUtils.STAGE.STAGE_3) && isAlive() && !isOnFire() && !this.level.isRaining() && !this.level.isNight() && !this.level.isClientSide() && this.level.canSeeSky(new BlockPos(this.position())) && !hasEffect(GameRegister.SUNSCREEN_REGISTRY.get())) {
            cir.setReturnValue(true);;
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void hookAistep(CallbackInfo ci) {

        boolean isDayOldEnough = MathUtils.shouldExcuteStage(this.level, MathUtils.STAGE.STAGE_3);

        if (isDayOldEnough && isAlive() && !isOnFire() && !this.level.isRaining() && !this.level.isNight() && !this.level.isClientSide() && this.level.canSeeSky(new BlockPos(this.position())) && !hasEffect(GameRegister.SUNSCREEN_REGISTRY.get())) {
            setSecondsOnFire(8);
        }
    }

}
