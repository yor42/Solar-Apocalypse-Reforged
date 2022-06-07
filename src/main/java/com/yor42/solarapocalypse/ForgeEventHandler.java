package com.yor42.solarapocalypse;

import com.yor42.solarapocalypse.gameobjects.GameRegister;
import com.yor42.solarapocalypse.utils.MathUtils;
import com.yor42.solarapocalypse.utils.SyncStagePacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

    @Nullable
    private static MathUtils.STAGE laststage;

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

    @SubscribeEvent
    public static void ServerTick(TickEvent.WorldTickEvent event){
        World world = event.world;
        if(!world.isClientSide() && world.getGameTime()%20 == 0) {
            MathUtils.STAGE stage = MathUtils.getCurrentStage((ServerWorld) world);
            if(stage != null) {
                Main.NETWORK.send(PacketDistributor.ALL.noArg(), new SyncStagePacket(stage));
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event){
        MathUtils.LEVEL_STAGE = null;
    }

}
