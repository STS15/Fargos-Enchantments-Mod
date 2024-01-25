package dev.sts15.fargos.item.enchantments;

import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import javax.annotation.Nullable;

import com.mojang.math.Vector3f;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class DiamondEnchantment extends Item implements ICurioItem {

    private static final long RESISTANCE_COOLDOWN = 2 * 60 * 1000;
    private static final int RESISTANCE_DURATION = 60 * 20;
    private long lastResistanceActivation = 0;
    private final Random random = new Random();

    public DiamondEnchantment() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        long currentTime = System.currentTimeMillis();

        if (!player.level.isClientSide()) {
            spawnDiamondParticles(player);

//            if (currentTime - lastResistanceActivation >= RESISTANCE_COOLDOWN) {
//                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, RESISTANCE_DURATION, 1));
//                lastResistanceActivation = currentTime;
//            }
        }
    }

    private void spawnDiamondParticles(Player player) {
        ServerLevel serverLevel = (ServerLevel) player.level;
        ParticleOptions particleOptions = new DustParticleOptions(new Vector3f(0.0F, 1.0F, 1.0F), 1.0F);
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
        tooltip.add(Component.literal("Passive: Adds armor, armor toughness, and ignores 1/5th of all damage").withStyle(ChatFormatting.GRAY));
//        tooltip.add(Component.literal("Active: Adds resistance 2 for 1 minute (2-minute cooldown)").withStyle(ChatFormatting.GRAY));
//        long currentTime = System.currentTimeMillis();
//        long cooldownRemaining = Math.max(0, (lastResistanceActivation + 120000) - currentTime);
//        int secondsRemaining = (int) (cooldownRemaining / 1000);
//        if (secondsRemaining > 0) {
//            tooltip.add(Component.literal("Cooldown: " + secondsRemaining + " seconds remaining.").withStyle(ChatFormatting.RED));
//        } else {
//            tooltip.add(Component.literal("Ability ready to use.").withStyle(ChatFormatting.GREEN));
//        }
    }
}
