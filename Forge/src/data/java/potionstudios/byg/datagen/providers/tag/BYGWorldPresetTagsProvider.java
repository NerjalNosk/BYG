package potionstudios.byg.datagen.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.WorldPresetTagsProvider;
import net.minecraft.tags.WorldPresetTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import potionstudios.byg.common.world.preset.BYGWorldPresets;

import java.util.concurrent.CompletableFuture;

public class BYGWorldPresetTagsProvider extends WorldPresetTagsProvider {
    public BYGWorldPresetTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookUpProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookUpProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(WorldPresetTags.EXTENDED).add(BYGWorldPresets.DEBUG_BIOMES);
    }
}