package dev.sts15.fargos.item.weapons;

import java.util.List;

import javax.annotation.Nullable;

import dev.sts15.fargos.init.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class StyxGazer extends SwordItem {

    public StyxGazer(Item.Properties properties) {
    	super(new StyxTier(), 3, -2.4F, properties);
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Work in Progress- recipe will change with future updates").withStyle(ChatFormatting.RED));
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {}

    public static class StyxTier implements Tier {
    	@Override
        public int getUses() {
            return Integer.MAX_VALUE; // Infinite durability
        }

        @Override
        public float getSpeed() {
            return 8.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 15.0F; // 15 attack damage
        }

        @Override
        public int getLevel() {
            return 3; // Mining level
        }

        @Override
        public int getEnchantmentValue() {
            return 10;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ItemInit.ABOMINABLE_ENERGY.get());
        }
        
        
    }
}