package dev.sts15.fargos.item.enchantments;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;

import com.mojang.math.Vector3f;

import dev.sts15.fargos.Fargos;

import java.util.List;
import java.util.Random;

public class RedstoneEnchantment extends Item implements ICurioItem {

    private long lastRepairTime = 0;
    private long lastActivationTime = 0;
    private long nextRepairInterval = 5000;
    private final Random random = new Random();
    private long lastAbilityActivationTime = 0;
    private static final long PARTICLE_EFFECT_DURATION = 2 * 1000; // 2 seconds in milliseconds

    public RedstoneEnchantment() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }
    
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        long currentTime = System.currentTimeMillis();

        repairArmorOverTime(player, currentTime);
        checkAndActivateBoostAbility(player, currentTime);

        if (!player.level.isClientSide()) {
            if (currentTime - lastAbilityActivationTime <= PARTICLE_EFFECT_DURATION) {
                spawnActiveAbilityParticles(player);
            }
            spawnStationaryRedParticles(player);
        }
    }
    
    private void repairArmorOverTime(Player player, long currentTime) {
        if (currentTime - lastRepairTime > nextRepairInterval) {
            player.getInventory().armor.forEach(this::repairSingleArmorItem);
            lastRepairTime = currentTime;
            nextRepairInterval = 5000 + random.nextInt(2500);
        }
    }

    private void checkAndActivateBoostAbility(Player player, long currentTime) {
        if (isBoostAbilityActivated(player) && (currentTime - lastActivationTime > 60000)) {
            activateBoostAbility(player);
            lastActivationTime = currentTime;
        }
    }
    
    private void spawnActiveAbilityParticles(Player player) {
        if (player.level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) player.level;

            double x = player.getX();
            double y = player.getY() + 1; // Particle height slightly above the ground
            double z = player.getZ();
            
            int numberOfParticles = 30; // Total number of particles in the burst

            for (int i = 0; i < numberOfParticles; i++) {
                double offsetX = random.nextGaussian() * 0.2;
                double offsetY = random.nextGaussian() * 0.2;
                double offsetZ = random.nextGaussian() * 0.2;

                serverLevel.sendParticles(ParticleTypes.CRIMSON_SPORE, x + offsetX, y + offsetY, z + offsetZ, 1, 0, 0, 0, 0);
            }
        }
    }

    
    private void spawnStationaryRedParticles(Player player) {
        if (player.isOnGround()) {
            ServerLevel serverLevel = (ServerLevel) player.level;
            ParticleOptions particleOptions = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);
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

    private void repairSingleArmorItem(ItemStack armorItem) {
        if (armorItem.isDamaged()) {
            armorItem.setDamageValue(armorItem.getDamageValue() - 1);
        }
    }

    private boolean isBoostAbilityActivated(Player player) {
        return player.getHealth() < player.getMaxHealth() * 0.5;
    }

    private void activateBoostAbility(Player player) {
    	if (!player.hasEffect(Fargos.COOLDOWN_EFFECT.get())) {
            // Apply the boost effects
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1)); // 10 seconds speed boost
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 200, 1)); // 10 seconds jump boost
            player.playSound(SoundEvents.TOTEM_USE, 0.8F, 1.0F);

            // Apply the custom cooldown effect for 1 minute
            player.addEffect(new MobEffectInstance(Fargos.COOLDOWN_EFFECT.get(), 60 * 20, 0, false, false, true));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Passive: Repairs armor slowly over time").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Active: Boosts speed and jump when health is low (1-minute cooldown).").withStyle(ChatFormatting.GRAY));
        long currentTime = System.currentTimeMillis();
        long cooldownRemaining = Math.max(0, (lastActivationTime + 60000) - currentTime);
        int secondsRemaining = (int) (cooldownRemaining / 1000);
        if (secondsRemaining > 0) {
            tooltip.add(Component.literal("Cooldown: " + secondsRemaining + " seconds remaining.").withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.literal("Ability ready to use.").withStyle(ChatFormatting.GREEN));
        }
    }
}
