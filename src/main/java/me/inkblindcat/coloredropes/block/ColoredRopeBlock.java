package me.inkblindcat.coloredropes.block;

import com.mojang.serialization.MapCodec;
import me.inkblindcat.coloredropes.registries.ModBlocks;
import me.inkblindcat.coloredropes.util.DataComponentsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;


public class ColoredRopeBlock extends RopeBlock{
    public static final MapCodec<ColoredRopeBlock> CODEC = simpleCodec(ColoredRopeBlock::new);
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final BooleanProperty GLOW = BooleanProperty.create("glow");

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public ColoredRopeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;

        ItemStack itemStack = context.getItemInHand();
        if (itemStack.is(ModBlocks.COLORED_ROPE.asItem())) {
            state = state.setValue(COLOR, DataComponentsUtil.getColor(itemStack));
            state = state.setValue(GLOW, DataComponentsUtil.getGlow(itemStack));
        }
        return state;
    }

    @Override
    public MapColor getMapColor(BlockState state, BlockGetter level, BlockPos pos, MapColor defaultColor) {
        return state.getValue(COLOR).getMapColor();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, GLOW);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        DyeColor color = state.getValue(COLOR);
        Boolean glow = state.getValue(GLOW);
        ItemStack stack = new ItemStack(ModBlocks.COLORED_ROPE.asItem());
        DataComponentsUtil.setColor(stack, color);
        DataComponentsUtil.setGlow(stack, glow);

        return stack;
    }
}
