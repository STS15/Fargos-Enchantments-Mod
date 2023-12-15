package dev.sts15.fargos.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import dev.sts15.fargos.item.enchantments.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = "fargos")
public class EventHandler {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Map<Player, Boolean> lastEnchantmentTableInteraction = new HashMap<>();
	
	private static final UUID IRON_ARMOR_MODIFIER_ID = UUID.fromString("a6dcd5b8-5f4f-4991-b1d1-2d033b76b5b1");
    private static final double IRON_ARMOR_MODIFIER_AMOUNT = 6.0; // Equivalent to Iron Chestplate
    private static final UUID DIAMOND_ARMOR_MODIFIER_ID = UUID.fromString("a6dcd5b8-5f4f-4991-b1d1-2d033b76b5b2");
    private static final double DIAMOND_ARMOR_MODIFIER_AMOUNT = 8.0; // Equivalent to Diamond Chestplate
    private static final UUID DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID = UUID.fromString("c6fcd5b8-5f4f-4991-b1d1-2d033b76b5b3");
    private static final double DIAMOND_ARMOR_TOUGHNESS_MODIFIER_AMOUNT = 2.0; // Equivalent to Diamond Toughness
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
	    Player player = event.player;
	    if (player.level.isClientSide || event.phase != TickEvent.Phase.END) {
	        return;
	    }
	    
	    if (player.tickCount % 3 == 0) { // Every 3 ticks
	        removeMarkedLapisFromInventory(player);
	        removeDroppedMarkedLapisNearPlayer(player);
	    }

	    ItemStack lapisEnchantmentItem = LapisEnchantment.getLapisEnchantmentItem(player);
	    if (!lapisEnchantmentItem.isEmpty()) {
	    	boolean isCurrentlyInteracting = player.containerMenu instanceof EnchantmentMenu;
		    boolean wasInteractingBefore = lastEnchantmentTableInteraction.getOrDefault(player, false);
		    if (isCurrentlyInteracting && !wasInteractingBefore) {
		        if (player.containerMenu instanceof EnchantmentMenu) {
		            EnchantmentMenu enchantmentMenu = (EnchantmentMenu) player.containerMenu;
		            addMarkedLapisToEnchantmentTable(enchantmentMenu, player);
		        }
		    } else if (!isCurrentlyInteracting && wasInteractingBefore) {
		        restoreOriginalLapisAmount(player);
		    }
		    lastEnchantmentTableInteraction.put(player, isCurrentlyInteracting);
	    }
	    
	    ItemStack ironEnchantmentItem = IronEnchantment.getIronEnchantmentItem(player);
	    if (!ironEnchantmentItem.isEmpty()) {
	    	applyIronArmorModifier(player);
	    } else {
	    	removeIronArmorModifier(player);
        }
	    
	    ItemStack diamondEnchantmentItem = DiamondEnchantment.getDiamondEnchantmentItem(player);
	    if (!diamondEnchantmentItem.isEmpty()) {
	    	applyDiamondArmorModifier(player);
	    } else {
	    	removeDiamondArmorModifier(player);
        }
	    
