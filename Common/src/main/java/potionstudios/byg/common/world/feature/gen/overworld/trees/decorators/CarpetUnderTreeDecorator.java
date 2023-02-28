package potionstudios.byg.common.world.feature.gen.overworld.trees.decorators;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class CarpetUnderTreeDecorator extends TreeDecorator {
    public static final Codec<CarpetUnderTreeDecorator> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.intRange(1, 5).fieldOf("radius").forGetter(attachedToLogsDecorator -> attachedToLogsDecorator.radius),
                    BlockStateProvider.CODEC.fieldOf("provider").forGetter(carpetUnderTreeDecorator -> carpetUnderTreeDecorator.provider)
            ).apply(builder, (CarpetUnderTreeDecorator::new)));

    private final int radius;
    private final BlockStateProvider provider;

    public CarpetUnderTreeDecorator(int radius, BlockStateProvider provider) {
        this.radius = radius;
        this.provider = provider;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return BYGTreeDecoratorTypes.CARPET_UNDER_TREE.get();
    }

    @Override
    public void place(Context var1) {
        List<BlockPos> $$1 = Lists.newArrayList();
        List<BlockPos> $$2 = var1.roots();
        List<BlockPos> $$3 = var1.logs();
        if ($$2.isEmpty()) {
            $$1.addAll($$3);
        } else if (!$$3.isEmpty() && $$2.get(0).getY() == $$3.get(0).getY()) {
            $$1.addAll($$3);
            $$1.addAll($$2);
        } else {
            $$1.addAll($$2);
        }

        if (!$$1.isEmpty()) {
            int $$4 = $$1.get(0).getY();
            $$1.stream().filter(($$1x) -> $$1x.getY() == $$4).forEach(($$1x) -> {
                this.placeCircle(var1, $$1x.west().north());
                this.placeCircle(var1, $$1x.east(2).north());
                this.placeCircle(var1, $$1x.west().south(2));
                this.placeCircle(var1, $$1x.east(2).south(2));

                for(int $$2x = 0; $$2x < 5; ++$$2x) {
                    int $$3x = var1.random().nextInt(64);
                    int $$4x = $$3x % 8;
                    int $$5 = $$3x / 8;
                    if ($$4x == 0 || $$4x == 7 || $$5 == 0 || $$5 == 7) {
                        this.placeCircle(var1, $$1x.offset(-3 + $$4x, 0, -3 + $$5));
                    }
                }

            });
        }
    }

    private void placeCircle(TreeDecorator.Context var1, BlockPos $$1) {
        for(int $$2 = -radius; $$2 <= radius; ++$$2) {
            for(int $$3 = -radius; $$3 <= radius; ++$$3) {
                if (Math.abs($$2) != radius || Math.abs($$3) != radius) {
                    this.placeBlockAt(var1, $$1.offset($$2, 0, $$3));
                }
            }
        }

    }

    private void placeBlockAt(TreeDecorator.Context var1, BlockPos $$1) {
        for(int $$2 = 2; $$2 >= -3; --$$2) {
            BlockPos $$3 = $$1.above($$2);
            if (Feature.isGrassOrDirt(var1.level(), $$3)) {
                var1.setBlock($$3.above(), this.provider.getState(var1.random(), $$1));
                break;
            }

            if (!var1.isAir($$3) && $$2 < 0) {
                break;
            }
        }

    }
}
