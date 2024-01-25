package dev.sts15.fargos.screen.crucibleofthecosmos;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import dev.sts15.fargos.blocks.crucibleofthecosmos.CrucibleOfTheCosmosRecipeRegistry;
import dev.sts15.fargos.init.BlockInit;
import dev.sts15.fargos.init.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.CraftingContainer;

public class CrucibleOfTheCosmosMenu extends AbstractContainerMenu {
    private final Container craftingSlots = new CraftingContainer(this, 4, 4); // 4x4 Grid
    private final ResultContainer resultSlots = new ResultContainer();
    private final Level level;
    private final BlockPos blockEntityPosition;

    public CrucibleOfTheCosmosMenu(int windowId, Inventory playerInventory, ContainerLevelAccess access, BlockPos pos, Level level) {
        super(ModMenus.CRUCIBLE_OF_THE_COSMOS_MENU.get(), windowId);
        this.blockEntityPosition = pos;
        this.level = level;

        // Crafting grid
        int startX = 24;
        int startY = 23;
        int slotSizeWithGap = 18 + 1;
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {
                int x = startX + col * slotSizeWithGap;
                int y = startY + row * slotSizeWithGap;
                this.addSlot(new Slot(craftingSlots, col + row * 4, x, y));
            }
        }

        // Result slot
        this.addSlot(new Slot(resultSlots, 0, 137, 52) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player thePlayer, ItemStack stack) {
                consumeCraftingItems();
                updateCraftingResult(); // Re-check the crafting recipe
                super.onTake(thePlayer, stack);
            }

        });


        // Player inventory
        int playerInventoryStartX = 8;
        int playerInventoryStartY = 104;
        int hotbarY = playerInventoryStartY + 58;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, 9 + col + row * 9, playerInventoryStartX + col * 18, playerInventoryStartY + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, playerInventoryStartX + col * 18, hotbarY));
        }

    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            if (index >= 0 && index <= 15) {
                if (!this.moveItemStackTo(stackInSlot, 17, 53, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 16) {
                if (!this.moveItemStackTo(stackInSlot, 17, 53, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stackInSlot, itemstack);
                consumeCraftingItems();
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return itemstack;
    }


    private void consumeCraftingItems() {
        for (int i = 0; i < 16; i++) {
            ItemStack itemInSlot = this.craftingSlots.getItem(i);
            if (!itemInSlot.isEmpty()) {
                itemInSlot.shrink(1);
                if (itemInSlot.getCount() == 0) {
                    this.craftingSlots.setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }



    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntityPosition), player, BlockInit.CRUCIBLE_OF_THE_COSMOS.get());
    }
    
    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level.isClientSide) {
            clearContainer(player, this.craftingSlots);
        }
    }

    public void clearContainer(Player player, Container container) {
        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemstack = container.removeItemNoUpdate(i);
            if (!itemstack.isEmpty()) {
                player.drop(itemstack, false);
            }
        }
    }
    
    @Override
    public void slotsChanged(Container inventory) {
        super.slotsChanged(inventory);
        if (inventory == this.craftingSlots) {
            updateCraftingResult();
        }
    }

    private void updateCraftingResult() {
        NonNullList<ItemStack> itemsInGrid = NonNullList.withSize(16, ItemStack.EMPTY);
        for (int i = 0; i < itemsInGrid.size(); i++) {
            itemsInGrid.set(i, this.craftingSlots.getItem(i));
        }
        ItemStack result = CrucibleOfTheCosmosRecipeRegistry.getOutput(itemsInGrid);
        this.resultSlots.setItem(0, result);
    }


}
