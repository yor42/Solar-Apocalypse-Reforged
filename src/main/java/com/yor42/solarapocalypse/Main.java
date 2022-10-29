package com.yor42.solarapocalypse;

import com.yor42.solarapocalypse.gameobjects.BlockPandorasTotem;
import com.yor42.solarapocalypse.gameobjects.GameRegister;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.yor42.solarapocalypse.RegisterNetwork.getChannel;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.MODID)
public class Main
{
    public static final SimpleChannel NETWORK = getChannel();

    public static CreativeModeTab SA_GROUP = new CreativeModeTab(Constants.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GameRegister.DUSTBLOCK_REGISTRY.get().asItem());
        }
    };
    public Main() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SolApocalypseConfig.CONFIG_SPEC, "solar_apocalypse.toml");
        MinecraftForge.EVENT_BUS.register(this);
        GameRegister.register();
    }
}
