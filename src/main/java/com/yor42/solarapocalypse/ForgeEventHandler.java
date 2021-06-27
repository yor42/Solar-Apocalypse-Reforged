package com.yor42.solarapocalypse;

import com.yor42.solarapocalypse.gameobjects.GameRegister;
import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event){
        PlayerEntity player = event.getPlayer();

        if(player.level.isClientSide){
            return;
        }

        World world = player.level;
        if(!MathUtils.shouldExcuteStage((ServerWorld) player.level, MathUtils.STAGE.STAGE_3) || !player.isAlive() || world.isRaining() || player.isSpectator() || player.isCreative() || world.isNight() || world.isClientSide() || !world.canSeeSky(new BlockPos(player.position())) || player.hasEffect(GameRegister.SUNSCREEN_REGISTRY.get())){
            return;
        }
        player.addEffect(new EffectInstance(GameRegister.SUNSCREEN_REGISTRY.get(), 2400, 0, false, false, true));
    }

}
