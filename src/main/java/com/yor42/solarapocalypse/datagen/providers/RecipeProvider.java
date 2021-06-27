package com.yor42.solarapocalypse.datagen.providers;

import com.yor42.solarapocalypse.gameobjects.GameRegister;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.RecipeProvider{
    public RecipeProvider(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(GameRegister.PANDORAS_TOTEM.asItem())
                .define('C', Tags.Items.COBBLESTONE)
                .define('P', Blocks.OAK_PLANKS.asItem())
                .define('T', Blocks.TORCH.asItem())
                .pattern("PCP")
                .pattern("CTC")
                .pattern("PCP")
                .unlockedBy("has_torch", has(Blocks.TORCH.asItem()))
                .save(consumer);
    }
}
