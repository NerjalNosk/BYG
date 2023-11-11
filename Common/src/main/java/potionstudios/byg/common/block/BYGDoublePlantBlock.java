package potionstudios.byg.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BYGDoublePlantBlock extends DoublePlantBlock {
    private final TagKey<Block> mayPlaceOn;

    public BYGDoublePlantBlock(Properties properties, TagKey<Block> mayPlaceOn) {
        super(properties);
        this.mayPlaceOn = mayPlaceOn;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        return state.is(this.mayPlaceOn);
    }
}