	    ItemStack goldEnchantmentItem = GoldEnchantment.getGoldEnchantmentItem(player);
	    if (!goldEnchantmentItem.isEmpty()) {
	   
	    } else {
            
        }
	    
	}
	
	@SubscribeEvent
	public static void onBlockBreak(BreakEvent event) {
	    Player player = event.getPlayer();
	    BlockState state = event.getState();

	    // Check if the block is stone and if the player has the Gold Enchantment
	    if (state.is(Blocks.STONE) && !GoldEnchantment.getGoldEnchantmentItem(player).isEmpty()) {
	        ServerLevel serverLevel = (ServerLevel) player.level;
	        Random random = new Random();

	        // 5% chance to drop gold nuggets
	        if (random.nextDouble() < 0.05) {
	            int nuggetCount = 2 + random.nextInt(2); // Drops 2-3 gold nuggets

	            // Drop the gold nuggets
	            for (int i = 0; i < nuggetCount; i++) {
	                ItemEntity goldNuggetEntity = new ItemEntity(
	                    serverLevel,
	                    event.getPos().getX() + 0.5,
	                    event.getPos().getY() + 0.5,
	                    event.getPos().getZ() + 0.5,
	                    new ItemStack(Items.GOLD_NUGGET)
	                );
	                serverLevel.addFreshEntity(goldNuggetEntity);
	            }
	        }
	    }
	}
	
    private static void applyIronArmorModifier(Player player) {
        AttributeModifier modifier = new AttributeModifier(IRON_ARMOR_MODIFIER_ID, "Iron Enchantment Armor", IRON_ARMOR_MODIFIER_AMOUNT, AttributeModifier.Operation.ADDITION);
        if (player.getAttribute(Attributes.ARMOR).getModifier(IRON_ARMOR_MODIFIER_ID) == null) {
            player.getAttribute(Attributes.ARMOR).addTransientModifier(modifier);
            System.out.println("Applied Iron Enchantment armor modifier.");
        }
    }

    private static void removeIronArmorModifier(Player player) {
        if (player.getAttribute(Attributes.ARMOR).getModifier(IRON_ARMOR_MODIFIER_ID) != null) {
            player.getAttribute(Attributes.ARMOR).removeModifier(IRON_ARMOR_MODIFIER_ID);
            System.out.println("Removed Iron Enchantment armor modifier.");
        }
    }
    
    private static void applyDiamondArmorModifier(Player player) {
        // Armor Modifier
        AttributeModifier armorModifier = new AttributeModifier(
            DIAMOND_ARMOR_MODIFIER_ID, "Diamond Enchantment Armor", DIAMOND_ARMOR_MODIFIER_AMOUNT, AttributeModifier.Operation.ADDITION
        );
        if (player.getAttribute(Attributes.ARMOR).getModifier(DIAMOND_ARMOR_MODIFIER_ID) == null) {
            player.getAttribute(Attributes.ARMOR).addTransientModifier(armorModifier);
        }

        // Armor Toughness Modifier
        AttributeModifier toughnessModifier = new AttributeModifier(
            DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID, "Diamond Enchantment Armor Toughness", DIAMOND_ARMOR_TOUGHNESS_MODIFIER_AMOUNT, AttributeModifier.Operation.ADDITION
        );
        if (player.getAttribute(Attributes.ARMOR_TOUGHNESS).getModifier(DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID) == null) {
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(toughnessModifier);
        }
    }

    private static void removeDiamondArmorModifier(Player player) {
        // Remove Armor Modifier
        if (player.getAttribute(Attributes.ARMOR).getModifier(DIAMOND_ARMOR_MODIFIER_ID) != null) {
            player.getAttribute(Attributes.ARMOR).removeModifier(DIAMOND_ARMOR_MODIFIER_ID);
        }

        // Remove Armor Toughness Modifier
        if (player.getAttribute(Attributes.ARMOR_TOUGHNESS).getModifier(DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID) != null) {
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID);
        }
    }

	private static void addMarkedLapisToEnchantmentTable(EnchantmentMenu enchantmentMenu, Player player) {
	    Slot lapisSlot = enchantmentMenu.getSlot(1); // Assuming slot 1 is for Lapis Lazuli
	    int originalLapisAmount = lapisSlot.getItem().getCount();
	    player.getPersistentData().putInt("OriginalLapisAmount", originalLapisAmount);

	    if (originalLapisAmount < 64) {
	        ItemStack lapisStack = new ItemStack(Items.LAPIS_LAZULI, 64);
	        CompoundTag nbt = new CompoundTag();
	        nbt.putBoolean("AddedByEnchantment", true);
	        lapisStack.setTag(nbt);
	        lapisSlot.set(lapisStack);
	    }
	}
	
	private static void removeDroppedMarkedLapisNearPlayer(Player player) {
	    AABB searchArea = new AABB(
	        player.getX() - 2, player.getY() - 2, player.getZ() - 2,
	        player.getX() + 2, player.getY() + 2, player.getZ() + 2
	    );

	    List<ItemEntity> lapisEntities = player.level.getEntitiesOfClass(
	        ItemEntity.class, searchArea,
	        entity -> entity.getItem().getItem() == Items.LAPIS_LAZULI && hasAddedByEnchantmentTag(entity.getItem())
	    );

	    for (ItemEntity lapisEntity : lapisEntities) {
	        lapisEntity.discard(); // Remove the entity
	    }
	}
	
	private static boolean hasAddedByEnchantmentTag(ItemStack stack) {
	    CompoundTag nbt = stack.getTag();
	    return nbt != null && nbt.contains("AddedByEnchantment") && nbt.getBoolean("AddedByEnchantment");
	}

	private static void restoreOriginalLapisAmount(Player player) {
	    if (player.containerMenu instanceof EnchantmentMenu) {
	        EnchantmentMenu enchantmentMenu = (EnchantmentMenu) player.containerMenu;
	        Slot lapisSlot = enchantmentMenu.getSlot(1); // Assuming slot 1 is for Lapis Lazuli
	        int originalLapisAmount = player.getPersistentData().getInt("OriginalLapisAmount");
	        if (lapisSlot.hasItem()) {
	            lapisSlot.getItem().setCount(originalLapisAmount);
	        }
	        player.getPersistentData().remove("OriginalLapisAmount");
	    }
	}

	private static void removeMarkedLapisFromInventory(Player player) {
	    Inventory playerInventory = player.getInventory();
	    for (int i = 0; i < playerInventory.getContainerSize(); i++) {
	        ItemStack stack = playerInventory.getItem(i);
	        if (stack.getItem() == Items.LAPIS_LAZULI && stack.hasTag()) {
	            CompoundTag nbt = stack.getTag();
	            if (nbt.contains("AddedByEnchantment") && nbt.getBoolean("AddedByEnchantment")) {
	                playerInventory.removeItem(stack);
	                break; // Remove only one stack at a time
	            }
	        }
	    }
	}

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().isProjectile()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (hasAmethystEnchantment(player)) {
                    event.setCanceled(true);
                }
            }
        } else if (event.getSource().getEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();
            checkAndApplyStaticChargeAttack(player, event);
            checkAndApplyEmeraldEnchantment(player, event);
        }
        
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!DiamondEnchantment.getDiamondEnchantmentItem(player).isEmpty()) {
                float newDamage = event.getAmount() * 0.66f;
                event.setAmount(newDamage);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void checkAndApplyStaticChargeAttack(Player player, LivingHurtEvent event) {
        CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof CopperEnchantment, player).ifPresent(triple -> {
            ItemStack stack = triple.getRight();
            CompoundTag nbt = stack.getTag();
            if (nbt != null && nbt.contains("StaticChargeLevel")) {
                int chargePoints = nbt.getInt("StaticChargeLevel");
                if (chargePoints > 0) {
                    int extraDamage = chargePoints / 100; // Assuming every 100 points add +1 damage
                    event.setAmount(event.getAmount() + extraDamage);

                    // Spawn electric spark particles
                    spawnElectricSparkParticles(event.getEntity(), player, chargePoints);

                    // Logging the damage and charge points
                    //System.out.println("Attacking with extra damage: " + extraDamage + ", Charge Points: " + chargePoints);

                    // Reset the accumulated charge points
                    nbt.putInt("StaticChargeLevel", 0);
                    stack.setTag(nbt);
                }
            }
        });
    }
    
    private static void checkAndApplyEmeraldEnchantment(Player player, LivingHurtEvent event) {
        CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof EmeraldEnchantment, player).ifPresent(triple -> {
            if (event.getEntity() instanceof AbstractIllager) {
                float extraDamage = 2.0F; // Set the extra damage amount
                float baseDamage = event.getAmount();
                float totalDamage = baseDamage + extraDamage;

                event.setAmount(totalDamage);

                // Log the damage details
                //System.out.println("Emerald Enchantment Attack: Base Damage = " + baseDamage + ", Extra Damage = " + extraDamage + ", Total Damage = " + totalDamage);

                // Optional: Spawn particles or other effects to signify the extra damage
            }
        });
    }

    private static void spawnElectricSparkParticles(Entity hitEntity, Player player, int chargePoints) {
        if (hitEntity.level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) hitEntity.level;

            double dx = player.getX() - hitEntity.getX();
            double dy = (player.getY() + player.getBbHeight() / 2.0) - (hitEntity.getY() + hitEntity.getBbHeight() / 2.0);
            double dz = player.getZ() - hitEntity.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            // Normalize the direction vector
            double dirX = dx / distance;
            double dirY = dy / distance;
            double dirZ = dz / distance;

            int particleAmount = chargePoints / 10; // Adjust particle amount based on charge points
            Random random = new Random();
            for (int i = 0; i < particleAmount; i++) {
                // Add randomness to the offset
                double offsetX = dirX * 0.5 + (random.nextDouble() - 0.5) * 0.2;
                double offsetY = dirY * 0.5 + (random.nextDouble() - 0.5) * 0.2;
                double offsetZ = dirZ * 0.5 + (random.nextDouble() - 0.5) * 0.2;

                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                                          hitEntity.getX() + offsetX, hitEntity.getY() + hitEntity.getBbHeight() / 2.0F + offsetY, hitEntity.getZ() + offsetZ,
                                          1, 0.1, 0.1, 0.1, 0.02);
            }

            // Play electric sound, volume proportional to charge (e.g., max charge = 1.0F volume)
            float volume = Math.min(1.0F, chargePoints / 1000.0F);
            serverLevel.playSound(null, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, volume, 1.0F);
        }
    }
    
    @SuppressWarnings("deprecation")
    private static boolean hasAmethystEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof AmathystEnchantment, player).isPresent();
    }

}
