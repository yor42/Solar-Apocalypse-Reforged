package com.yor42.solarapocalypse.utils;

import com.yor42.solarapocalypse.Constants;
import com.yor42.solarapocalypse.Main;
import com.yor42.solarapocalypse.SolApocalypseConfig;
import com.yor42.solarapocalypse.SolarApocalypseMapData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class MathUtils {

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static STAGE LEVEL_STAGE;

    private static STAGE laststage = null;


    public static boolean shouldExcuteStage(World world, STAGE stage){
        if(!world.isClientSide()){
            return shouldExcuteStage((ServerWorld) world, stage);
        }
        else{
            return false;
        }
    }

    public static boolean shouldExcuteStage(ServerWorld world, STAGE stage){
        double minecraftDay = (double) world.getDayTime()/24000;

        SolarApocalypseMapData mapData = SolarApocalypseMapData.getMapdata(world);
        double apocalypseStartedDay = mapData.getApocalypseStartedDay();

        if(!SolApocalypseConfig.CONFIG.ApocalypseNow.get()){
            if(!(stage.getDays() < 0.0) && SolApocalypseConfig.CONFIG.EnableApocalypse.get() && mapData.isApocalypseStarted() && minecraftDay - apocalypseStartedDay >= stage.getDays()){

                return true;
            }
        }
        else {
            if(!SolarApocalypseMapData.isApocalypseStarted(world)) {
                SolarApocalypseMapData.StartApocalypseFronTheBeginning(world);
            }
            if(!(stage.getDays() < 0.0) && SolApocalypseConfig.CONFIG.EnableApocalypse.get() && minecraftDay >= stage.getDays()){
                return true;
            }
        }
        return false;
    }

    public static STAGE getCurrentStage(ServerWorld world){

        for(int i = STAGE.values().length-1; i>=0; i--){
            STAGE stage = STAGE.values()[i];
            if(shouldExcuteStage(world, stage)){
                return stage;
            }
        }
        return null;
    }



    public enum STAGE{
        STAGE_1(SolApocalypseConfig.CONFIG.STAGE_1.get(),"sun_stage1"),
        STAGE_2(SolApocalypseConfig.CONFIG.STAGE_2.get(),"sun_stage2"),
        STAGE_3(SolApocalypseConfig.CONFIG.STAGE_3.get(),"sun_stage3"),
        STAGE_3_5(SolApocalypseConfig.CONFIG.STAGE_3_5.get(), "sun_stage3.5");

        private final double Days;
        private final ResourceLocation suntexture;
        STAGE(double days, String suntexture){
            this.Days = days;
            this.suntexture = new ResourceLocation(Constants.MODID, "textures/environment/"+suntexture+".png");
        }

        public ResourceLocation getSuntexture() {
            return suntexture;
        }

        public double getDays() {
            return this.Days;
        }
    }

}
