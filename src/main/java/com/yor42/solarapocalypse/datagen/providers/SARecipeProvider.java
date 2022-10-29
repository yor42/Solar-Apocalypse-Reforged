package com.yor42.solarapocalypse.datagen.providers;

import com.yor42.solarapocalypse.gameobjects.GameRegister;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class SARecipeProvider extends RecipeProvider {
    public SARecipeProvider(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }


    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
        buildShapelessRecipes(p_176532_);
    }

    protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
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
