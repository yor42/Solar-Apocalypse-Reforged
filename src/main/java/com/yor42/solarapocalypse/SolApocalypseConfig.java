package com.yor42.solarapocalypse;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SolApocalypseConfig {

    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final SAModConfig CONFIG;

    static
    {
        final Pair<SAModConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(SAModConfig::new);
        CONFIG_SPEC = pair.getRight();
        CONFIG = pair.getLeft();
    }

    public static class SAModConfig{

        public final ForgeConfigSpec.DoubleValue STAGE_1;
        public final ForgeConfigSpec.DoubleValue STAGE_2;
        public final ForgeConfigSpec.DoubleValue STAGE_3;
        public final ForgeConfigSpec.DoubleValue STAGE_3_5;
        public final ForgeConfigSpec.BooleanValue IsWaterfinite;
        public final ForgeConfigSpec.BooleanValue EnableApocalypse;
        public final ForgeConfigSpec.BooleanValue ApocalypseNow;

        private SAModConfig(ForgeConfigSpec.Builder builder) {

            builder.push("Days until specific event");
            builder.comment("value -1 will disable the stage");
            STAGE_1 = builder.defineInRange("Days until Grass Starts to die out", 3.0, -1.0,Double.MAX_VALUE);
            STAGE_2 = builder.defineInRange("Days until water starts to evaporate and meltable/organic block gets destroyed", 5.0, -1.0,Double.MAX_VALUE);
            STAGE_3 = builder.defineInRange("Days Until All mob burns in Sunlight", 7.0, -1.0, Double.MAX_VALUE);
            STAGE_3_5 = builder.defineInRange("Days Until Coarse Dirt turns into Sand", -1.0, -1.0, Double.MAX_VALUE);
            builder.pop().push("Misc").comment("Caution! this will turn water into finite resource!");
            IsWaterfinite = builder.define("Turn Infinite water off and water evaporates", true);
            builder.comment("even if this is set to false  apocalypse can still be started with pandora's lantern.");
            ApocalypseNow = builder.define("Start Apocalypse as soon as world starts", true);
            builder.comment("turns off apocalypse completely. turns pandora's lantern to normal light source");
            EnableApocalypse = builder.define("Enable Mod", true);
        }

    }

}
