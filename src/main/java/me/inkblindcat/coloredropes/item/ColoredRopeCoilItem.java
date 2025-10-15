package me.inkblindcat.coloredropes.item;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.block.ColoredRopeBlock;
import me.inkblindcat.coloredropes.block.RopeBlock;
import me.inkblindcat.coloredropes.registries.ModBlocks;
import me.inkblindcat.coloredropes.util.DataComponentsUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class ColoredRopeCoilItem extends Item {
    public ColoredRopeCoilItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();

        ItemStack itemStack = context.getItemInHand();

        BlockHitResult blockHitResult = new BlockHitResult(
                context.getClickLocation(),
                context.getClickedFace(),
                blockPos,
                context.isInside()
        );

        InteractionResult interactionResult = processRopeAttachment(level, blockHitResult, player, itemStack, 8);

        if (interactionResult == InteractionResult.SUCCESS) {
            BlockState blockState = ModBlocks.COLORED_ROPE.get().defaultBlockState();
            SoundType soundtype = blockState.getSoundType(level, blockPos, player);
            level.playSound(
                    player,
                    blockPos,
                    soundtype.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (soundtype.getVolume() + 1.0F) / 2.0F,
                    soundtype.getPitch() * 0.8F
            );
            itemStack.consume(1, player);
        }
        return interactionResult;
    }

    public static InteractionResult processRopeAttachment(Level level, BlockHitResult hitResult,
                                                          Player player, ItemStack stack, int amount) {
        BlockPos blockPos = hitResult.getBlockPos();
        Direction direction = hitResult.getDirection();
        Direction attachment = direction.getOpposite();

        // Attempt to attach the rope to the targeted block
        if (level.getBlockState(blockPos).isFaceSturdy(level, blockPos, attachment)) {
            BlockPos placePos = blockPos.relative(direction);

            // Only place rope if the space is empty or replaceable and not attaching downward
            if ((level.isEmptyBlock(placePos) || level.getBlockState(placePos).canBeReplaced()) && attachment != Direction.DOWN) {
                placeColoredRopeBlocks(level, placePos, attachment, player, stack, amount);
                return InteractionResult.SUCCESS;
            }
        } else if (level.getBlockState(blockPos).getBlock() instanceof RopeBlock) {
            // If the clicked block is a rope, find the bottom of the rope chain
            BlockPos below = blockPos.below();
            while (level.getBlockState(below).getBlock() instanceof RopeBlock) {
                below = below.below();
            }

            BlockState aboveBlockState = level.getBlockState(below.above());
            attachment = aboveBlockState.getValue(ColoredRopeBlock.ATTACHMENT);

            // Place rope blocks from the bottom if possible
            if ((level.isEmptyBlock(below) || level.getBlockState(below).canBeReplaced()) && attachment != Direction.DOWN) {
                placeColoredRopeBlocks(level, below, attachment, player, stack, amount);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private static void placeColoredRopeBlocks(Level level, BlockPos pos, Direction attachment,
                                               Player player, ItemStack stack, int amount) {
        if (!level.isClientSide()) {
            BlockPos currentPos = pos;
            for (int i = 0; i < amount; i++) {
                if (!placeColoredRopeBlock(level, currentPos, attachment, player, stack)) {
                    // If the block cannot be placed, drop the remaining ropes at the position above the current one
                    BlockPos dropPos = currentPos.above();
                    DyeColor color = DataComponentsUtil.getColor(stack);
                    boolean glow = DataComponentsUtil.getGlow(stack);

                    ItemStack ropeStack = new ItemStack(ModBlocks.COLORED_ROPE.asItem(), amount - i);
                    DataComponentsUtil.setColor(ropeStack, color);
                    DataComponentsUtil.setGlow(ropeStack, glow);

                    ItemEntity itemEntity = new ItemEntity(
                            level,
                            dropPos.getX() + 0.5,
                            dropPos.getY() + 0.5,
                            dropPos.getZ() + 0.5,
                            ropeStack
                    );
                    level.addFreshEntity(itemEntity);
                    break;
                }
                currentPos = currentPos.below();
            }
        }
    }

    private static boolean placeColoredRopeBlock(Level level, BlockPos pos, Direction attachment, Player player, ItemStack stack) {
        if ((level.isEmptyBlock(pos) || level.getBlockState(pos).canBeReplaced()) && attachment != Direction.DOWN) {

            DyeColor color = DataComponentsUtil.getColor(stack);
            boolean glow = DataComponentsUtil.getGlow(stack);

            BlockState state = ModBlocks.COLORED_ROPE.get().defaultBlockState()
                    .setValue(RopeBlock.ATTACHMENT, attachment)
                    .setValue(ColoredRopeBlock.COLOR, color)
                    .setValue(ColoredRopeBlock.GLOW, glow);
            state = RopeBlock.setStartEndStates(state, level, pos);
            level.setBlock(pos, state, 11);

            state.getBlock().setPlacedBy(level, pos, state, player, stack);
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
            }
            level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, state));
            return true;
        }
        return false;
    }

    @Override
    public Component getName(ItemStack stack) {
        DyeColor color = DataComponentsUtil.getColor(stack);
        String glow = DataComponentsUtil.getGlow(stack) ? ".glow" : "";
        return Component.translatable("block." + ColoredRopes.MODID + ".colored_rope_coil." + color.getSerializedName() + glow);
    }
}
