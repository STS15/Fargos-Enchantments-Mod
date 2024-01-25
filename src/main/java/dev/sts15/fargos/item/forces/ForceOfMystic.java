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

public class ForceOfMystic extends Item implements ICurioItem {

    private final Random random = new Random();
	
	private static final ChatFormatting[] colors = { 
	        ChatFormatting.DARK_BLUE, ChatFormatting.GREEN, ChatFormatting.AQUA, 
	        ChatFormatting.DARK_PURPLE,ChatFormatting.LIGHT_PURPLE, ChatFormatting.YELLOW
	    };
	private static final Vector3f[] PARTICLE_COLORS = {
			new Vector3f(0.000F, 0.000F, 0.545F),
			new Vector3f(0.824F, 0.706F, 0.549F),
			new Vector3f(0.961F, 0.961F, 0.863F),
			new Vector3f(0.580F, 0.000F, 0.827F),
			new Vector3f(0.867F, 0.627F, 0.867F),
			new Vector3f(1.000F, 0.843F, 0.000F)
		};

	    private int colorIndex = 0;
	    private int tickCounter = 0;

	public ForceOfMystic() {
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
        stack.setHoverName(Component.literal("Force of Mystic").withStyle(colors[colorIndex]));
        colorIndex++;
    }

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.literal("Combines the passive abilities from some of the Mystical themed Enchantments").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.literal(" ").withStyle(ChatFormatting.GRAY));
	    tooltip.add(Component.literal("- Vampiric: Drains health from enemies to heal the player").withStyle(ChatFormatting.DARK_BLUE));
	    tooltip.add(Component.literal("- Enchanting: Reduces levels required for enchantments").withStyle(ChatFormatting.GREEN));
	    tooltip.add(Component.literal("- Librarian: Increases experience gained from all sources").withStyle(ChatFormatting.AQUA));
	    tooltip.add(Component.literal("- Witch: Witches throw good potions at you and applies blindness to attackers").withStyle(ChatFormatting.DARK_PURPLE));
	    tooltip.add(Component.literal("- Shulker: Grants temporary flight ability").withStyle(ChatFormatting.LIGHT_PURPLE));
	    tooltip.add(Component.literal("- Undying: Revives the player once upon death with full health").withStyle(ChatFormatting.YELLOW));
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
