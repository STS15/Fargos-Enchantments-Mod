package dev.sts15.fargos.item.forces;

import net.minecraft.server.level.ServerPlayer;
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

public class ForceOfRejectors extends Item implements ICurioItem {

    private final Random random = new Random();
	
	private static final ChatFormatting[] colors = { 
	        ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_GREEN, ChatFormatting.YELLOW, 
	        ChatFormatting.GREEN,ChatFormatting.DARK_GRAY, ChatFormatting.WHITE, ChatFormatting.GRAY
	    };
	private static final Vector3f[] PARTICLE_COLORS = {
			new Vector3f(1.0F, 0.753F, 0.796F),
			new Vector3f(0.565F, 0.698F, 0.429F),
			new Vector3f(1.0F, 0.549F, 0.0F),
			new Vector3f(0.980F, 0.980F, 0.980F),
			new Vector3f(0.467F, 0.675F, 0.188F),
			new Vector3f(0.941F, 0.941F, 0.941F),
			new Vector3f(0.412F, 0.412F, 0.412F)
		};

	    private int colorIndex = 0;
	    private int tickCounter = 0;

	public ForceOfRejectors() {
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
	
	private void removeWitherEffect(Player player) {
        if (player.hasEffect(MobEffects.WITHER) && FargosConfig.getConfigValue((ServerPlayer) player,"wither_enchantment")) {
            player.removeEffect(MobEffects.WITHER);
        }
    }
	
	@Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        spawnStationaryForceParticles(player);
        removeWitherEffect(player);
	}

    private void updateItemName(ItemStack stack) {
        if (colorIndex >= colors.length) {
            colorIndex = 0;
        }
        stack.setHoverName(Component.literal("Force of Rejectors").withStyle(colors[colorIndex]));
        colorIndex++;
    }

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.literal("Combines the passive abilities from some of the Hostile themed Enchantments").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.literal(" ").withStyle(ChatFormatting.GRAY));
	    tooltip.add(Component.literal("- Dragon: Negates magic damage").withStyle(ChatFormatting.LIGHT_PURPLE));
	    tooltip.add(Component.literal("- Wither: Negates wither debuff").withStyle(ChatFormatting.GRAY));
	    tooltip.add(Component.literal("- Zombie: Temporarily increases attack speed after not attacking for a while").withStyle(ChatFormatting.DARK_GREEN));
	    tooltip.add(Component.literal("- Blaze: Nearby enemies are ignited and take 25% more damage").withStyle(ChatFormatting.RED));
	    tooltip.add(Component.literal("- Skeleton: Double arrow velocity and increased arrow damage").withStyle(ChatFormatting.WHITE));
	    tooltip.add(Component.literal("- Creeper: Explosive jump, and player explodes when hit").withStyle(ChatFormatting.GREEN));
	    tooltip.add(Component.literal("- Ghast: Grants dash ability on initial run").withStyle(ChatFormatting.WHITE));
	    tooltip.add(Component.literal("- Vindicator: Boosts next attack damage after switching weapons").withStyle(ChatFormatting.GRAY));
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
