package com.yor42.solarapocalypse.gameobjects;

import com.yor42.solarapocalypse.Constants;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.yor42.solarapocalypse.Main.SA_GROUP;

public class GameRegister {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);

    public static Effect SUNSCREEN = new CustomEffect(EffectType.BENEFICIAL, 0xffd073){
    };
    public static RegistryObject<Effect> SUNSCREEN_REGISTRY = EFFECTS.register("sunscreen", ()-> SUNSCREEN);

    public static Block DUSTBLOCK = new FallingBlock(AbstractBlock.Properties.of(Material.SAND, DyeColor.YELLOW).strength(0.5F).sound(SoundType.SAND));
    public static RegistryObject<Block> DUSTBLOCK_REGISTRY = register("dust", ()-> DUSTBLOCK, SA_GROUP);

    public static Block PANDORAS_TOTEM = new BlockPandorasTotem(AbstractBlock.Properties.of(Material.STONE, DyeColor.BLACK).strength(50.0F, 3600000.0F).sound(SoundType.STONE).lightLevel(activeBlockEmission(15)));

    private static ToIntFunction<BlockState> activeBlockEmission(int brightness) {
        return (state) -> {
            return state.getValue(BlockPandorasTotem.ACTIVE) ? brightness : 0;
        };
    }

    public static RegistryObject<Block> PANDORAS_TOTEM_REGISTRY = register("pandora_totem", ()-> PANDORAS_TOTEM, SA_GROUP);

    public static class CustomEffect extends Effect{
        protected CustomEffect(EffectType effectType, int color) {
            super(effectType, color);
        }
    }

    public static void register(){
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        EFFECTS.register(eventbus);
        BLOCKS.register(eventbus);
        ITEMS.register(eventbus);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, ItemGroup group){
        return register(name, block, group, new Item.Properties().tab(group));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, ItemGroup group, Item.Properties properties){
        RegistryObject<T> ret = register_noblock(name, block);
        ITEMS.register(name, () -> new BlockItem(ret.get(), properties.tab(group)));
        return ret;
    }

    private static <T extends Block> RegistryObject<T> register_noblock(String name, Supplier<T> block){
        return BLOCKS.register(name, block);
    }
}
