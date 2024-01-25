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

    private static final long COOLDOWN_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds
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
            spawnStationaryPurpleParticles(player);
        }
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
    }

}
