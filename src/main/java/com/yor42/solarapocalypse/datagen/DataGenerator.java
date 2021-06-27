package com.yor42.solarapocalypse.datagen;

import com.yor42.solarapocalypse.Constants;
import com.yor42.solarapocalypse.datagen.providers.BlockModelProvider;
import com.yor42.solarapocalypse.datagen.providers.ItemModelProvider;
import com.yor42.solarapocalypse.datagen.providers.RecipeProvider;
import com.yor42.solarapocalypse.datagen.providers.loottable.LootTableProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        net.minecraft.data.DataGenerator gen = event.getGenerator();
        ExistingFileHelper filehelper = event.getExistingFileHelper();

        gen.addProvider(new BlockModelProvider(gen, filehelper));
        gen.addProvider(new ItemModelProvider(gen, filehelper));
        gen.addProvider(new LootTableProvider(gen));
        gen.addProvider(new RecipeProvider(gen));
    }

}
