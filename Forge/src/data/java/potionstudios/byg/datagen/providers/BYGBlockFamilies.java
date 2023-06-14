package potionstudios.byg.datagen.providers;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import potionstudios.byg.common.block.BYGBlocks;
import potionstudios.byg.common.block.BYGWoodTypes;

import java.util.Map;
import java.util.stream.Stream;

public class BYGBlockFamilies {

    private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();

    public static final BlockFamily ASPEN = familyBuilder(BYGWoodTypes.ASPEN).getFamily();
    public static final BlockFamily BAOBAB = familyBuilder(BYGWoodTypes.BAOBAB).getFamily();
    public static final BlockFamily BLUE_ENCHANTED = familyBuilder(BYGWoodTypes.BLUE_ENCHANTED).getFamily();
    public static final BlockFamily CIKA = familyBuilder(BYGWoodTypes.CIKA).getFamily();
    public static final BlockFamily CYPRESS = familyBuilder(BYGWoodTypes.CYPRESS).getFamily();
    public static final BlockFamily EBONY = familyBuilder(BYGWoodTypes.EBONY).getFamily();
    public static final BlockFamily FIR = familyBuilder(BYGWoodTypes.FIR).getFamily();
    public static final BlockFamily GREEN_ENCHANTED = familyBuilder(BYGWoodTypes.GREEN_ENCHANTED).getFamily();
    public static final BlockFamily HOLLY = familyBuilder(BYGWoodTypes.HOLLY).getFamily();
    public static final BlockFamily MAHOGANY = familyBuilder(BYGWoodTypes.MAHOGANY).getFamily();
    public static final BlockFamily WHITE_MANGROVE = familyBuilder(BYGWoodTypes.WHITE_MANGROVE).getFamily();
    public static final BlockFamily PALM = familyBuilder(BYGWoodTypes.PALM).getFamily();
    public static final BlockFamily PINE = familyBuilder(BYGWoodTypes.PINE).getFamily();
    public static final BlockFamily RAINBOW_EUCALYPTUS = familyBuilder(BYGWoodTypes.RAINBOW_EUCALYPTUS).getFamily();
    public static final BlockFamily REDWOOD = familyBuilder(BYGWoodTypes.REDWOOD).getFamily();
    public static final BlockFamily WILLOW = familyBuilder(BYGWoodTypes.WILLOW).getFamily();
    public static final BlockFamily WITCH_HAZEL = familyBuilder(BYGWoodTypes.WITCH_HAZEL).getFamily();
    public static final BlockFamily ZELKOVA = familyBuilder(BYGWoodTypes.ZELKOVA).getFamily();
    public static final BlockFamily SKYRIS = familyBuilder(BYGWoodTypes.SKYRIS).getFamily();
    public static final BlockFamily MAPLE = familyBuilder(BYGWoodTypes.MAPLE).getFamily();
    public static final BlockFamily JACARANDA = familyBuilder(BYGWoodTypes.JACARANDA).getFamily();

    public static final BlockFamily WINDSWEPT_SANDSTONE = familyBuilder(BYGBlocks.WINDSWEPT_SANDSTONE.get()).slab(BYGBlocks.WINDSWEPT_SANDSTONE_SLAB.get()).stairs(BYGBlocks.WINDSWEPT_SANDSTONE_STAIRS.get()).wall(BYGBlocks.WINDSWEPT_SANDSTONE_WALL.get()).chiseled(BYGBlocks.CHISELED_WINDSWEPT_SANDSTONE.get()).cut(BYGBlocks.CUT_WINDSWEPT_SANDSTONE.get()).getFamily();
    public static final BlockFamily CUT_WINDSWEPT_SANDSTONE = familyBuilder(BYGBlocks.CUT_WINDSWEPT_SANDSTONE.get()).slab(BYGBlocks.CUT_WINDSWEPT_SANDSTONE_SLAB.get()).stairs(BYGBlocks.CUT_WINDSWEPT_SANDSTONE_STAIRS.get()).wall(BYGBlocks.CUT_WINDSWEPT_SANDSTONE_WALL.get()).getFamily();
    public static final BlockFamily SMOOTH_WINDSWEPT_SANDSTONE = familyBuilder(BYGBlocks.SMOOTH_WINDSWEPT_SANDSTONE.get()).slab(BYGBlocks.SMOOTH_WINDSWEPT_SANDSTONE_SLAB.get()).stairs(BYGBlocks.SMOOTH_WINDSWEPT_SANDSTONE_STAIRS.get()).wall(BYGBlocks.SMOOTH_WINDSWEPT_SANDSTONE_WALL.get()).getFamily();


    public static final BlockFamily BLUE_SANDSTONE = familyBuilder(BYGBlocks.BLUE_SANDSTONE.get()).slab(BYGBlocks.BLUE_SANDSTONE_SLAB.get()).stairs(BYGBlocks.BLUE_SANDSTONE_STAIRS.get()).wall(BYGBlocks.BLUE_SANDSTONE_WALL.get()).chiseled(BYGBlocks.BLUE_CHISELED_SANDSTONE.get()).cut(BYGBlocks.BLUE_CUT_SANDSTONE.get()).getFamily();
    public static final BlockFamily CUT_BLUE_SANDSTONE = familyBuilder(BYGBlocks.BLUE_CUT_SANDSTONE.get()).slab(BYGBlocks.BLUE_CUT_SANDSTONE_SLAB.get()).stairs(BYGBlocks.BLUE_CUT_SANDSTONE_STAIRS.get()).wall(BYGBlocks.BLUE_CUT_SANDSTONE_WALL.get()).getFamily();
    public static final BlockFamily SMOOTH_BLUE_SANDSTONE = familyBuilder(BYGBlocks.BLUE_SMOOTH_SANDSTONE.get()).slab(BYGBlocks.BLUE_CUT_SANDSTONE_SLAB.get()).stairs(BYGBlocks.BLUE_SMOOTH_SANDSTONE_STAIRS.get()).wall(BYGBlocks.SMOOTH_WINDSWEPT_SANDSTONE_WALL.get()).getFamily();


    public static BlockFamily.Builder familyBuilder(BYGWoodTypes woodTypes) {
        Block block = woodTypes.planks().get();
        BlockFamily.Builder blockfamily$builder = new BlockFamily.Builder(block);

        blockfamily$builder.button(woodTypes.button().get()).fence(woodTypes.fence().get()).fenceGate(woodTypes.fenceGate().get()).pressurePlate(woodTypes.pressurePlate().get()).sign(woodTypes.sign().get(), woodTypes.wallSign().get()).slab(woodTypes.slab().get()).stairs(woodTypes.stairs().get()).door(woodTypes.door().get()).trapdoor(woodTypes.trapdoor().get()).recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks");
        BlockFamily blockfamily = MAP.put(block, blockfamily$builder.getFamily());
        if (blockfamily != null) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(block));
        } else {
            return blockfamily$builder;
        }
    }

    public static BlockFamily.Builder familyBuilder(Block block) {
        BlockFamily.Builder blockfamily$builder = new BlockFamily.Builder(block);
        BlockFamily blockfamily = MAP.put(block, blockfamily$builder.getFamily());
        if (blockfamily != null) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(block));
        } else {
            return blockfamily$builder;
        }
    }

    public static Stream<BlockFamily> getAllFamilies() {
        return MAP.values().stream();
    }

}
