package me.inkblindcat.coloredropes.datagen;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.block.ColoredRopeBlock;
import me.inkblindcat.coloredropes.block.RopeBlock;
import me.inkblindcat.coloredropes.client.color.ColoredItemTintSource;
import me.inkblindcat.coloredropes.client.conditional.IsGlowItem;
import me.inkblindcat.coloredropes.registries.ModBlocks;
import me.inkblindcat.coloredropes.registries.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;


public class ModModelProvider extends ModelProvider {
    public static final ModelTemplate GLOWING_ITEM = ModelTemplates.createItem("generated", "_glow",
            TextureSlot.LAYER0, TextureSlot.LAYER1);
    public static final ModelTemplate ROPE_ARROW_ITEM = ModelTemplates.createItem("generated",
            TextureSlot.LAYER0, TextureSlot.LAYER1);
    public static final ModelTemplate GLOWING_ROPE_ARROW_ITEM = ModelTemplates.createItem("generated", "_glow",
            TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2);


    public ModModelProvider(PackOutput pOutput) {
        super(pOutput, ColoredRopes.MODID);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().filter(block -> !isManuallyHandledBlock(block.get()));
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return ModItems.ITEMS.getEntries().stream().filter(item -> !isManuallyHandledItem(item.get()));
    }

    private boolean isManuallyHandledBlock(Block block) {
        return false;
    }

    private boolean isManuallyHandledItem(Item item) {
        return false;
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        createColoredRope(blockModels, ModBlocks.COLORED_ROPE.get());

        generateGlowingColoredItem(itemModels, ModBlocks.COLORED_ROPE.asItem());
        generateGlowingColoredItem(itemModels, ModItems.COLORED_ROPE_COIL.get());
        generateColoredRopeArrowItem(itemModels, ModItems.COLORED_ROPE_ARROW.get());
    }

    private void generateGlowingColoredItem(ItemModelGenerators itemModels, Item coloredItem) {
        ResourceLocation resourceLocation1 = itemModels.createFlatItemModel(coloredItem, ModelTemplates.FLAT_ITEM);
        ResourceLocation resourceLocation2 = GLOWING_ITEM.create(coloredItem, TextureMapping.layered(
                        ModelLocationUtils.getModelLocation(coloredItem).withSuffix("_without_glow"),
                        ModelLocationUtils.getModelLocation(coloredItem).withSuffix("_glow")
                ),itemModels.modelOutput
        );

        ItemModel.Unbaked itemModel1 = ItemModelUtils.tintedModel(resourceLocation1, new ColoredItemTintSource(0xFFFFFF));
        ItemModel.Unbaked itemModel2 = ItemModelUtils.tintedModel(resourceLocation2, new ColoredItemTintSource(0xFFFFFF));

        itemModels.itemModelOutput
                .accept(
                        coloredItem,
                        ItemModelUtils.conditional(
                                new IsGlowItem(),
                                itemModel2,
                                itemModel1
                        )
                );
    }

    private void generateColoredRopeArrowItem(ItemModelGenerators itemModels, Item coloredItem) {
        ResourceLocation resourceLocation1 = ROPE_ARROW_ITEM.create(coloredItem, TextureMapping.layered(
                        ModelLocationUtils.getModelLocation(coloredItem),
                        ModelLocationUtils.getModelLocation(coloredItem).withSuffix("_head")
                ),itemModels.modelOutput
        );
        ResourceLocation resourceLocation2 = GLOWING_ROPE_ARROW_ITEM.create(coloredItem, TextureMapping.layered(
                        ModelLocationUtils.getModelLocation(coloredItem),
                        ModelLocationUtils.getModelLocation(coloredItem).withSuffix("_head"),
                        ModelLocationUtils.getModelLocation(coloredItem).withSuffix("_glow")
                ),itemModels.modelOutput
        );

        ItemModel.Unbaked itemModel1 = ItemModelUtils.tintedModel(resourceLocation1, new ColoredItemTintSource(0xFFFFFF));
        ItemModel.Unbaked itemModel2 = ItemModelUtils.tintedModel(resourceLocation2, new ColoredItemTintSource(0xFFFFFF));

        itemModels.itemModelOutput
                .accept(
                        coloredItem,
                        ItemModelUtils.conditional(
                                new IsGlowItem(),
                                itemModel2,
                                itemModel1
                        )
                );
    }


