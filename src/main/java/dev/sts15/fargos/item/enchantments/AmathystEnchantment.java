package dev.sts15.fargos.item.enchantments;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import javax.annotation.Nullable;

import com.mojang.math.Vector3f;
import dev.sts15.fargos.Fargos;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AmathystEnchantment extends Item implements ICurioItem {

    private static final long COOLDOWN_DURATION = 1 * 60 * 1000; // 5 minutes in milliseconds
    private static final long BOOST_DURATION = 30 * 1000; // 30 seconds in milliseconds
    private static final long PARTICLE_EFFECT_DURATION = 2 * 1000; // 2 seconds in milliseconds
    private final Random random = new Random();
    private long lastAbilityActivationTime = 0;

    public AmathystEnchantment() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        long currentTime = System.currentTimeMillis();

        if (!player.level.isClientSide()) {
            if (currentTime - lastAbilityActivationTime <= PARTICLE_EFFECT_DURATION) {
                spawnActiveAbilityParticles(player);
            }
            spawnStationaryPurpleParticles(player);
            checkAndRestoreEnchantments(player);
            activateBoostAbility(player);
        }
    }

    private void activateBoostAbility(Player player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAbilityActivationTime > COOLDOWN_DURATION) {
        	player.addEffect(new MobEffectInstance(Fargos.COOLDOWN_EFFECT.get(), 5 * 60 * 20, 0, false, false, true));
            boostAndSaveAllEnchantedItems(player);
            player.getPersistentData().putLong("AmethystBoostExpiry", currentTime + BOOST_DURATION);
            lastAbilityActivationTime = currentTime;
        }
    }

    private void boostAndSaveAllEnchantedItems(Player player) {
        Iterable<ItemStack> allItems = player.getAllSlots();
        for (ItemStack itemStack : allItems) {
            if (!itemStack.isEmpty() && itemStack.isEnchanted()) {
            	upgradeItem(itemStack);
                saveOriginalEnchantments(itemStack);
                boostEnchantments(itemStack);
            }
        }
    }

    
    private void saveOriginalEnchantments(ItemStack itemStack) {
        if (itemStack.isEnchanted()) {
            Map<Enchantment, Integer> originalEnchantments = EnchantmentHelper.getEnchantments(itemStack);
            ListTag enchantmentList = new ListTag();
            originalEnchantments.forEach((enchantment, level) -> {
                CompoundTag enchantmentTag = new CompoundTag();
                enchantmentTag.putString("id", String.valueOf(EnchantmentHelper.getEnchantmentId(enchantment)));
                enchantmentTag.putInt("lvl", level);
                enchantmentList.add(enchantmentTag);
            });
            CompoundTag itemTag = itemStack.getOrCreateTag();
            itemTag.put("OriginalEnchantments", enchantmentList);
        }
    }

    private void boostEnchantments(ItemStack itemStack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        enchantments.replaceAll((enchantment, level) -> level + 1); // Increase each enchantment level by 1
        EnchantmentHelper.setEnchantments(enchantments, itemStack);
    }

    // Call this method regularly, e.g., in curioTick, to check and restore original enchantments
    public void checkAndRestoreEnchantments(Player player) {
        long currentTime = System.currentTimeMillis();
        long boostExpiryTime = player.getPersistentData().getLong("AmethystBoostExpiry");

        if (currentTime > boostExpiryTime) {
            restoreOriginalEnchantments(player);
            player.getPersistentData().remove("AmethystBoostExpiry"); // Remove the tag after restoring enchantments
        }
    }


    private void restoreOriginalEnchantments(Player player) {
        Iterable<ItemStack> allItems = player.getAllSlots();
        for (ItemStack itemStack : allItems) {
            if (!itemStack.isEmpty() && itemStack.getTag() != null && itemStack.getTag().contains("OriginalEnchantments", 9)) {
                restoreEnchantmentsFromTag(itemStack);
                resetUpgrade(itemStack);
            }
        }
    }
    
    private void restoreEnchantmentsFromTag(ItemStack itemStack) {
        if (itemStack.getTag() != null && itemStack.getTag().contains("OriginalEnchantments", 9)) { // 9 is the tag type for List
            ListTag originalEnchantmentsList = itemStack.getTag().getList("OriginalEnchantments", 10); // 10 is the tag type for Compound
            Map<Enchantment, Integer> originalEnchantments = EnchantmentHelper.deserializeEnchantments(originalEnchantmentsList);
            EnchantmentHelper.setEnchantments(originalEnchantments, itemStack);
            itemStack.removeTagKey("OriginalEnchantments");
        }
    }


    private void spawnActiveAbilityParticles(Player player) {
        ServerLevel serverLevel = (ServerLevel) player.level;
        // Particle spawning logic
    }
    
    @Override
    public boolean isRepairable(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.getBoolean("IsUpgraded")) {
            return false; // Not repairable if it's upgraded
        }
        return super.isRepairable(stack); // Repairable otherwise
    }

    private void spawnStationaryPurpleParticles(Player player) {
        if (player.isOnGround()) {
            ServerLevel serverLevel = (ServerLevel) player.level;
            ParticleOptions particleOptions = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 1.0F), 1.0F); // Purple color
            double radius = 0.5;
            double offsetY = 0.8;
            for (int i = 0; i < 1; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double offsetX = radius * Math.cos(angle);
                double offsetZ = radius * Math.sin(angle);
                spawnParticle(serverLevel, particleOptions, player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ);
            }
        }
    }

    private void spawnParticle(ServerLevel serverLevel, ParticleOptions particleType, double x, double y, double z) {
        double offsetX = random.nextGaussian() * 0.2;
        double offsetY = random.nextGaussian() * 0.2;
        double offsetZ = random.nextGaussian() * 0.2;
        serverLevel.sendParticles(particleType, x + offsetX, y + offsetY, z + offsetZ, 1, 0, 0, 0, 0);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Passive: Negates incoming projectile damage").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Active: Temporarily boosts enchantment levels for armor and equipped tool (5-minute cooldown)").withStyle(ChatFormatting.GRAY));
        
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.getBoolean("IsUpgraded")) {
            tooltip.add(Component.literal("Cannot be used in an anvil while upgraded").withStyle(ChatFormatting.RED));
        }

        long currentTime = System.currentTimeMillis();
        long cooldownRemaining = Math.max(0, (lastAbilityActivationTime + COOLDOWN_DURATION) - currentTime);
        int secondsRemaining = (int) (cooldownRemaining / 1000);
        if (secondsRemaining > 0) {
            tooltip.add(Component.literal("Cooldown: " + secondsRemaining + " seconds remaining.").withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.literal("Ability ready to use.").withStyle(ChatFormatting.GREEN));
        }
    }
    public void resetUpgrade(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putBoolean("IsUpgraded", false);
        stack.setTag(nbt);
        // Handle other logic for resetting the upgrade here
    }
    public void upgradeItem(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putBoolean("IsUpgraded", true);
        stack.setTag(nbt);
        // Handle other upgrading logic here
    }
    
    
}
