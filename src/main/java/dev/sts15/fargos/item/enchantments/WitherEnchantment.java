package dev.sts15.fargos.item.enchantments;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.math.Vector3f;

public class WitherEnchantment extends Item implements ICurioItem {
	
	private final Random random = new Random();

    public WitherEnchantment() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if (!player.level.isClientSide()) {
            removeWitherEffect(player);
            spawnStationaryBlackParticles(player);
        }
    }

    private void removeWitherEffect(Player player) {
        if (player.hasEffect(MobEffects.WITHER)) {
            player.removeEffect(MobEffects.WITHER);
        }
    }
    
    private void spawnStationaryBlackParticles(Player player) {
        if (player.isOnGround()) {
            ServerLevel serverLevel = (ServerLevel) player.level;
            ParticleOptions particleOptions = new DustParticleOptions(new Vector3f(0.0F, 0.0F, 0.0F), 1.0F); // Purple color
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
        tooltip.add(Component.literal("Passive: Negates Wither effect").withStyle(ChatFormatting.GRAY));
    }
}
