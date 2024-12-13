package dev.sts15.fargos.jei;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;

import java.util.List;

public class CrucibleOfTheCosmosRecipe {
	private final NonNullList<ItemStack> ingredients;
    private final ItemStack output;

    public CrucibleOfTheCosmosRecipe(NonNullList<ItemStack> ingredients, ItemStack output) {
        this.ingredients = ingredients;
        this.output = output;
    }

    public boolean matches(NonNullList<ItemStack> inputs) {
    	if (inputs.size() != ingredients.size()) {
            return false;
        }

        for (int i = 0; i < inputs.size(); i++) {
            ItemStack inputStack = inputs.get(i);
            ItemStack ingredientStack = ingredients.get(i);
            if (!ItemStack.isSame(inputStack, ingredientStack)) {
                return false;
            }

            if ((inputStack.getItem() instanceof AxeItem ||
            	 inputStack.getItem() instanceof PickaxeItem ||
            	 inputStack.getItem() instanceof ShieldItem ||
                 inputStack.getItem() instanceof ArmorItem || 
                 inputStack.getItem() instanceof SwordItem) &&
                !inputStack.isEnchanted() && 
                inputStack.isDamageableItem() && 
                inputStack.getDamageValue() == 0) {
                continue;
            } else if (inputStack.getItem() instanceof EnchantedBookItem) {
                continue;
            } else if (!inputStack.isEnchanted() && inputStack.isDamageableItem() && inputStack.getDamageValue() != 0) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getOutput() {
        return output;
    }

    public NonNullList<ItemStack> getIngredients() {
        return ingredients;
    }
}
