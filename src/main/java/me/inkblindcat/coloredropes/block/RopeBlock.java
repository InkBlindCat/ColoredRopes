package me.inkblindcat.coloredropes.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RopeBlock extends Block {
    public static final MapCodec<RopeBlock> CODEC = simpleCodec(RopeBlock::new);

    public static final BooleanProperty START = BooleanProperty.create("start");
    public static final BooleanProperty END = BooleanProperty.create("end");
    public static final EnumProperty<Direction> ATTACHMENT = EnumProperty.create(
            "attachment", Direction.class,
            Stream.of(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST).collect(Collectors.toList())
    );

    public static final Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.UP, Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0),
            Direction.NORTH, Block.box(6.0, 0.0, 2.0, 10.0, 16.0, 6.0),
            Direction.SOUTH, Block.box(6.0, 0.0, 10.0, 10.0, 16.0, 14.0),
            Direction.WEST, Block.box(2.0, 0.0, 6.0, 6.0, 16.0, 10.0),
            Direction.EAST, Block.box(10.0, 0.0, 6.0, 14.0, 16.0, 10.0)
    );

    @Override
    protected MapCodec<? extends Block> codec() { return CODEC; }

    public RopeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape baseShape = SHAPES.get(state.getValue(ATTACHMENT));

        if (state.getValue(END)) {
            VoxelShape cut = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
            return Shapes.join(baseShape, cut, BooleanOp.ONLY_FIRST);
        }

        return baseShape;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityCtx && entityCtx.getEntity() instanceof AbstractArrow) {
            return Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (stack.is(Items.SHEARS)) {
            List<BlockPos> ropePositions = new ArrayList<>();
            ropePositions.add(pos);
            ropePositions.addAll(getAllRopesBelow(level, pos));

            Collections.reverse(ropePositions);

            for (BlockPos ropePos : ropePositions) {
                BlockState ropeState = level.getBlockState(ropePos);

                List<ItemStack> drops = getDrops(ropeState,
                        (ServerLevel) level,
                        ropePos,
                        level.getBlockEntity(ropePos),
                        player,
                        stack
                );

                level.destroyBlock(ropePos, false);

                for (ItemStack drop : drops) {
                    if (!player.getInventory().add(drop)) {
                        Block.popResource(level, player.blockPosition(), drop);
                    }
                }
            }

            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

            return InteractionResult.CONSUME;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private boolean canAttachTo(BlockGetter level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);

        if (direction == Direction.UP && state.getBlock() instanceof RopeBlock) {
            return true;
        }

        return state.isFaceSturdy(level, pos, direction.getOpposite());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (aboveState.getBlock() instanceof RopeBlock) {
            return true;
        }

        Direction attach = state.getValue(ATTACHMENT);
        return canAttachTo(level, pos.relative(attach), attach);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        Direction attach = state.getValue(ATTACHMENT);
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (!canAttachTo(level, pos.relative(attach), attach)) {
            if (aboveState.getBlock() instanceof RopeBlock) {
                Direction aboveAttach = aboveState.getValue(ATTACHMENT);
                state = state.setValue(ATTACHMENT, aboveAttach);
                return setStartEndStates(state, level, pos);
            }

            for (Direction possible : ATTACHMENT.getPossibleValues()) {
                state = state.setValue(ATTACHMENT, possible);
                if (state.canSurvive(level, pos)) {
                    return setStartEndStates(state, level, pos);
                }
            }
            return Blocks.AIR.defaultBlockState();
        }
        if (!state.getValue(START)) {
            if (aboveState.getBlock() instanceof RopeBlock) {
                Direction aboveAttach = aboveState.getValue(ATTACHMENT);
                state = state.setValue(ATTACHMENT, aboveAttach);
                return setStartEndStates(state, level, pos);
            }
        }

        return setStartEndStates(state, level, pos);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = this.defaultBlockState();

        Direction dir = context.getClickedFace().getOpposite();
        if (dir == Direction.DOWN) return null;

        if (dir == Direction.UP) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            if (aboveState.is(this)) {
                Direction aboveAttach = aboveState.getValue(ATTACHMENT);
                state = state.setValue(ATTACHMENT, aboveAttach);
                return setStartEndStates(state, level, pos);
            }
        }

        state = state.setValue(ATTACHMENT, dir);
        if (canSurvive(state, level, pos)) {
            return setStartEndStates(state, level, pos);
        }

        return null;
    }

    public static BlockState setStartEndStates(BlockState state, LevelReader level, BlockPos pos) {
        Direction attach = state.getValue(ATTACHMENT);

        BlockPos abovePos = pos.above();
        BlockPos belowPos = pos.below();
        BlockState aboveState = level.getBlockState(abovePos);
        BlockState belowtate = level.getBlockState(belowPos);

        if (aboveState.getBlock() instanceof RopeBlock) {
            Direction aboveAttach = aboveState.getValue(ATTACHMENT);
            state = state.setValue(START, aboveAttach != attach);
        } else {
            state = state.setValue(START, true);
        }

        if (belowtate.getBlock() instanceof RopeBlock) {
            Direction belowAttach = belowtate.getValue(ATTACHMENT);
            state = state.setValue(END, belowAttach != attach);
        } else {
            state = state.setValue(END, true);
        }
        return state;
    }

    private static List<BlockPos> getAllRopesBelow(LevelReader level, BlockPos startPos) {
        List<BlockPos> result = new ArrayList<>();
        BlockPos currentPos = startPos.below();

        while (true) {
            BlockState state = level.getBlockState(currentPos);
            if (!(state.getBlock() instanceof RopeBlock)) break;

            result.add(currentPos);
            currentPos = currentPos.below();
        }

        return result;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        Direction attach = state.getValue(ATTACHMENT);
        if (attach.getAxis().isHorizontal()) {
            return state.setValue(ATTACHMENT, rotation.rotate(attach));
        }
        return state;
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction attach = state.getValue(ATTACHMENT);
        if (attach.getAxis().isHorizontal()) {
            return state.rotate(mirror.getRotation(attach));
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ATTACHMENT, START, END);
    }
}
