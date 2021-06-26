package com.yor42.solarapocalypse;

import net.minecraft.world.World;

public class MathUtils {

    public static boolean isWorldOldEnough(World world, STAGE stage){
        double minecraftDay = (double) world.getDayTime()/24000;
        return !(stage.getDays() < 0.0) && SolApocalypseConfig.CONFIG.ApocalypseNow.get() && minecraftDay >= stage.getDays();
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
