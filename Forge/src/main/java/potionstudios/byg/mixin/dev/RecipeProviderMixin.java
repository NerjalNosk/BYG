package potionstudios.byg.mixin.dev;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import potionstudios.byg.BYG;
import potionstudios.byg.common.BYGTags;

@Mixin(RecipeProvider.class)
public class RecipeProviderMixin {


    @Redirect(method = "signBuilder", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/recipes/ShapedRecipeBuilder;define(Ljava/lang/Character;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/data/recipes/ShapedRecipeBuilder;", ordinal = 0))
    private static ShapedRecipeBuilder useStickTag(ShapedRecipeBuilder instance, Character character, ItemLike stick) {
        if (BuiltInRegistries.ITEM.getKey(instance.getResult()).getNamespace().equalsIgnoreCase(BYG.MOD_ID)) {
            return instance.define('X', BYGTags.STICKS.all(BYGTags.RegistryType.ITEMS));
        } else {
            return instance.define('X', Items.STICK);
        }
    }
}