    private void createColoredRope(BlockModelGenerators blockModels, Block ropeBlock) {
        // Regular models
        MultiVariant midTopStart = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_mid_top_start"));
        MultiVariant midTop = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_mid_top"));
        MultiVariant midBottomEnd = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_mid_bottom_end"));
        MultiVariant midBottom = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_mid_bottom"));

        MultiVariant sideTopStart = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_side_top_start"));
        MultiVariant sideTop = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_side_top"));
        MultiVariant sideBottomEnd = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_side_bottom_end"));
        MultiVariant sideBottom = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_side_bottom"));

        // Glowing models
        MultiVariant glowMidTopStart = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_glow_mid_top_start"));
        MultiVariant glowMidTop = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_glow_mid_top"));
        MultiVariant glowMidBottomEnd = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_glow_mid_bottom_end"));
        MultiVariant glowMidBottom = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_glow_mid_bottom"));

        MultiVariant glowSideTopStart = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_glow_side_top_start"));
        MultiVariant glowSideTop = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_glow_side_top"));
        MultiVariant glowSideBottomEnd = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_glow_side_bottom_end"));
        MultiVariant glowSideBottom = plainVariant(ModelLocationUtils.getModelLocation(ropeBlock, "_glow_side_bottom"));

        MultiPartGenerator multipart = MultiPartGenerator.multiPart(ropeBlock);

        BiConsumer<Direction, Boolean> addVariants = (dir, isGlow) -> {
            boolean up = dir == Direction.UP;

            MultiVariant topStart = up ? midTopStart : sideTopStart;
            MultiVariant top = up ? midTop : sideTop;
            MultiVariant bottomEnd = up ? midBottomEnd : sideBottomEnd;
            MultiVariant bottom = up ? midBottom : sideBottom;

            MultiVariant glowTopStart = up ? glowMidTopStart : glowSideTopStart;
            MultiVariant glowTop = up ? glowMidTop : glowSideTop;
            MultiVariant glowBottomEnd = up ? glowMidBottomEnd : glowSideBottomEnd;
            MultiVariant glowBottom = up ? glowMidBottom : glowSideBottom;

            // Turns for side directions
            VariantMutator rotation = switch (dir) {
                case NORTH -> BlockModelGenerators.Y_ROT_90;
                case EAST -> BlockModelGenerators.Y_ROT_180;
                case SOUTH -> BlockModelGenerators.Y_ROT_270;
                default -> BlockModelGenerators.NOP;
            };

            // Adding the top options
            multipart.with(
                    BlockModelGenerators.condition()
                            .term(RopeBlock.ATTACHMENT, dir)
                            .term(RopeBlock.START, true)
                            .term(ColoredRopeBlock.GLOW, isGlow),
                    isGlow ? glowTopStart.with(rotation) : topStart.with(rotation)
            );

            multipart.with(
                    BlockModelGenerators.condition()
                            .term(RopeBlock.ATTACHMENT, dir)
                            .term(RopeBlock.START, false)
                            .term(ColoredRopeBlock.GLOW, isGlow),
                    isGlow ? glowTop.with(rotation) : top.with(rotation)
            );

            // Adding the lower options
            multipart.with(
                    BlockModelGenerators.condition()
                            .term(RopeBlock.ATTACHMENT, dir)
                            .term(RopeBlock.END, true)
                            .term(ColoredRopeBlock.GLOW, isGlow),
                    isGlow ? glowBottomEnd.with(rotation) : bottomEnd.with(rotation)
            );

            multipart.with(
                    BlockModelGenerators.condition()
                            .term(RopeBlock.ATTACHMENT, dir)
                            .term(RopeBlock.END, false)
                            .term(ColoredRopeBlock.GLOW, isGlow),
                    isGlow ? glowBottom.with(rotation) : bottom.with(rotation)
            );
        };

        addVariants.accept(Direction.UP, false);
        addVariants.accept(Direction.UP, true);

        for (Direction dir : List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)) {
            addVariants.accept(dir, false);
            addVariants.accept(dir, true);
        }

        blockModels.blockStateOutput.accept(multipart);
    }

}