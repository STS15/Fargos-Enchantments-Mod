package dev.sts15.fargos.item.souls;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.mojang.math.Vector3f;

import dev.sts15.fargos.init.FargosConfig;

public class SoulOfMinecraft extends Item implements ICurioItem {

    private final Random random = new Random();
	
    private static final ChatFormatting[] RAINBOW_COLORS = {
		    ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW,
		    ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE,
		    ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_PURPLE
		};
	private static final Vector3f[] PARTICLE_COLORS = {
			new Vector3f(0.000F, 0.000F, 0.000F)
	};

	    private int colorIndex = 0;
	    private int tickCounter = 0;
	    private int tooltipIndex = 0;
	    private long lastTooltipUpdateTime = 0;

	public SoulOfMinecraft() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }
	
	@Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTooltipUpdateTime > 1500) {
            tooltipIndex = (tooltipIndex + 1) % 22;
            lastTooltipUpdateTime = currentTime;
        }
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
        if (!player.level.isClientSide()) {
            provideInfiniteAir(player);
        }
	}

	private void provideInfiniteAir(Player player) {
        if (player.isUnderWater() && FargosConfig.getConfigValue("water_enchantment")) {
            player.setAirSupply(player.getMaxAirSupply());
        }
    }

		private void updateItemName(ItemStack stack) {
		    String itemName = "Soul of Minecraft";
		    StringBuilder coloredName = new StringBuilder();

		    for (int i = 0; i < itemName.length(); i++) {
		        ChatFormatting color = RAINBOW_COLORS[i % RAINBOW_COLORS.length];
		        coloredName.append(color.toString());
		        coloredName.append(itemName.charAt(i));
		    }

		    stack.setHoverName(Component.literal(coloredName.toString()));
		}
		
		@Override
	    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
	        List<Component> allTooltips = getAllTooltips();
	        tooltip.add(Component.literal("Combines the abilities from all of the Forces").withStyle(ChatFormatting.GRAY));
	        tooltip.add(Component.literal("----").withStyle(ChatFormatting.GRAY));
	        for (int i = 0; i < 4; i++) {
	            tooltip.add(allTooltips.get((tooltipIndex + i) % allTooltips.size()));
	        }
	    }

	    private List<Component> getAllTooltips() {
	        List<Component> tooltips = new ArrayList<>();
		    tooltips.add(Component.literal("- Architect: Increases block placement range").withStyle(ChatFormatting.YELLOW));
		    tooltips.add(Component.literal("- Enderman: Prevents Endermen aggression").withStyle(ChatFormatting.DARK_PURPLE));
		    tooltips.add(Component.literal("- Arctic: Grants resistance to freezing effects").withStyle(ChatFormatting.WHITE));
		    tooltips.add(Component.literal("- Spectral: Makes phantoms less likely to attack the player").withStyle(ChatFormatting.GRAY));
		    tooltips.add(Component.literal("- Glowstone: Automatically lights up nearby dark areas").withStyle(ChatFormatting.GOLD));
		    tooltips.add(Component.literal("- Vampiric: Drains health from enemies to heal the player").withStyle(ChatFormatting.DARK_BLUE));
		    tooltips.add(Component.literal("- Enchanting: Reduces levels required for enchantments").withStyle(ChatFormatting.GREEN));
		    tooltips.add(Component.literal("- Librarian: Increases experience gained from all sources").withStyle(ChatFormatting.AQUA));
		    tooltips.add(Component.literal("- Witch: Witches throw good potions at you and applies blindness to attackers").withStyle(ChatFormatting.DARK_PURPLE));
		    tooltips.add(Component.literal("- Shulker: Grants temporary flight ability").withStyle(ChatFormatting.LIGHT_PURPLE));
		    tooltips.add(Component.literal("- Undying: Revives the player once upon death with full health").withStyle(ChatFormatting.YELLOW));
		    tooltips.add(Component.literal("- Earth: Converts dirt to grass with empty hand").withStyle(ChatFormatting.DARK_GREEN));
		    tooltips.add(Component.literal("- Water: Provides unlimited air underwater").withStyle(ChatFormatting.BLUE));
		    tooltips.add(Component.literal("- Fire: Negates all damage from fire sources").withStyle(ChatFormatting.RED));
		    tooltips.add(Component.literal("- Air: Negates all fall damage").withStyle(ChatFormatting.WHITE));
		    tooltips.add(Component.literal("- Amethyst: Negates all projectile damage").withStyle(ChatFormatting.DARK_PURPLE));
		    tooltips.add(Component.literal("- Redstone: Repairs armor slowly over time").withStyle(ChatFormatting.RED));
		    tooltips.add(Component.literal("- Diamond: Adds Armor and Armor Toughness, ignores 1/5th of all damage").withStyle(ChatFormatting.AQUA));
		    tooltips.add(Component.literal("- Emerald: Extra damage to Pillagers/Illagers").withStyle(ChatFormatting.GREEN));
		    tooltips.add(Component.literal("- Emerald: Apply bonemeal to nearby crops").withStyle(ChatFormatting.GREEN));
		    tooltips.add(Component.literal("- Lapis: Increased XP gain, auto-fills Enchantment Tables with Lapis").withStyle(ChatFormatting.BLUE));
		    tooltips.add(Component.literal("- Gold: Piglins ignore you, stone may drop gold nuggets").withStyle(ChatFormatting.YELLOW));
		    tooltips.add(Component.literal("- Dragon: Negates magic damage").withStyle(ChatFormatting.LIGHT_PURPLE));
		    tooltips.add(Component.literal("- Wither: Negates wither debuff").withStyle(ChatFormatting.GRAY));
		    tooltips.add(Component.literal("- Zombie: Temporarily increases attack speed after not attacking for a while").withStyle(ChatFormatting.DARK_GREEN));
		    tooltips.add(Component.literal("- Blaze: Nearby enemies are ignited and take 25% more damage").withStyle(ChatFormatting.RED));
		    tooltips.add(Component.literal("- Skeleton: Double arrow velocity and increased arrow damage").withStyle(ChatFormatting.WHITE));
		    tooltips.add(Component.literal("- Creeper: Explosive jump, and player explodes when hit").withStyle(ChatFormatting.GREEN));
		    tooltips.add(Component.literal("- Ghast: Grants dash ability on initial run").withStyle(ChatFormatting.WHITE));
		    tooltips.add(Component.literal("- Vindicator: Boosts next attack damage after switching weapons").withStyle(ChatFormatting.GRAY));
		    tooltips.add(Component.literal("- Battle: Grants temporary invincibility after taking damage").withStyle(ChatFormatting.DARK_GREEN));
		    tooltips.add(Component.literal("- Cactus: Reflects a portion of damage back to the attacker").withStyle(ChatFormatting.GREEN));
		    tooltips.add(Component.literal("- Void: Offers a small chance to avoid lethal damage and negates falling into the void").withStyle(ChatFormatting.DARK_GRAY));
		    tooltips.add(Component.literal("- Thorny: Negate Sweet Berry Bush damage, regen while standing on leaves").withStyle(ChatFormatting.RED));
		    tooltips.add(Component.literal("- Iron Golem: Gives the player additional health").withStyle(ChatFormatting.WHITE));
	        return tooltips;
	    }	


	private void spawnStationaryForceParticles(Player player) {
		if (player.isOnGround() && !player.level.isClientSide) {
			ServerLevel serverLevel = (ServerLevel) player.level;
	        double radius = 0.3;
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
