package potionstudios.byg.common;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import potionstudios.byg.common.block.BYGBlocks;
import potionstudios.byg.mixin.access.ShovelItemAccess;

import java.util.IdentityHashMap;
import java.util.Map;

public class BYGPaths {

    public static void addBYGPaths() {
        Map<Block, BlockState> flattenables = new IdentityHashMap<>(ShovelItemAccess.getFlattenables());
        flattenables.put(BYGBlocks.LUSH_GRASS_BLOCK, BYGBlocks.LUSH_GRASS_PATH.defaultBlockState());
        ShovelItemAccess.setFlattenables(flattenables);
    }
}