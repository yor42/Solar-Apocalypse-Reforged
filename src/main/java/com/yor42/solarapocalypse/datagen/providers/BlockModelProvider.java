package com.yor42.solarapocalypse.datagen.providers;

import com.yor42.solarapocalypse.Constants;
import com.yor42.solarapocalypse.gameobjects.GameRegister;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends net.minecraftforge.client.model.generators.BlockStateProvider{
    public BlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(GameRegister.DUSTBLOCK_REGISTRY.get());
    }
}
