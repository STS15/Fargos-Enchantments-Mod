package dev.sts15.fargos.item.forces;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.mojang.math.Vector3f;

import dev.sts15.fargos.init.FargosConfig;

public class ForceOfOverworld extends Item implements ICurioItem {
	
	private long lastRepairTime = 0;
    private long nextRepairInterval = 5000;
    private final Random random = new Random();
    private static final long ACTIVE_ABILITY_COOLDOWN = 15000; // 15 seconds
    private long lastActiveAbilityTime = 0;
	
	private static final ChatFormatting[] colors = { 
	        ChatFormatting.DARK_PURPLE, ChatFormatting.RED, ChatFormatting.AQUA, 
	        ChatFormatting.GREEN, ChatFormatting.BLUE, ChatFormatting.YELLOW 
	    };
	private static final Vector3f[] PARTICLE_COLORS = {
		    new Vector3f(1.0F, 0.0F, 0.0F),    // Red
		    new Vector3f(1.0F, 0.0F, 1.0F),    // Magenta
		    new Vector3f(0.0F, 1.0F, 1.0F),    // Cyan
		    new Vector3f(0.0F, 1.0F, 0.0F),    // Green
		    new Vector3f(1.0F, 0.84F, 0.0F),   // Gold
		    new Vector3f(0.0F, 0.0F, 1.0F)     // Blue
		};

	    private int colorIndex = 0;
	    private int tickCounter = 0;

	public ForceOfOverworld() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }
	
	@Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        if (FargosConfig.getConfigValue("gold_enchantment")) {
        	CompoundTag nbt = stack.getOrCreateTag();
            nbt.putBoolean("minecraft:piglin_loved", true);
        }
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
        long currentTime = System.currentTimeMillis();
        if (FargosConfig.getConfigValue("redstone_enchantment")) {
        	repairArmorOverTime(player, currentTime);
        }
        Level level = player.level;

        if (!level.isClientSide) {
            if (currentTime - lastActiveAbilityTime >= ACTIVE_ABILITY_COOLDOWN) {
                lastActiveAbilityTime = currentTime;
                applyBonemealEffect(player, level);
            }
        }
            spawnStationaryForceParticles(player);
         
	}

    private void updateItemName(ItemStack stack) {
        if (colorIndex >= colors.length) {
            colorIndex = 0;
        }
        stack.setHoverName(Component.literal("Force of Overworld").withStyle(colors[colorIndex]));
        colorIndex++;
    }

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.literal("Combines the passive abilities from some of the Overworld themed Enchantments").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.literal(" ").withStyle(ChatFormatting.GRAY));
	    tooltip.add(Component.literal("- Amethyst: Negates all projectile damage").withStyle(ChatFormatting.DARK_PURPLE));
	    tooltip.add(Component.literal("- Redstone: Repairs armor slowly over time").withStyle(ChatFormatting.RED));
	    tooltip.add(Component.literal("- Diamond: Adds Armor and Armor Toughness, ignores 1/5th of all damage").withStyle(ChatFormatting.AQUA));
	    tooltip.add(Component.literal("- Emerald: Extra damage to Pillagers/Illagers").withStyle(ChatFormatting.GREEN));
	    tooltip.add(Component.literal("- Emerald: Apply bonemeal to nearby crops").withStyle(ChatFormatting.GREEN));
	    tooltip.add(Component.literal("- Lapis: Increased XP gain, auto-fills Enchantment Tables with Lapis").withStyle(ChatFormatting.BLUE));
	    tooltip.add(Component.literal("- Gold: Piglins ignore you, stone may drop gold nuggets").withStyle(ChatFormatting.YELLOW));
	}
	
	private void repairArmorOverTime(Player player, long currentTime) {
        if (currentTime - lastRepairTime > nextRepairInterval) {
            player.getInventory().armor.forEach(this::repairSingleArmorItem);
            lastRepairTime = currentTime;
            nextRepairInterval = 5000 + random.nextInt(2500);
        }
    }
	private void repairSingleArmorItem(ItemStack armorItem) {
        if (armorItem.isDamaged()) {
            armorItem.setDamageValue(armorItem.getDamageValue() - 1);
        }
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

	private void spawnParticle(ServerLevel serverLevel, ParticleOptions particleType, double x, double y, double z) {
	    double offsetX = random.nextGaussian() * 0.2;
	    double offsetY = random.nextGaussian() * 0.2;
	    double offsetZ = random.nextGaussian() * 0.2;
	    serverLevel.sendParticles(particleType, x + offsetX, y + offsetY, z + offsetZ, 1, 0, 0, 0, 0);
	}

}
