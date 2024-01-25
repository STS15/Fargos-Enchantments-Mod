package dev.sts15.fargos.item.weapons;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

public class SwordOfTheFates extends SwordItem {

    public SwordOfTheFates(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, Integer.MAX_VALUE, attackSpeed, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            Random random = new Random();
            int roll = random.nextInt(6) + 1; // Generates a number between 1 and 6

            if (roll >= 4) {
                // Rolled 4, 5, or 6 - damage the target
                player.sendSystemMessage(Component.literal("You rolled a " + roll + " to damage the target!"));
                target.hurt(DamageSource.GENERIC, Integer.MAX_VALUE);
            } else {
                // Rolled 1, 2, or 3 - damage the player
                player.sendSystemMessage(Component.literal("You rolled a " + roll + " to damage yourself!"));
                player.hurt(DamageSource.GENERIC, Integer.MAX_VALUE);
            }
            return true;
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Sword will 50/50 do integer max damage to the target or the player"));
    }
}
