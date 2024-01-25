package dev.sts15.fargos.item.souls;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

public class SoulOfColossus extends Item implements ICurioItem {
    
    private static final double EXTRA_ARMOR = 10.0;
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("c96dbe8b-aa78-4f56-b8b6-1b0f36b3baf3");
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("d96dbe8b-aa78-4f56-b8b6-1b0f36b3baf3"); // Unique identifier for armor modifier

    public SoulOfColossus() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }
    
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            removeNegativeEffects(player);
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            modifyHealth(player, 2.0, HEALTH_MODIFIER_UUID);
            modifyAttribute(player, Attributes.ARMOR, EXTRA_ARMOR, Operation.ADDITION, ARMOR_MODIFIER_UUID);
        }
    }

    private void modifyHealth(Player player, double multiplier, UUID modifierId) {
        AttributeInstance healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute != null && healthAttribute.getModifier(modifierId) == null) {
            double baseHealth = healthAttribute.getBaseValue();
            AttributeModifier healthModifier = new AttributeModifier(modifierId, "SoulOfColossusHealthModifier", baseHealth * (multiplier - 1), Operation.ADDITION);
            healthAttribute.addPermanentModifier(healthModifier);
            player.setHealth((float)healthAttribute.getValue());
        }
    }

    private void modifyAttribute(Player player, Attribute attribute, double amount, Operation operation, UUID modifierId) {
        AttributeInstance attr = player.getAttribute(attribute);
        if (attr != null && attr.getModifier(modifierId) == null) {
            AttributeModifier modifier = new AttributeModifier(modifierId, "SoulOfColossusModifier", amount, operation);
            attr.addPermanentModifier(modifier);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            restoreHealth(player, HEALTH_MODIFIER_UUID);
            removeAttributeModifier(player, Attributes.ARMOR, ARMOR_MODIFIER_UUID);
        }
    }

    private void restoreHealth(Player player, UUID modifierId) {
        AttributeInstance healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute != null) {
            AttributeModifier modifier = healthAttribute.getModifier(modifierId);
            if (modifier != null) {
                healthAttribute.removeModifier(modifierId);
                float healthPercentage = player.getHealth() / (float) (healthAttribute.getValue() + modifier.getAmount());
                player.setHealth(healthPercentage * (float) healthAttribute.getValue());
            }
        }
    }

    private void removeAttributeModifier(Player player, Attribute attribute, UUID modifierId) {
        AttributeInstance attr = player.getAttribute(attribute);
        if (attr != null) {
            attr.removeModifier(modifierId);
        }
    }
    
    private void removeNegativeEffects(Player player) {
        for (MobEffectInstance effect : player.getActiveEffects()) {
            if (!effect.getEffect().isBeneficial()) {
                player.removeEffect(effect.getEffect());
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Very powerful defensive charm that provides 2x health, priority aggression(WIP), and immunity to all negative debuffs.").withStyle(ChatFormatting.GRAY));
    }
}
