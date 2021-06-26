package com.yor42.solarapocalypse.datagen.providers.loottable;

import com.yor42.solarapocalypse.Constants;
import com.yor42.solarapocalypse.GameRegister;
import net.minecraft.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {

    @Override
    protected void addTables() {
        this.dropSelf(GameRegister.DUSTBLOCK_REGISTRY.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> Constants.MODID.equals(block.getRegistryName().getNamespace()))
                .collect(Collectors.toSet());
    }
}
