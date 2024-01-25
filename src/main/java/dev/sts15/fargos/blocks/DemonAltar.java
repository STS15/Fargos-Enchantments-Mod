package dev.sts15.fargos.blocks;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DemonAltar extends Block {
	
	private static final double RADIUS = 24.0;
    private BlockPos altarPosition = null;
    private final Map<BlockPos, BlockState> changedBlocks = new HashMap<>();


    public DemonAltar(BlockBehaviour.Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public void onBlockPlaced(Level world, BlockPos pos) {
        this.altarPosition = pos;
    }
    
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
        System.out.println("Demon Altar placed at: " + pos);
        this.altarPosition = pos;
    }

 // ...

    @SubscribeEvent
    public void onBlockChange(BlockEvent event) {
        if (altarPosition == null) {
            return;
        }
        
        LevelAccessor levelAccessor = event.getLevel();
        if (levelAccessor instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) levelAccessor;
            BlockPos eventPos = event.getPos();
            double distanceSquared = altarPosition.distSqr(eventPos);

            if (distanceSquared <= RADIUS * RADIUS) {
                if (event instanceof BlockEvent.EntityPlaceEvent) {
                    BlockEvent.EntityPlaceEvent placeEvent = (BlockEvent.EntityPlaceEvent) event;
                    if (placeEvent.getEntity() instanceof Player) {
                        Player player = (Player) placeEvent.getEntity();
                        BlockState placedState = placeEvent.getPlacedBlock();

                        if (placedState.getFluidState().isEmpty()) {
                            // It's a regular block, not a fluid
                            System.out.println("Player " + player.getName().getString() + 
                                               " placed a block in Mutant Arena at " + eventPos);
                            changedBlocks.put(eventPos, placedState);
                        } else {
                            // It's a fluid block
                            System.out.println("Player " + player.getName().getString() + 
                                               " placed a fluid in Mutant Arena at " + eventPos);
                            changedBlocks.put(eventPos, Blocks.AIR.defaultBlockState()); // Replace fluid with air
                            handleFluidFlow(serverLevel, eventPos); // Handle fluid flow
                        }
                    }
                } else if (event instanceof BlockEvent.BreakEvent) {
                    System.out.println("Player broke block in Mutant Arena at " + eventPos);
                    if (changedBlocks.containsKey(eventPos)) {
                        changedBlocks.remove(eventPos);
                    }
                }
            }
        }
    }

    // ...

    private void handleFluidFlow(Level world, BlockPos pos) {
        System.out.println("Handling fluid flow at " + pos);
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = world.getBlockState(neighborPos);

            if (neighborState.getFluidState().isEmpty() && neighborState.getBlock() == Blocks.WATER) {
                // Replace flowing water with air
                System.out.println("Replacing flowing water with air at " + neighborPos);
                world.setBlock(neighborPos, Blocks.AIR.defaultBlockState(), 3);
                handleFluidFlow(world, neighborPos); // Recursively handle fluid flow
            }
        }
    }

    // ...

    
    @SubscribeEvent
    public void onBlockUpdate(BlockEvent.NeighborNotifyEvent event) {
        LevelAccessor levelAccessor = event.getLevel();
        BlockPos pos = event.getPos();

        // Ensure that we're working in a server-level context
        if (levelAccessor instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) levelAccessor;

            if (serverLevel.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) {
                boolean isLavaNearby = hasLavaNearby(serverLevel, pos);
                boolean isWaterNearby = hasWaterNearby(serverLevel, pos);

                if (isLavaNearby && isWaterNearby) {
                    // Lava turned into Obsidian
                    System.out.println("Lava turned into Obsidian at " + pos);
                    changedBlocks.put(pos, serverLevel.getBlockState(pos));
                }
            }
        }
    }

    private boolean hasLavaNearby(Level world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (world.getBlockState(pos.relative(direction)).getMaterial() == Material.LAVA) {
                return true;
            }
        }
        return false;
    }

    private boolean hasWaterNearby(Level world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (world.getBlockState(pos.relative(direction)).getMaterial() == Material.WATER) {
                return true;
            }
        }
        return false;
    }

    private void summonBoss(Level world, BlockPos pos) {
        // Summon the boss (fargos:mutant)
        // Hide the altar
        // Handle block destruction with loot drops
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (!changedBlocks.isEmpty()) {
                revertBlocks(world);
                changedBlocks.clear();
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    private void revertBlocks(Level world) {
        Set<BlockPos> processedObsidian = new HashSet<>();
        for (Map.Entry<BlockPos, BlockState> entry : changedBlocks.entrySet()) {
            BlockPos blockPos = entry.getKey();
            BlockState state = entry.getValue();

            if (state.getBlock() == Blocks.OBSIDIAN && blockPos.getY() == altarPosition.getY() - 2) {
            	revertBlockAndNeighbors(world, blockPos, processedObsidian);
            } else {
                dropBlockAsItem(world, altarPosition, state);
                world.removeBlock(blockPos, false);
            }
        }
        changedBlocks.clear();
    }

    private void revertBlockAndNeighbors(Level world, BlockPos pos, Set<BlockPos> processedBlocks) {
        BlockState state = world.getBlockState(pos);
        if (processedBlocks.contains(pos) || state.getBlock() != Blocks.OBSIDIAN) {
            return;
        }
        processedBlocks.add(pos);
        if (pos.getY() == altarPosition.getY() - 2) {
            world.setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
        }
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            revertBlockAndNeighbors(world, neighborPos, processedBlocks);
        }
    }


    private void replaceWithLava(Level world, BlockPos blockPos, BlockState state) {
        if (state.getBlock() != Blocks.OBSIDIAN) {
            dropBlockAsItem(world, blockPos, state);
        }
        world.setBlock(blockPos, Blocks.LAVA.defaultBlockState(), 3);
    }

    private void revertAndDropItem(Level world, BlockPos blockPos, BlockState state) {
        dropBlockAsItem(world, blockPos, state);
        world.removeBlock(blockPos, false); // Remove the block
    }

    private void dropBlockAsItem(Level world, BlockPos dropPosition, BlockState state) {
        if (!world.isClientSide) {
            ItemStack itemStack = new ItemStack(state.getBlock().asItem());
            if (!itemStack.isEmpty()) {
                double x = dropPosition.getX() + 0.5;
                double y = dropPosition.getY() + 1.0;
                double z = dropPosition.getZ() + 0.5;
                ItemEntity itemEntity = new ItemEntity(world, x, y, z, itemStack);
                world.addFreshEntity(itemEntity);
            }
        }
    }
    
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        if (!state.is(newState.getBlock())) {
            changedBlocks.clear();
        }
    }


}

