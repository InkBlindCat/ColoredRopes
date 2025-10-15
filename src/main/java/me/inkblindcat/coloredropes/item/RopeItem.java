package me.inkblindcat.coloredropes.item;

import me.inkblindcat.coloredropes.block.RopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RopeItem extends BlockItem {
    public RopeItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos below = pos.below();

        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof RopeBlock) {
            while (level.getBlockState(below).getBlock() instanceof RopeBlock) {
                below = below.below();
            }

            if (level.isEmptyBlock(below)) {
                BlockPos above = below.above();
                BlockState aboveState = level.getBlockState(above);
                Direction aboveAttach = aboveState.getValue(RopeBlock.ATTACHMENT);

                BlockPlaceContext blockPlaceContext = new BlockPlaceContext(context);
                blockPlaceContext = BlockPlaceContext.at(blockPlaceContext, below, aboveAttach.getOpposite());
                InteractionResult interactionresult = place(blockPlaceContext);

                return !interactionresult.consumesAction() && context.getItemInHand().has(DataComponents.CONSUMABLE)
                        ? super.use(context.getLevel(), context.getPlayer(), context.getHand())
                        : interactionresult;
            }
        }

        return super.useOn(context);
    }
}
