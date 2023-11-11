package potionstudios.byg.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class HydrangeaBushBlock extends BYGFlowerBlock implements BonemealableBlock {
    private final TagKey<Block> validGround;

    protected HydrangeaBushBlock(Properties properties, TagKey<Block> validGround) {
        super(properties, validGround);
        this.validGround = validGround;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return state.is(validGround);
    }


    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader blockGetter, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean b) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, @NotNull RandomSource random, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        serverLevel.setBlockAndUpdate(blockPos, BYGBlocks.HYDRANGEA_HEDGE.defaultBlockState());
        serverLevel.neighborChanged(blockPos, BYGBlocks.HYDRANGEA_HEDGE.get(), blockPos);
    }


}
