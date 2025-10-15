package me.inkblindcat.coloredropes.datagen;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.registries.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ColoredRopes.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.CLIMBABLE).add(ModBlocks.COLORED_ROPE.get());
        tag(BlockTags.DAMPENS_VIBRATIONS).add(ModBlocks.COLORED_ROPE.get());
    }
}
