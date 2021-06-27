package com.yor42.solarapocalypse.utils;

import com.yor42.solarapocalypse.SolApocalypseConfig;
import com.yor42.solarapocalypse.SolarApocalypseMapData;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class MathUtils {

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
            return !(stage.getDays() < 0.0) && SolApocalypseConfig.CONFIG.EnableApocalypse.get() && mapData.isApocalypseStarted() && minecraftDay - apocalypseStartedDay >= stage.getDays();
        }
        else {
            if(SolarApocalypseMapData.isApocalypseStarted(world)) {
                SolarApocalypseMapData.StartApocalypseFronTheBeginning(world);
            }
            return !(stage.getDays() < 0.0) && SolApocalypseConfig.CONFIG.EnableApocalypse.get() && minecraftDay >= stage.getDays();
        }
    }



    public enum STAGE{
        STAGE_1(SolApocalypseConfig.CONFIG.STAGE_1.get()),
        STAGE_2(SolApocalypseConfig.CONFIG.STAGE_2.get()),
        STAGE_3(SolApocalypseConfig.CONFIG.STAGE_3.get()),
        STAGE_3_5(SolApocalypseConfig.CONFIG.STAGE_3_5.get());

        private final double Days;
        STAGE(double days){
            this.Days = days;
        }

        public double getDays() {
            return this.Days;
        }
    }

}
