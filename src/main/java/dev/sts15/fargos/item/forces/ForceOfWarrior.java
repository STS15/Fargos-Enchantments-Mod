package dev.sts15.fargos.item.forces;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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

import dev.sts15.fargos.init.FargosConfig;

public class ForceOfWarrior extends Item implements ICurioItem {

    private final Random random = new Random();
	
	private static final ChatFormatting[] colors = { 
	        ChatFormatting.DARK_GREEN, ChatFormatting.GREEN, ChatFormatting.DARK_GRAY, 
	        ChatFormatting.RED,ChatFormatting.WHITE
	    };
	private static final Vector3f[] PARTICLE_COLORS = {
			new Vector3f(0.000F, 0.392F, 0.000F),
			new Vector3f(0.000F, 0.502F, 0.000F),
			new Vector3f(0.663F, 0.663F, 0.663F),
			new Vector3f(0.545F, 0.000F, 0.000F),
			new Vector3f(1.000F, 1.000F, 1.000F)
		};

	    private int colorIndex = 0;
	    private int tickCounter = 0;

	public ForceOfWarrior() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }
	
	@Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isClientSide() && entity instanceof Player) {
            if (++tickCounter >= 10) {
                updateItemName(stack);
                tickCounter = 0;
            }
        }
    }
	
	@Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        spawnStationaryForceParticles(player);
	}

    private void updateItemName(ItemStack stack) {
        if (colorIndex >= colors.length) {
            colorIndex = 0;
        }
        stack.setHoverName(Component.literal("Force of Warrior").withStyle(colors[colorIndex]));
        colorIndex++;
    }

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.literal("Combines the passive abilities from some of the Damage themed Enchantments").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.literal(" ").withStyle(ChatFormatting.GRAY));
	    tooltip.add(Component.literal("- Battle: Grants temporary invincibility after taking damage").withStyle(ChatFormatting.DARK_GREEN));
	    tooltip.add(Component.literal("- Cactus: Reflects a portion of damage back to the attacker").withStyle(ChatFormatting.GREEN));
	    tooltip.add(Component.literal("- Void: Offers a small chance to avoid lethal damage and negates falling into the void").withStyle(ChatFormatting.DARK_GRAY));
	    tooltip.add(Component.literal("- Thorny: Negate Sweet Berry Bush damage, regen while standing on leaves").withStyle(ChatFormatting.RED));
	    tooltip.add(Component.literal("- Iron Golem: Gives the player additional health").withStyle(ChatFormatting.WHITE));
	}

	private void spawnStationaryForceParticles(Player player) {
		if (player.isOnGround() && !player.level.isClientSide) {
			ServerLevel serverLevel = (ServerLevel) player.level;
	        double radius = 0.5;
	        double offsetY = 0.8;

	        // Randomly select a color
	        Vector3f color = PARTICLE_COLORS[random.nextInt(PARTICLE_COLORS.length)];
	        ParticleOptions particleOptions = new DustParticleOptions(color, 1.0F);

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

}
