package dev.sts15.fargos.item.enchantments;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import javax.annotation.Nullable;

import com.mojang.math.Vector3f;
import dev.sts15.fargos.Fargos;
import java.util.List;
import java.util.Random;

public class CopperEnchantment extends Item implements ICurioItem {

    private static final long COOLDOWN_DURATION = 2 * 60 * 1000; // 1 minute in milliseconds
    private static final float HEALTH_THRESHOLD_FOR_DISCHARGE = 0.3f; // 30% health
    private final Random random = new Random();
    private long lastAbilityActivationTime = 0;
    private static final int STATIC_CHARGE_THRESHOLD = 1000; // Threshold for static charge accumulation
    private static final int CHARGE_PER_20_BLOCKS = 50;
    private static final double BLOCKS_MOVED_THRESHOLD = 20.0;
    private double lastX = 0.0;
    private double lastZ = 0.0;

    public CopperEnchantment() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();

        if (!player.level.isClientSide()) {
            spawnCopperParticles(player);
            checkAndActivateStaticCharge(player, stack);
            checkAndActivateElectricDischarge(player);
        }
    }

    private void checkAndActivateStaticCharge(Player player, ItemStack stack) {
        double movedX = Math.abs(player.getX() - lastX);
        double movedZ = Math.abs(player.getZ() - lastZ);
        CompoundTag nbt = stack.getOrCreateTag();

        int currentCharge = nbt.getInt("StaticChargeLevel");

        if (movedX + movedZ >= BLOCKS_MOVED_THRESHOLD) {
            currentCharge += CHARGE_PER_20_BLOCKS;
            lastX = player.getX();
            lastZ = player.getZ();
            // Ensure we don't exceed the maximum charge
            currentCharge = Math.min(currentCharge, STATIC_CHARGE_THRESHOLD);
            nbt.putInt("StaticChargeLevel", currentCharge);
            stack.setTag(nbt);
            //System.out.println("Increased charge to " + currentCharge);
        }
    }

    private void checkAndActivateElectricDischarge(Player player) {
        if (player.getHealth() / player.getMaxHealth() < HEALTH_THRESHOLD_FOR_DISCHARGE) {
            activateElectricDischarge(player);
        }
    }

    private void activateElectricDischarge(Player player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAbilityActivationTime > COOLDOWN_DURATION) {
            AABB area = new AABB(player.getX() - 3, player.getY() - 3, player.getZ() - 3, 
                                 player.getX() + 3, player.getY() + 3, player.getZ() + 3);
            List<Entity> nearbyEntities = player.level.getEntities(player, area, e -> e instanceof LivingEntity && !isTamedPet(e));

            for (Entity entity : nearbyEntities) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).hurt(DamageSource.MAGIC, 8.0F); // Apply 8 damage
                }
            }
            
            player.addEffect(new MobEffectInstance(Fargos.COOLDOWN_EFFECT.get(), 2 * 60 * 20, 0, false, false, true));

            lastAbilityActivationTime = currentTime;
            
         // Spawn a lightning bolt at the player's location
            if (!player.level.isClientSide()) {
                Level level = player.level;
                LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
                if (lightningBolt != null) {
                    lightningBolt.moveTo(player.getX(), player.getY(), player.getZ());
                    lightningBolt.setVisualOnly(true); // This makes the lightning bolt visual only, causing no damage
                    level.addFreshEntity(lightningBolt);
                }
            }

            // Play totem pop sound
            //player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

            // Create a disc of electrical spark particles around the player
            spawnElectricSparkParticleDisc(player);
        }
    }
    
    private void spawnElectricSparkParticleDisc(Player player) {
        if (player.level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) player.level;
            Random random = new Random();

            for (int i = 0; i < 360; i += 5) { // Loop through angles in 5-degree increments
                double radian = Math.toRadians(i);
                double radius = 3.0; // Radius of the disc
                double x = player.getX() + Math.cos(radian) * radius;
                double z = player.getZ() + Math.sin(radian) * radius;
                double y = player.getY() + player.getBbHeight() / 2.0;

                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0, 0, 0, 0.02);
            }
        }
    }
    
    private boolean isTamedPet(Entity entity) {
        return (entity instanceof Wolf && ((Wolf) entity).isTame()) || 
               (entity instanceof Cat && ((Cat) entity).isTame());
    }

    private void spawnCopperParticles(Player player) {
        ServerLevel serverLevel = (ServerLevel) player.level;
        ParticleOptions particleOptions = new DustParticleOptions(new Vector3f(0.8F, 0.5F, 0.2F), 1.0F); // Copper color
        double radius = 0.5;
        double offsetY = 0.8;
        for (int i = 0; i < 1; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double offsetX = radius * Math.cos(angle);
            double offsetZ = radius * Math.sin(angle);
            spawnParticle(serverLevel, particleOptions, player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ);
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
        tooltip.add(Component.literal("Passive: Accumulates charge from movement for extra damage").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Active: Emits energy in all directions when health is low (2-minute cooldown)").withStyle(ChatFormatting.GRAY));
        
        int chargeLevel = 0;
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("StaticChargeLevel")) {
            chargeLevel = nbt.getInt("StaticChargeLevel");
        }
        tooltip.add(Component.literal("Charge: " + chargeLevel + " / 1000").withStyle(ChatFormatting.BLUE));
        
        long currentTime = System.currentTimeMillis();
        long cooldownRemaining = Math.max(0, (lastAbilityActivationTime + COOLDOWN_DURATION) - currentTime);
        int secondsRemaining = (int) (cooldownRemaining / 1000);
        if (secondsRemaining > 0) {
            tooltip.add(Component.literal("Cooldown: " + secondsRemaining + " seconds remaining.").withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.literal("Ability ready to use.").withStyle(ChatFormatting.GREEN));
        }
    }
    
    private void updateItemStackWithChargeLevel(ItemStack stack, int chargeLevel) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putInt("StaticChargeLevel", chargeLevel);
        stack.setTag(nbt);
    }

}
