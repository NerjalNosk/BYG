package potionstudios.byg.datagen.providers;

import net.minecraft.data.BlockFamilies;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import potionstudios.byg.common.BYGTags;

import java.util.function.Consumer;

public class BYGRecipeProviders extends RecipeProvider {


    public BYGRecipeProviders(DataGenerator pGenerator) {
        super(pGenerator.getPackOutput());
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        createWoodRecipes(consumer);
    }

    private static void createWoodRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        generateForEnabledBYGBlockFamilies(consumer, FeatureFlags.VANILLA_SET);
    }

    protected static void generateForEnabledBYGBlockFamilies(Consumer<FinishedRecipe> finishedRecipeConsumer, FeatureFlagSet featureFlagSet) {
        BYGBlockFamilies.getAllFamilies().filter((blockFamily) -> blockFamily.shouldGenerateRecipe(featureFlagSet)).forEach((blockFamily) -> generateRecipes(finishedRecipeConsumer, blockFamily));
    }

}
