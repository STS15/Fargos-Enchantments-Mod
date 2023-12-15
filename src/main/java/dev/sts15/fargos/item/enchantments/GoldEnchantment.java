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

public class GoldEnchantment extends Item implements ICurioItem {

    private static final long HERO_COOLDOWN = 5 * 60 * 1000; // 5 minutes in milliseconds
    private static final int HERO_DURATION = 30 * 20; // 30 seconds in game ticks
    private long lastHeroActivation = 0;
    private final Random random = new Random();

    public GoldEnchantment() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        long currentTime = System.currentTimeMillis();

        if (!player.level.isClientSide()) {
            spawnGoldParticles(player);
            if (currentTime - lastHeroActivation >= HERO_COOLDOWN) {
                // Active ability: Hero of the Village effect for 30 seconds
                player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, HERO_DURATION, 0));
                lastHeroActivation = currentTime;
            }

            // Passive ability: Gold Armor mimic for Piglins and stone dropping gold nuggets
            // Note: You'll need to implement the logic for Piglin interaction and stone drops separately.
        }
    }
    
    public static ItemStack getGoldEnchantmentItem(Player player) {
        AtomicReference<ItemStack> goldEnchantmentItem = new AtomicReference<>(ItemStack.EMPTY);

        CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof GoldEnchantment, player)
            .ifPresent(triple -> goldEnchantmentItem.set(triple.getRight()));

        return goldEnchantmentItem.get();
    }

    private void spawnGoldParticles(Player player) {
        ServerLevel serverLevel = (ServerLevel) player.level;
        ParticleOptions particleOptions = new DustParticleOptions(new Vector3f(1.0F, 0.84F, 0.0F), 1.0F); // Gold-like color
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
        tooltip.add(Component.literal("Passive: Piglin damage negated, stone may drop gold nuggets").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Active: Grants Hero of the Village effect for 30 seconds (5-minute cooldown)").withStyle(ChatFormatting.GRAY));
        long currentTime = System.currentTimeMillis();
        long cooldownRemaining = Math.max(0, (lastHeroActivation + HERO_COOLDOWN) - currentTime);
        int secondsRemaining = (int) (cooldownRemaining / 1000);
        if (secondsRemaining > 0) {
            tooltip.add(Component.literal("Cooldown: " + secondsRemaining + " seconds remaining.").withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.literal("Ability ready to use.").withStyle(ChatFormatting.GREEN));
        }
    }
}
