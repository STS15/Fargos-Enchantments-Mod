package dev.sts15.fargos.blocks.crucibleofthecosmos;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nullable;
import dev.sts15.fargos.screen.crucibleofthecosmos.CrucibleOfTheCosmosMenu;

public class CrucibleOfTheCosmos extends HorizontalDirectionalBlock {
    public static final EnumProperty<ChestType> PART = BlockStateProperties.CHEST_TYPE;

    public static final VoxelShape SHAPE_NORTH = Shapes.or(
    	    Block.box(0, 0, 0, 32, 14, 16)
    	);

    	public static final VoxelShape SHAPE_SOUTH = Shapes.or(
    	    Block.box(-16, 0, 0, 16, 14, 16)
    	);

    	public static final VoxelShape SHAPE_EAST = Shapes.or(
    	    Block.box(0, 0, -16, 16, 14, 16)
    	);

    	public static final VoxelShape SHAPE_WEST = Shapes.or(
    	    Block.box(0, 0, 0, 16, 14, 32)
    	);

    public CrucibleOfTheCosmos() {
        super(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, ChestType.SINGLE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos blockpos = pContext.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction.getCounterClockWise());
        Level level = pContext.getLevel();
        if (level.getBlockState(blockpos1).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(blockpos1)) {
            return this.defaultBlockState().setValue(FACING, direction.getOpposite());
        }
        return null;
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide) {
            BlockPos blockpos = pPos.relative(pState.getValue(FACING).getClockWise());
            pLevel.setBlock(blockpos, pState.setValue(PART, ChestType.LEFT), 3);
            pLevel.setBlock(pPos, pState.setValue(PART, ChestType.RIGHT), 3);
            pLevel.blockUpdated(pPos, Blocks.AIR);
            pState.updateNeighbourShapes(pLevel, pPos, 3);
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(PART).equals(ChestType.RIGHT) ? pState.getValue(FACING) : pState.getValue(FACING).getOpposite();
        return switch (direction) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_EAST;
            default -> SHAPE_WEST;
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide() && player instanceof ServerPlayer) {
            NetworkHooks.openScreen((ServerPlayer) player, 
                new SimpleMenuProvider((windowId, inv, p) -> 
                    new CrucibleOfTheCosmosMenu(windowId, inv, ContainerLevelAccess.create(world, pos), pos, world),
                    Component.translatable("container.crucible_of_the_cosmos")),
                pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide());
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        if (blockState.getValue(PART).equals(ChestType.RIGHT))
            return RenderShape.MODEL;
        else
            return RenderShape.INVISIBLE;
    }
    
    private static Direction getNeighbourDirection(ChestType pPart, Direction pDirection) {
        return pPart == ChestType.LEFT ? pDirection.getCounterClockWise() : pDirection.getClockWise();
    }
    
    public void playerWillDestroy(Level pLevel, BlockPos pos1, BlockState state1, Player pPlayer) {
        if (!pLevel.isClientSide) {
            ChestType half = state1.getValue(PART);
            BlockPos pos2 = pos1.relative(getNeighbourDirection(half, state1.getValue(FACING)));
            BlockState state2 = pLevel.getBlockState(pos2);
            if (state2.is(this) && state2.getValue(PART) != state1.getValue(PART)) {
                pLevel.setBlock(pos2, Blocks.AIR.defaultBlockState(), 35);
                pLevel.levelEvent(pPlayer, 2001, pos2, Block.getId(state2));
            }
        }
        super.playerWillDestroy(pLevel, pos1, state1, pPlayer);
    }
    
    @SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

}