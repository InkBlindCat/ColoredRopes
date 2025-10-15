package me.inkblindcat.coloredropes.registries;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.block.ColoredRopeBlock;
import me.inkblindcat.coloredropes.item.ColoredRopeItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ColoredRopes.MODID);

    public static final DeferredBlock<Block> COLORED_ROPE = registerBlock(
            "colored_rope",
            properties -> new ColoredRopeBlock(properties
                    .noCollission()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(ColoredRopeBlock.GLOW) ? 10 : 0)
            ),
            (block, properties) -> new ColoredRopeItem(block, properties
                    .component(ModDataComponents.COLOR, DyeColor.WHITE)
                    .component(ModDataComponents.GLOW, false))
    );


    private static <T extends Block> DeferredBlock<T> registerBlock(
            String name,
            Function<BlockBehaviour.Properties, T> blockFunction,
            BiFunction<T, Item.Properties, Item> itemFunction) {
        DeferredBlock<T> block = registerBlockWithoutBlockItem(name, blockFunction);
        ModItems.registerItem(name, properties -> itemFunction.apply(block.get(), properties.useBlockDescriptionPrefix()));
        return block;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = registerBlockWithoutBlockItem(name, function);
        ModItems.ITEMS.registerSimpleBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithoutBlockItem(String name, Function<BlockBehaviour.Properties, T> function) {
        return BLOCKS.register(name, registryName -> function.apply(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, registryName))
        ));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
