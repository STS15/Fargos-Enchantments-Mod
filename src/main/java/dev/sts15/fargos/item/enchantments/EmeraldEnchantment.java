package dev.sts15.fargos.item.enchantments;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.level.BlockEvent;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

import com.mojang.math.Vector3f;

public class EmeraldEnchantment extends Item implements ICurioItem {

    private static final long ACTIVE_ABILITY_COOLDOWN = 15000; // 15 seconds
    private final Random random = new Random();
    private long lastActiveAbilityTime = 0;

    public EmeraldEnchantment() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        Level level = player.level;
        long currentTime = System.currentTimeMillis();

        if (!level.isClientSide) {
            if (currentTime - lastActiveAbilityTime >= ACTIVE_ABILITY_COOLDOWN) {
                lastActiveAbilityTime = currentTime;
                applyBonemealEffect(player, level);
            }
            spawnEmeraldParticles(player);
        }
    }

    private void applyBonemealEffect(Player player, Level level) {
        RandomSource randomSource = level.random;

        BlockPos.betweenClosedStream(player.blockPosition().offset(-2, -1, -2), player.blockPosition().offset(2, 1, 2))
                .forEach(pos -> {
                    BlockState blockState = level.getBlockState(pos);
                    if (isBonemealableCrop(blockState)) {
                        BonemealableBlock bonemealable = (BonemealableBlock) blockState.getBlock();
                        if (bonemealable.isValidBonemealTarget(level, pos, blockState, level.isClientSide)) {
                            if (level instanceof ServerLevel) {
                                if (bonemealable.isBonemealSuccess(level, randomSource, pos, blockState)) {
                                    bonemealable.performBonemeal((ServerLevel) level, randomSource, pos, blockState);
                                }
                            }
                        }
                    }
                });
    }
    
    private boolean isBonemealableCrop(BlockState blockState) {
        Block block = blockState.getBlock();
        return block instanceof BonemealableBlock && block != Blocks.GRASS_BLOCK && block != Blocks.TALL_GRASS && block != Blocks.GRASS;
    }


    private void spawnEmeraldParticles(Player player) {
        ServerLevel serverLevel = (ServerLevel) player.level;
        ParticleOptions particleOptions = new DustParticleOptions(new Vector3f(0.0F, 1.0F, 0.0F), 1.0F); // Emerald green
        double radius = 0.5;
        double offsetY = 0.8;
        for (int i = 0; i < 1; i++) { // Increase particle count
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
    
    public static ItemStack getEmeraldEnchantmentItem(Player player) {
        AtomicReference<ItemStack> emeraldEnchantmentItem = new AtomicReference<>(ItemStack.EMPTY);

        CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof EmeraldEnchantment, player)
            .ifPresent(triple -> emeraldEnchantmentItem.set(triple.getRight()));

        return emeraldEnchantmentItem.get();
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Passive: Extra damage to Pillager/Illager").withStyle(ChatFormatting.GRAY));

        // Active Ability Description
        tooltip.add(Component.literal("Passive: Provides Bonemeal effect to nearby crops (15-second cooldown)").withStyle(ChatFormatting.GRAY));
        
//        // Cooldown Information
//        long currentTime = System.currentTimeMillis();
//        long timeSinceLastUse = currentTime - lastActiveAbilityTime;
//        long cooldownRemaining = Math.max(0, ACTIVE_ABILITY_COOLDOWN - timeSinceLastUse);
//        
//        if (cooldownRemaining > 0) {
//            int secondsRemaining = (int) (cooldownRemaining / 1000);
//            tooltip.add(Component.literal("Cooldown: " + secondsRemaining + " seconds remaining").withStyle(ChatFormatting.RED));
//        } else {
//            tooltip.add(Component.literal("Ability ready to use").withStyle(ChatFormatting.GREEN));
//        }
    }

}
