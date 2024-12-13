package dev.sts15.fargos.item.souls;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import net.minecraft.world.item.CreativeModeTab;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import dev.sts15.fargos.init.FargosConfig;

public class SoulOfSupersonic extends Item implements ICurioItem {

    private static final UUID WALK_SPEED_MODIFIER_ID = UUID.fromString("4eb1e391-9559-42fc-9914-8f39b440a31c");
    private static final float WALK_SPEED_BOOST = 3;
    private static final float FLY_SPEED_BOOST = 5;

    public SoulOfSupersonic() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {

    }
    
    @Override
    public void onEquip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        //System.out.println("Supersonic equipped");
        Entity entity = slotContext.entity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            CompoundTag playerData = player.getPersistentData();

            // Walking speed boost
            if (FargosConfig.getConfigValue((ServerPlayer) player,"soul_of_supersonic-walking")) {
                AttributeModifier modifier = new AttributeModifier(WALK_SPEED_MODIFIER_ID, "Supersonic walking speed boost", WALK_SPEED_BOOST, AttributeModifier.Operation.MULTIPLY_TOTAL);
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(modifier);
            }

            // Flying speed boost
            if (FargosConfig.getConfigValue(player,"soul_of_supersonic-flying")) {
                player.getAbilities().setFlyingSpeed(player.getAbilities().getFlyingSpeed() * FLY_SPEED_BOOST);
                playerData.putBoolean("SoulOfSupersonicFlyingSpeedBoost", true); // Set flag
            }
            player.onUpdateAbilities();
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        //System.out.println("Supersonic unequipped");
        Entity entity = slotContext.entity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            CompoundTag playerData = player.getPersistentData();

            // Remove walking speed boost
            if (player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(WALK_SPEED_MODIFIER_ID) != null) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(WALK_SPEED_MODIFIER_ID);
            }

            // Remove flying speed boost if it was applied
            if (playerData.getBoolean("SoulOfSupersonicFlyingSpeedBoost")) {
                player.getAbilities().setFlyingSpeed(player.getAbilities().getFlyingSpeed() / FLY_SPEED_BOOST);
                playerData.remove("SoulOfSupersonicFlyingSpeedBoost"); // Clear flag
            }
            player.onUpdateAbilities();
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Grants supersonic speed for running and flying.").withStyle(ChatFormatting.GRAY));
    }
}
