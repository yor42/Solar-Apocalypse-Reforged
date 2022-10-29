package com.yor42.solarapocalypse.gameobjects;

import com.yor42.solarapocalypse.Constants;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.yor42.solarapocalypse.Main.SA_GROUP;

public class GameRegister {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);

    public static MobEffect SUNSCREEN = new CustomPotion(MobEffectCategory.BENEFICIAL, 0xffd073){
    };
    public static RegistryObject<MobEffect> SUNSCREEN_REGISTRY = EFFECTS.register("sunscreen", ()-> SUNSCREEN);

    public static Block DUSTBLOCK = new FallingBlock(BlockBehaviour.Properties.of(Material.SAND, DyeColor.YELLOW).strength(0.5F).sound(SoundType.SAND));
    public static RegistryObject<Block> DUSTBLOCK_REGISTRY = register("dust", ()-> DUSTBLOCK, SA_GROUP);

    public static Block PANDORAS_TOTEM = new BlockPandorasTotem(BlockBehaviour.Properties.of(Material.STONE, DyeColor.BLACK).strength(50.0F, 3600000.0F).sound(SoundType.STONE).lightLevel(activeBlockEmission(15)));

    private static ToIntFunction<BlockState> activeBlockEmission(int brightness) {
        return (state) -> {
            return state.getValue(BlockPandorasTotem.ACTIVE) ? brightness : 0;
        };
    }

    public static RegistryObject<Block> PANDORAS_TOTEM_REGISTRY = register("pandora_totem", ()-> PANDORAS_TOTEM, SA_GROUP);

    public static class CustomPotion extends MobEffect{
        protected CustomPotion(MobEffectCategory effectType, int color) {
            super(effectType, color);
        }
    }

    public static void register(){
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        EFFECTS.register(eventbus);
        BLOCKS.register(eventbus);
        ITEMS.register(eventbus);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, CreativeModeTab group){
        return register(name, block, group, new Item.Properties().tab(group));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, CreativeModeTab group, Item.Properties properties){
        RegistryObject<T> ret = register_noblock(name, block);
        ITEMS.register(name, () -> new BlockItem(ret.get(), properties.tab(group)));
        return ret;
    }

    private static <T extends Block> RegistryObject<T> register_noblock(String name, Supplier<T> block){
        return BLOCKS.register(name, block);
    }
}
