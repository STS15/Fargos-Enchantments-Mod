package dev.sts15.fargos.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.effect.EnchantmentUtils;
import dev.sts15.fargos.entity.custom.*;
import dev.sts15.fargos.init.EffectsInit;
import dev.sts15.fargos.init.FargosConfig;
import dev.sts15.fargos.init.ModEntities;
import dev.sts15.fargos.item.enchantments.*;
import dev.sts15.fargos.item.forces.*;
import dev.sts15.fargos.item.souls.SoulOfFlightMastery;
import dev.sts15.fargos.item.souls.SoulOfMinecraft;
import dev.sts15.fargos.item.weapons.StyxGazer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import top.theillusivec4.curios.api.CuriosApi;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod.EventBusSubscriber(modid = "fargos")
public class EventHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger("Fargos");
	
	private static final UUID IRON_ARMOR_MODIFIER_ID = UUID.fromString("a6dcd5b8-5f4f-4991-b1d1-2d033b76b5b1");
    private static final double IRON_ARMOR_MODIFIER_AMOUNT = 6.0;
    private static final UUID DIAMOND_ARMOR_MODIFIER_ID = UUID.fromString("a6dcd5b8-5f4f-4991-b1d1-2d033b76b5b2");
    private static final double DIAMOND_ARMOR_MODIFIER_AMOUNT = 8.0;
    private static final UUID DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID = UUID.fromString("c6fcd5b8-5f4f-4991-b1d1-2d033b76b5b3");
    private static final double DIAMOND_ARMOR_TOUGHNESS_MODIFIER_AMOUNT = 2.0;
    private static final UUID IRON_GOLEM_HEALTH_BOOST_ID = UUID.fromString("a6dcd5b8-5f4f-4991-b1d1-2d033b76b5b1");

    private static final Map<UUID, Long> lastMovementTime = new HashMap<>();
    private static final Map<UUID, BlockPos> lastPositions = new HashMap<>();
    private static final Map<UUID, Boolean> wasWalking = new HashMap<>();
    private static final Map<UUID, Long> lastDashTimes = new HashMap<>();
    private static final Map<UUID, Long> lastWeaponSwitchTimes = new HashMap<>();
    private static final Map<UUID, ItemStack> lastHeldItems = new HashMap<>();
    private static final Map<UUID, Boolean> canBoostAttack = new HashMap<>();
    private static final Map<UUID, Long> lastBoostTimes = new HashMap<>();
    private static final Map<UUID, Long> lastAttackTimes = new HashMap<>();
    private static final Map<UUID, AttributeModifier> attackSpeedModifiers = new HashMap<>();
    private static final Map<UUID, Long> boostActivationTimes = new HashMap<>();
    private static final Map<UUID, Long> lastStyxGazerUseTimes = new HashMap<>();
    private static Map<UUID, Long> undyingCooldowns = new HashMap<>();
    private static final Map<UUID, Integer> invinciblePlayers = new HashMap<>();
    private static final UUID REACH_MODIFIER_ID = UUID.randomUUID();
    private static LinkedList<BlockPos> lastTorchPositions = new LinkedList<>();
    private static final Map<Player, Boolean> lastEnchantmentTableInteraction = new HashMap<>();
    
    private static final long INCREASE_ATTACK_SPEED_THRESHOLD = 1200;
    private static final double ATTACK_SPEED_BOOST = 0.5;
    private static final long BOOST_COOLDOWN = 60;
    private static final long STILL_TIME_THRESHOLD = 3000;
    private static final int UNDYING_COOLDOWN = 12000;
    
//    @SubscribeEvent
//    public static void onLivingDeath(LivingDeathEvent event) {
//        if (event.getEntity() instanceof Player) {
//            Player player = (Player) event.getEntity();
//            Level world = player.level;
//
//            for (MutantEntity boss : world.getEntitiesOfClass(MutantEntity.class, player.getBoundingBox().inflate(15))) {
//                if (player.distanceToSqr(boss) <= 225) {
//                    boss.heal(boss.getMaxHealth() * 0.1f);
//                    Component message = Component.literal(boss.getName().getString() + " has healed 10% of its health!");
//                    AABB broadcastArea = boss.getBoundingBox().inflate(30);
//                    for (Player nearbyPlayer : world.getEntitiesOfClass(Player.class, broadcastArea)) {
//                        nearbyPlayer.sendSystemMessage(message);
//                    }
//                    LOGGER.info("Player {} died near the boss {} at [x={}, y={}, z={}].", player.getName().getString(), boss.getName().getString(), player.getX(), player.getY(), player.getZ());
//                }
//            }
//        }
//    }
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
	    Player player = event.player;
	    UUID playerUUID = player.getUUID();
	    long currentTime = player.level.getGameTime();
	    if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {

	        if (hasArchitectEnchantment(player) || (hasForceOfExplorer(player) && FargosConfig.getConfigValue(player, "architect_enchantment"))) {
	        	 EnchantmentUtils.setPlayerReach(REACH_MODIFIER_ID, player, 59.0);
            } else {
                EnchantmentUtils.removePlayerReach(REACH_MODIFIER_ID, player);
            }
	    }
	    if (player.level.isClientSide || event.phase != TickEvent.Phase.END) {
	        return;
	    }
	    EntityType<DeathSickleEntity> deathSickleEntityType = ModEntities.DEATH_SICKLE.get();
	    
	    if (player.swinging && player.getMainHandItem().getItem() instanceof StyxGazer) {
	    	
	    	Long lastUseTime = lastStyxGazerUseTimes.getOrDefault(playerUUID, 0L);
	        if (currentTime - lastUseTime >= 100) {
	            lastStyxGazerUseTimes.put(playerUUID, currentTime);
	            double circleRadius = 4.0;
		        for (int i = 0; i < 5; i++) {
		            double angle = 2 * Math.PI * i / 5;
		            double dx = circleRadius * Math.sin(angle);
		            double dz = circleRadius * Math.cos(angle);
		            DeathSickleEntity sickle = new DeathSickleEntity(deathSickleEntityType, player.level);
		            sickle.setPlayerToCircle(player, angle);
		            sickle.setLifespan(100);
		            sickle.moveTo(player.getX() + dx, player.getY() - 0.5, player.getZ() + dz, 0.0F, 0.0F);
		            player.level.addFreshEntity(sickle);
		        }
	        }
	    }

	    trackWeaponSwitch(player, playerUUID);
	    tryVoidTick(player.level, player);
	    
	    if (player.tickCount % 3 == 0) {
	        removeMarkedLapisFromInventory(player);
	        removeDroppedMarkedLapisNearPlayer(player);
	    }
	    
	    if (hasGlowstoneEnchantment(player) || (hasForceOfExplorer(player) && FargosConfig.getConfigValue(player,"glowstone_enchantment"))) {
	    	Level world = player.level;
	        BlockPos playerPos = player.blockPosition();
	        BlockPos blockBelowPos = playerPos.below();
	        if (world.getBrightness(LightLayer.BLOCK, playerPos) < 5 && world.isEmptyBlock(playerPos) && isPlaceable(world, blockBelowPos) && player.isOnGround()) {
	            if (lastTorchPositions.size() >= 3) {
	                BlockPos oldPos = lastTorchPositions.removeFirst();
	                if (world.getBlockState(oldPos).is(Blocks.TORCH)) {
	                    world.removeBlock(oldPos, false);
	                }
	            }

	            world.setBlock(playerPos, Blocks.TORCH.defaultBlockState(), 3);
	            lastTorchPositions.addLast(playerPos);
	        }
	    }
	    
	    if ((hasThornyEnchantment(player) || (hasForceOfWarrior(player) && FargosConfig.getConfigValue(player,"thorny_enchantment")))) {
	        BlockPos belowPlayer = player.blockPosition().below();
	        BlockState blockBelow = player.level.getBlockState(belowPlayer);
	        if (blockBelow.getBlock() instanceof LeavesBlock) {
	            player.heal(0.1F);
	        }
	    }

	    if (hasLapisEnchantment(player)) {
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
	    
	    if (!hasGhastEnchantment(player) && lastDashTimes.containsKey(playerUUID)) {
            lastDashTimes.remove(playerUUID);
        }
	    
	    if (hasIronEnchantment(player)) {
	    	applyIronArmorModifier(player);
	    } else {
	    	removeIronArmorModifier(player);
        }

	    if (hasDiamondEnchantment(player)) {
	    	applyDiamondArmorModifier(player);
	    } else {
	    	removeDiamondArmorModifier(player);
        }
	    
	    if (hasBlazeEnchantment(player) || (hasForceOfRejectors(player) && FargosConfig.getConfigValue(player,"blaze_enchantment"))) { // Add force here
            int radius = 4;
            AABB area = player.getBoundingBox().inflate(radius);
            List<Monster> mobs = player.level.getEntitiesOfClass(Monster.class, area);
            for (Monster mob : mobs) {
            		if (canSee(player, mob)) {
                        mob.setSecondsOnFire(5);
                    }
            }
        }
	    
	    if (hasMooshroomEnchantment(player)) { // Add force here
            if (isPlayerStill(player)) {
                if (!player.hasEffect(MobEffects.INVISIBILITY) || player.getEffect(MobEffects.INVISIBILITY).getDuration() < 60) {
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 80, 0, false, false, true));
                }
            } else if (player.hasEffect(MobEffects.INVISIBILITY)) {
                player.removeEffect(MobEffects.INVISIBILITY);
            }
        }

	    
	    if (hasGhastEnchantment(player) || (hasForceOfRejectors(player)&& FargosConfig.getConfigValue(player,"ghast_enchantment"))) { // Add force here
            boolean isCurrentlyWalking = player.isSprinting();

            if (isCurrentlyWalking && !wasWalking.getOrDefault(playerUUID, false)) {
                dashPlayerForward(player);
            }

            wasWalking.put(playerUUID, isCurrentlyWalking);
        }
	    if (hasZombieEnchantment(player) || (hasForceOfRejectors(player) && FargosConfig.getConfigValue(player,"zombie_enchantment"))) {
            Long lastAttackTime = lastAttackTimes.getOrDefault(playerUUID, 0L);
            if (currentTime - lastAttackTime >= INCREASE_ATTACK_SPEED_THRESHOLD) {
                increaseAttackSpeed(player, playerUUID);
            } else {
            	resetAttackSpeed(player, playerUUID);
            }
        }
	    boolean hasEnchantment = hasIronGolemEnchantment(player) || (hasForceOfWarrior(player) && FargosConfig.getConfigValue(player,"iron_golem_enchantment"));
        AttributeInstance healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute.getModifier(IRON_GOLEM_HEALTH_BOOST_ID) == null && hasEnchantment) {
            AttributeModifier healthBoostModifier = new AttributeModifier(IRON_GOLEM_HEALTH_BOOST_ID, "IronGolemHealthBoost", 20.0F, AttributeModifier.Operation.ADDITION);
            healthAttribute.addPermanentModifier(healthBoostModifier);
        } else if (healthAttribute.getModifier(IRON_GOLEM_HEALTH_BOOST_ID) != null && !hasEnchantment) {
            healthAttribute.removeModifier(IRON_GOLEM_HEALTH_BOOST_ID);
        }
	    
	}
	
	private static boolean isPlaceable(Level world, BlockPos pos) {
	    BlockState blockBelowState = world.getBlockState(pos.below());
	    return blockBelowState.is(BlockTags.BASE_STONE_OVERWORLD) || blockBelowState.is(BlockTags.DIRT);
	}
	
	@SubscribeEvent
	public static void onPlayerAttack(LivingAttackEvent event) {
		
		DamageSource source = event.getSource();
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if ((source.isProjectile() && (hasAmethystEnchantment(player))) ||
                (source.isFire() && (hasFireEnchantment(player))) ||
                (source.isFall() && (hasAirEnchantment(player))) ||
                (source.isExplosion() && (hasObsidianEnchantment(player))) ||
                (source.isMagic() && (hasDragonEnchantment(player) || (hasForceOfRejectors(player) && FargosConfig.getConfigValue(player,"dragon_enchantment"))))
            ) {
                if (player.isOnFire()) {
                	player.setRemainingFireTicks(0);
                	player.setSecondsOnFire(0);
                }
                event.setCanceled(true);
            }
        }
		
		 if (event.getSource().getEntity() instanceof Player) {
		        Player player = (Player) event.getSource().getEntity();
		        UUID playerId = player.getUUID();
		        long currentTime = player.level.getGameTime();
		        lastAttackTimes.put(playerId, currentTime);
		        if (hasZombieEnchantment(player) || (hasForceOfRejectors(player)&& FargosConfig.getConfigValue(player,"zombie_enchantment"))) {
		            long lastAttackTime = lastAttackTimes.getOrDefault(playerId, 0L);
		            if (currentTime - lastAttackTime > INCREASE_ATTACK_SPEED_THRESHOLD) {
		            	increaseAttackSpeed(player, playerId);
		                boostActivationTimes.put(playerId, currentTime);
		            }
		        }
		    }
	}
	
	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
	    Entity entity = event.getEntity();
	    DamageSource source = event.getSource();
	    if (entity instanceof Player) {
	        Player player = (Player) entity;
	        if (
	        	(source.isProjectile() && (hasAmethystEnchantment(player))) ||
	            (source.isFire() && (hasFireEnchantment(player))) ||
	            (source.isFall() && (hasAirEnchantment(player))) ||
	            (source.isExplosion() && (hasObsidianEnchantment(player))) ||
	            (source.isMagic() && (hasDragonEnchantment(player) || (hasForceOfRejectors(player)&& FargosConfig.getConfigValue(player,"dragon_enchantment")))) // Add Force here
	            ) {
	            event.setCanceled(true);
	            event.getEntity().hurtTime = 0;
	        }

	        if (hasDiamondEnchantment(player)) {
	            float newDamage = event.getAmount() * 0.8f;
	            event.setAmount(newDamage);
	        }
	        
	        if (!player.isOnGround() && !player.isInWater() && (hasNetherStarEnchantment(player))) {
	            float criticalMultiplier = 1.5F;
	            float increasedCritDamage = event.getAmount() * criticalMultiplier;
	            event.setAmount(increasedCritDamage);
	        }
	        if (hasCreeperEnchantment(player) || (hasForceOfRejectors(player)&& FargosConfig.getConfigValue(player,"creeper_enchantment"))) {
                createExplosion(player);
            }
            if (hasBattleEnchantment(player) || (hasForceOfWarrior(player) && FargosConfig.getConfigValue(player,"battle_enchantment"))) {
                Integer invincibilityTicks = invinciblePlayers.get(player.getUUID());
                if (invincibilityTicks == null || invincibilityTicks <= 0) {
                    invinciblePlayers.put(player.getUUID(), 5);
                    event.setCanceled(true);
                }
            }
            if (hasUndyingEnchantment(player) || (hasForceOfMystic(player) && FargosConfig.getConfigValue(player,"undying_enchantment"))) {
            	if (player.getHealth() - event.getAmount() <= 0) {
                    long currentTime = player.level.getGameTime();
                    undyingCooldowns.putIfAbsent(player.getUUID(), 0L);
                    if (currentTime - undyingCooldowns.get(player.getUUID()) >= UNDYING_COOLDOWN) {
                        teleportPlayerBackwards(player, 10);
                        player.setHealth(player.getMaxHealth());
                        undyingCooldowns.put(player.getUUID(), currentTime);
                        event.setCanceled(true);
                    }
                }
            }
            if ((hasVoidEnchantment(player) || (hasForceOfWarrior(player) && FargosConfig.getConfigValue(player,"void_enchantment"))) && Math.random() < 0.15) {
            	if (player.getHealth() - event.getAmount() <= 0) {
            		event.setCanceled(true);
            	}
            }
            
            if ((hasCactusEnchantment(player) || (hasForceOfWarrior(player) && FargosConfig.getConfigValue(player,"cactus_enchantment"))) && event.getSource().getEntity() instanceof LivingEntity) {
            	Entity attacker = event.getSource().getEntity();
                float damageToReflect = event.getAmount() / 4;
                attacker.hurt(DamageSource.GENERIC, damageToReflect);
            }
            if ((hasWitchEnchantment(player) || (hasForceOfMystic(player) && FargosConfig.getConfigValue(player,"witch_enchantment"))) && event.getSource().getEntity() instanceof LivingEntity attacker) {
                attacker.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
            }
            if ((hasThornyEnchantment(player) || (hasForceOfWarrior(player) && FargosConfig.getConfigValue(player,"thorny_enchantment")))) {
                if (event.getSource() == DamageSource.SWEET_BERRY_BUSH) {
                    event.setCanceled(true);
                }
            }
	    }
	    
	    if (event.getSource().getDirectEntity() instanceof Arrow && event.getSource().getEntity() instanceof Player) {
	        Player player = (Player) event.getSource().getEntity();
	        if (hasSkeletonEnchantment(player) || (hasForceOfRejectors(player) && FargosConfig.getConfigValue(player,"skeleton_enchantment"))) {
	            float newDamage = event.getAmount() * 1.5F;
	            event.setAmount(newDamage);
	        }
	    }

	    if (source.getEntity() instanceof Player) {
	        Player attacker = (Player) source.getEntity();
	        if (entity instanceof Monster) {
	            LivingEntity livingEntity = (LivingEntity) entity;
	            if ((hasBlazeEnchantment(attacker) || (hasForceOfRejectors(attacker)&& FargosConfig.getConfigValue(attacker,"blaze_enchantment")))
	            		&& livingEntity.isOnFire()) {
	                float increasedDamage = event.getAmount() * 1.25f;
	                event.setAmount(increasedDamage);
	            }
	            if ((hasVampiricEnchantment(attacker) || (hasForceOfMystic(attacker) && FargosConfig.getConfigValue(attacker,"vampiric_enchantment")))) {
	                float healAmount = event.getAmount() * 0.1f;
	                attacker.heal(healAmount);
	            }
	        }
	    }

	    if (source.getEntity() instanceof Player) {
	        Player attacker = (Player) source.getEntity();
	        applyVindicatorBoostIfEligible(attacker, event);
	        checkAndApplyStaticChargeAttack(attacker, event);
	        checkAndApplyEmeraldEnchantment(attacker, event);
	    }
	}
	
	@SubscribeEvent
    public void breakSpeedCheck(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (hasSoulOfFlightMastery(player) && !player.isOnGround()) {
            //LOGGER.info("Flying speed: " + event.getState());
            float standardSpeed = player.getInventory().getDestroySpeed(event.getState());
            event.setNewSpeed(standardSpeed);
            return;
        } else {
            //LOGGER.info("Ground speed: " + event.getState());
        }
    }
	
	private static void resetAttackSpeed(Player player, UUID playerId) {
	    AttributeInstance attackSpeedAttribute = player.getAttribute(Attributes.ATTACK_SPEED);
	    if (attackSpeedAttribute != null && attackSpeedModifiers.containsKey(playerId)) {
	        attackSpeedAttribute.removeModifier(attackSpeedModifiers.get(playerId));
	        attackSpeedModifiers.remove(playerId);
	        //System.out.println("Reset attack speed for player " + player.getName().getString() + " (ID: " + playerId + ")");
	    }
	}
	
	private static void increaseAttackSpeed(Player player, UUID playerId) {
	    AttributeInstance attackSpeedAttribute = player.getAttribute(Attributes.ATTACK_SPEED);
	    if (attackSpeedAttribute != null && !attackSpeedModifiers.containsKey(playerId)) {
	        AttributeModifier modifier = new AttributeModifier("ZombieAttackSpeedBoost", ATTACK_SPEED_BOOST, AttributeModifier.Operation.ADDITION);
	        attackSpeedAttribute.addPermanentModifier(modifier);
	        attackSpeedModifiers.put(playerId, modifier);
	        //System.out.println("Increased attack speed for player " + player.getName().getString() + " (ID: " + playerId + ")");
	    }
	}
	
	private static void trackWeaponSwitch(Player player, UUID playerId) {
	    ItemStack currentItem = player.getMainHandItem();
	    long currentTime = player.level.getGameTime();

	    if (!lastHeldItems.containsKey(playerId) || !ItemStack.matches(currentItem, lastHeldItems.get(playerId))) {
	        lastHeldItems.put(playerId, currentItem.copy());
	        lastWeaponSwitchTimes.put(playerId, currentTime);
	        if (!lastBoostTimes.containsKey(playerId) || (currentTime - lastBoostTimes.get(playerId) > BOOST_COOLDOWN)) {
	            canBoostAttack.put(playerId, true);
	        }
	    }
	}
	
	private static void dashPlayerForward(Player player) {
		long currentTime = player.level.getGameTime();
	    UUID playerId = player.getUUID();
		
		if (lastDashTimes.containsKey(playerId) && (currentTime - lastDashTimes.get(playerId)) < 60) {
	        return;
	    }
	    Vec3 lookVec = player.getLookAngle();
	    double dashDistance = 5.0;
	    Vec3 startPos = player.position();
	    double eyeHeightFactor = 1.8 * 0.75;
	    Vec3 adjustedEyePos = startPos.add(0.0, eyeHeightFactor, 0.0);
	    Vec3 endPos = startPos.add(lookVec.x * dashDistance, 0.0, lookVec.z * dashDistance);

	    endPos = checkForObstructions(player, adjustedEyePos, startPos, lookVec, dashDistance);
	    player.teleportTo(endPos.x, startPos.y, endPos.z);
	    lastDashTimes.put(playerId, currentTime);
	}

	private static Vec3 checkForObstructions(Player player, Vec3 playerEye, Vec3 startPos, Vec3 lookVec, double dashDistance) {
	    Vec3 endPos = startPos.add(lookVec.x * dashDistance, 0.0, lookVec.z * dashDistance);
	    BlockHitResult hitResultEye = player.level.clip(new ClipContext(playerEye, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
	    BlockHitResult hitResultFeet = player.level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
	    double distanceToHitEye = hitResultEye.getType() != BlockHitResult.Type.MISS ? hitResultEye.getLocation().distanceTo(playerEye) : dashDistance;
	    double distanceToHitFeet = hitResultFeet.getType() != BlockHitResult.Type.MISS ? hitResultFeet.getLocation().distanceTo(startPos) : dashDistance;
	    double safeDashDistance = Math.min(distanceToHitEye, distanceToHitFeet) - 0.5;
	    dashDistance = Math.max(0, safeDashDistance);
	    endPos = startPos.add(lookVec.x * dashDistance, 0.0, lookVec.z * dashDistance);
	    if (!isPositionSafe(player, endPos)) {
	        dashDistance -= 0.5;
	        return checkForObstructions(player, playerEye, startPos, lookVec, dashDistance);
	    }

	    return endPos;
	}

	private static boolean isPositionSafe(Player player, Vec3 pos) {
	    Level level = player.level;
	    BlockPos feetPos = new BlockPos(pos);
	    BlockPos headPos = feetPos.above();
	    return !level.getBlockState(feetPos).isSolidRender(level, feetPos) &&
	           !level.getBlockState(headPos).isSolidRender(level, headPos);
	}

	private static boolean canSee(Player player, Entity target) {
        Vec3 playerEye = player.getEyePosition(1.0F);
        Vec3 targetPos = target.position();
        BlockHitResult result = player.level.clip(new ClipContext(playerEye, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        
        return result.getType() == HitResult.Type.MISS;
    }
	
	private static boolean isPlayerStill(Player player) {
        UUID playerId = player.getUUID();
        BlockPos currentPosition = player.blockPosition();
        long currentTime = System.currentTimeMillis();

        if (!lastPositions.containsKey(playerId) || !lastPositions.get(playerId).equals(currentPosition)) {
            lastPositions.put(playerId, currentPosition);
            lastMovementTime.put(playerId, currentTime);
            return false;
        } else if (currentTime - lastMovementTime.get(playerId) > STILL_TIME_THRESHOLD) {
            return true;
        }
        return false;
    }
	
	@SubscribeEvent
	public static void onBlockBreak(BreakEvent event) {

	    Player player = event.getPlayer();
	    BlockState state = event.getState();
	    if (state.is(Blocks.STONE) && (hasGoldEnchantment(player))) {
	        ServerLevel serverLevel = (ServerLevel) player.level;
	        Random random = new Random();
	        if (random.nextDouble() < 0.05) {
	            int nuggetCount = 2 + random.nextInt(2);
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
	
	@SubscribeEvent
    public static void onBlockPlacement(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();

        if (hasArchitectEnchantment(player) && hand == InteractionHand.MAIN_HAND) {
            // Check if the player is holding a block and is about to place it
            if (player.getMainHandItem().getItem() instanceof BlockItem) {
                // Reset the cooldown
                player.getCooldowns().removeCooldown(player.getMainHandItem().getItem());
                // Additional logic to handle the block placement can be added here
            }
        }
    }
	
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

    	Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() instanceof StyxGazer) {
            Level world = player.level;
            Vec3 playerPos = player.position();
            float yaw = player.getYRot();
            double rad = Math.toRadians(yaw);
            double xOffset = -Math.sin(rad);
            double zOffset = Math.cos(rad);
            Vec3 spawnPos = playerPos.add(xOffset, 0, zOffset);
            LaserSwordEntity laserSword = new LaserSwordEntity(ModEntities.LASER_SWORD.get(), world);
            laserSword.setPlayer(player);
            laserSword.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            laserSword.setYRot(yaw);
            world.addFreshEntity(laserSword);
            // Needs to add a delay
            // Needs to apply damage, aabd or raycast
        }
		if (hasEarthEnchantment(player)) {
			if(event.getLevel().getBlockState(event.getPos()).is(BlockTags.DIRT)){
				if(!event.getLevel().isClientSide()){
					event.getLevel().setBlock(new BlockPos(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()), Blocks.GRASS_BLOCK.defaultBlockState(), 3);
				}
			}
	    }
	}
	
    private static void applyIronArmorModifier(Player player) {
        AttributeModifier modifier = new AttributeModifier(IRON_ARMOR_MODIFIER_ID, "Iron Enchantment Armor", IRON_ARMOR_MODIFIER_AMOUNT, AttributeModifier.Operation.ADDITION);
        if (player.getAttribute(Attributes.ARMOR).getModifier(IRON_ARMOR_MODIFIER_ID) == null) {
            player.getAttribute(Attributes.ARMOR).addTransientModifier(modifier);
        }
    }

    private static void removeIronArmorModifier(Player player) {
        if (player.getAttribute(Attributes.ARMOR).getModifier(IRON_ARMOR_MODIFIER_ID) != null) {
            player.getAttribute(Attributes.ARMOR).removeModifier(IRON_ARMOR_MODIFIER_ID);
        }
    }
    
    private static void applyDiamondArmorModifier(Player player) {
        AttributeModifier armorModifier = new AttributeModifier(
            DIAMOND_ARMOR_MODIFIER_ID, "Diamond Enchantment Armor", DIAMOND_ARMOR_MODIFIER_AMOUNT, AttributeModifier.Operation.ADDITION
        );
        if (player.getAttribute(Attributes.ARMOR).getModifier(DIAMOND_ARMOR_MODIFIER_ID) == null) {
            player.getAttribute(Attributes.ARMOR).addTransientModifier(armorModifier);
        }
        AttributeModifier toughnessModifier = new AttributeModifier(
            DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID, "Diamond Enchantment Armor Toughness", DIAMOND_ARMOR_TOUGHNESS_MODIFIER_AMOUNT, AttributeModifier.Operation.ADDITION
        );
        if (player.getAttribute(Attributes.ARMOR_TOUGHNESS).getModifier(DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID) == null) {
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(toughnessModifier);
        }
    }

    private static void removeDiamondArmorModifier(Player player) {
        if (player.getAttribute(Attributes.ARMOR).getModifier(DIAMOND_ARMOR_MODIFIER_ID) != null) {
            player.getAttribute(Attributes.ARMOR).removeModifier(DIAMOND_ARMOR_MODIFIER_ID);
        }
        if (player.getAttribute(Attributes.ARMOR_TOUGHNESS).getModifier(DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID) != null) {
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(DIAMOND_ARMOR_TOUGHNESS_MODIFIER_ID);
        }
    }

	private static void addMarkedLapisToEnchantmentTable(EnchantmentMenu enchantmentMenu, Player player) {
	    Slot lapisSlot = enchantmentMenu.getSlot(1);
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
	        lapisEntity.discard();
	    }
	}
	
	private static boolean hasAddedByEnchantmentTag(ItemStack stack) {
	    CompoundTag nbt = stack.getTag();
	    return nbt != null && nbt.contains("AddedByEnchantment") && nbt.getBoolean("AddedByEnchantment");
	}

	private static void restoreOriginalLapisAmount(Player player) {
	    if (player.containerMenu instanceof EnchantmentMenu) {
	        EnchantmentMenu enchantmentMenu = (EnchantmentMenu) player.containerMenu;
	        Slot lapisSlot = enchantmentMenu.getSlot(1);
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
	                break;
	            }
	        }
	    }
	}
	
	private static void createExplosion(Player player) {
        player.level.explode(player, player.getX(), player.getY(), player.getZ(), 2.0F, false, Explosion.BlockInteraction.NONE);
    }
	
	@SubscribeEvent
    public static void onPlayerJump(LivingJumpEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (hasCreeperEnchantment(player) || (hasForceOfRejectors(player)&& FargosConfig.getConfigValue(player,"creeper_enchantment"))) { // Add Force here
                createExplosion(player);
            }
            if (hasShulkerEnchantment(player) || (hasForceOfMystic(player) && FargosConfig.getConfigValue(player,"shulker_enchantment"))) {
            	if (!hasSoulOfFlightMastery(player)) {
            		if (!player.isCreative()) {
                    	player.addEffect(new MobEffectInstance(EffectsInit.FLIGHT_EFFECT.get(), 400));
                    }
            	}
            }
        }
    }


	private static void applyVindicatorBoostIfEligible(Player attacker, LivingHurtEvent event) {
	    UUID attackerId = attacker.getUUID();
	    if ((hasVindicatorEnchantment(attacker) || (hasForceOfRejectors(attacker)) && FargosConfig.getConfigValue(attacker,"vindicator_enchantment")) && isEligibleForVindicatorBoost(attackerId, attacker.level.getGameTime())) { // Add force here
	        float boostedDamage = event.getAmount() * 1.5F;
	        event.setAmount(boostedDamage);
	        canBoostAttack.put(attackerId, false);
	        lastBoostTimes.put(attackerId, attacker.level.getGameTime());
	    }
	}

	private static boolean isEligibleForVindicatorBoost(UUID playerId, long currentTime) {
	    Long lastSwitchTime = lastWeaponSwitchTimes.get(playerId);
	    boolean isEligible = lastSwitchTime != null && canBoostAttack.getOrDefault(playerId, false) && (currentTime - lastSwitchTime <= 60);
	    return isEligible;
	}

	@SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (hasAppleEnchantment(player)) { // Add Force here
				float newHealAmount = event.getAmount() * 1.25f;
		        event.setAmount(newHealAmount);
			}
		}
    }
	
	@SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
		Entity entity = event.getEntity();
		Player player = (Player) entity;
        if (hasPickaxeEnchantment(player)) {
            float newSpeed = event.getOriginalSpeed() * 1.5f;
            event.setNewSpeed(newSpeed);
        }
    }

    @SuppressWarnings("deprecation")
    private static void checkAndApplyStaticChargeAttack(Player player, LivingHurtEvent event) {
        CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> 
            itemStack.getItem() instanceof CopperEnchantment, player
        ).ifPresent(triple -> {
            ItemStack stack = triple.getRight();
            CompoundTag nbt = stack.getTag();
            if (nbt != null && nbt.contains("StaticChargeLevel")) {
                int chargePoints = nbt.getInt("StaticChargeLevel");
                if (chargePoints > 0) {
                    int extraDamage = chargePoints / 100;
                    event.setAmount(event.getAmount() + extraDamage);
                    spawnElectricSparkParticles(event.getEntity(), player, chargePoints);
                    nbt.putInt("StaticChargeLevel", 0);
                    stack.setTag(nbt);
                }
            }
        });
    }

    private static void checkAndApplyEmeraldEnchantment(Player player, LivingHurtEvent event) {
        if (hasEmeraldEnchantment(player)) {
            if (event.getEntity() instanceof AbstractIllager) {
                float extraDamage = 2.0F; // Set the extra damage amount
                float baseDamage = event.getAmount();
                float totalDamage = baseDamage + extraDamage;
                event.setAmount(totalDamage);
            }
        };
    }

    private static void spawnElectricSparkParticles(Entity hitEntity, Player player, int chargePoints) {


        if (hitEntity.level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) hitEntity.level;

            double dx = player.getX() - hitEntity.getX();
            double dy = (player.getY() + player.getBbHeight() / 2.0) - (hitEntity.getY() + hitEntity.getBbHeight() / 2.0);
            double dz = player.getZ() - hitEntity.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double dirX = dx / distance;
            double dirY = dy / distance;
            double dirZ = dz / distance;

            int particleAmount = chargePoints / 10;
            Random random = new Random();
            for (int i = 0; i < particleAmount; i++) {
                double offsetX = dirX * 0.5 + (random.nextDouble() - 0.5) * 0.2;
                double offsetY = dirY * 0.5 + (random.nextDouble() - 0.5) * 0.2;
                double offsetZ = dirZ * 0.5 + (random.nextDouble() - 0.5) * 0.2;

                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                                          hitEntity.getX() + offsetX, hitEntity.getY() + hitEntity.getBbHeight() / 2.0F + offsetY, hitEntity.getZ() + offsetZ,
                                          1, 0.1, 0.1, 0.1, 0.02);
            }
            float volume = Math.min(1.0F, chargePoints / 1000.0F);
            serverLevel.playSound(null, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, volume, 1.0F);
        }
    }

    private static void teleportPlayerBackwards(Player player, int blocks) {
        Direction direction = player.getDirection();
        BlockPos pos = player.blockPosition();
        BlockPos newPos = pos.relative(direction.getOpposite(), blocks);
        player.teleportTo(newPos.getX(), newPos.getY(), newPos.getZ());
    }

    private static void tryVoidTick(Level world, Player player) {
        int minY = world.dimensionType().minY();
        int maxY = world.dimensionType().logicalHeight();
        if ((hasVoidEnchantment(player) || (hasForceOfWarrior(player) && FargosConfig.getConfigValue(player,"void_enchantment"))) && player.getY() < (minY - 40)) {
            player.teleportTo(player.getX(), maxY, player.getZ());
        }
    }
    
    // Overworld + Copper + Iron
	private static boolean hasAmethystEnchantment(Player player) {
    	
        @SuppressWarnings("deprecation")
		boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof AmathystEnchantment, player).isPresent();
        boolean hasForceConfig = FargosConfig.getConfigValue(player,"amethyst_enchantment");
        boolean hasForce = hasForceOfOverworld(player);
        
        return hasCurio || (hasForce && hasForceConfig);
    }
    @SuppressWarnings("deprecation")
    private static boolean hasEmeraldEnchantment(Player player) {
        boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof EmeraldEnchantment, player).isPresent();
        boolean hasConfig = FargosConfig.getConfigValue(player,"emerald_enchantment");
        boolean hasForce = hasForceOfOverworld(player);

        return hasCurio || (hasForce && hasConfig);
    }
    @SuppressWarnings("deprecation")
    private static boolean hasIronEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof IronEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasDiamondEnchantment(Player player) {
        boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof DiamondEnchantment, player).isPresent();
        boolean hasConfig = FargosConfig.getConfigValue(player,"diamond_enchantment");
        boolean hasForce = hasForceOfOverworld(player);

        return hasCurio || (hasForce && hasConfig);
    }
//    @SuppressWarnings("deprecation")
//    private static boolean hasRedstoneEnchantment(Player player) {
//        boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof RedstoneEnchantment, player).isPresent();
//        boolean hasConfig = FargosConfig.getConfigValue(player,"redstone_enchantment");
//        boolean hasForce = hasForceOfOverworld(player);
//
//        return hasCurio || (hasForce && hasConfig);
//    }
    @SuppressWarnings("deprecation")
    private static boolean hasGoldEnchantment(Player player) {
        boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof GoldEnchantment, player).isPresent();
        boolean hasConfig = FargosConfig.getConfigValue(player,"gold_enchantment");
        boolean hasForce = hasForceOfOverworld(player);

        return hasCurio || (hasForce && hasConfig);
    }
//    @SuppressWarnings("deprecation")
//    private static boolean hasCopperEnchantment(Player player) {
//        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof CopperEnchantment, player).isPresent();
//    }
    @SuppressWarnings("deprecation")
    private static boolean hasLapisEnchantment(Player player) {
        boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof LapisEnchantment, player).isPresent();
        boolean hasConfig = FargosConfig.getConfigValue(player,"lapis_enchantment");
        boolean hasForce = hasForceOfOverworld(player);

        return hasCurio || (hasForce && hasConfig);
    }
    
    // Nature
    @SuppressWarnings("deprecation")
    private static boolean hasFireEnchantment(Player player) {
        boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof FireEnchantment, player).isPresent();
        boolean hasConfig = FargosConfig.getConfigValue(player,"fire_enchantment");
        boolean hasForce = hasForceOfNature(player);

        return hasCurio || (hasForce && hasConfig);
    }
//    @SuppressWarnings("deprecation")
//    private static boolean hasWaterEnchantment(Player player) {
//        boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof WaterEnchantment, player).isPresent();
//        boolean hasConfig = FargosConfig.getConfigValue(player,"water_enchantment");
//        boolean hasForce = hasForceOfNature(player);
//
//        return hasCurio || (hasForce && hasConfig);
//    }
    @SuppressWarnings("deprecation")
    private static boolean hasAirEnchantment(Player player) {
        boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof AirEnchantment, player).isPresent();
        boolean hasConfig = FargosConfig.getConfigValue(player,"air_enchantment");
        boolean hasForce = hasForceOfNature(player);

        return hasCurio || (hasForce && hasConfig);
    }
    @SuppressWarnings("deprecation")
    private static boolean hasEarthEnchantment(Player player) {
        boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof EarthEnchantment, player).isPresent();
        boolean hasConfig = FargosConfig.getConfigValue(player,"earth_enchantment");
        boolean hasForce = hasForceOfNature(player);

        return hasCurio || (hasForce && hasConfig);
    }
    
    // Rejectors + nether star + mooshroom + apple + obsidian + pickaxe
//    @SuppressWarnings("deprecation")
//    private static boolean hasWitherEnchantment(Player player) {
//        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof WitherEnchantment, player).isPresent();
//    }
    @SuppressWarnings("deprecation")
    private static boolean hasNetherStarEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof NetherStarEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasAppleEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof AppleEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasObsidianEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ObsidianEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasDragonEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof DragonEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasMooshroomEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof MooshroomEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasSkeletonEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof SkeletonEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasZombieEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ZombieEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasBlazeEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof BlazeEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasPickaxeEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof PickaxeEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasCreeperEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof CreeperEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasGhastEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof GhastEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasVindicatorEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof VindicatorEnchantment, player).isPresent();
    }
    
    // Mystic
    @SuppressWarnings("deprecation")
    private static boolean hasVampiricEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof VampiricEnchantment, player).isPresent();
    }
//    @SuppressWarnings("deprecation")
//    private static boolean hasEnchantingEnchantment(Player player) {
//        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof EnchantingEnchantment, player).isPresent();
//    }
//    @SuppressWarnings("deprecation")
//    private static boolean hasLibrarianEnchantment(Player player) {
//        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof LibrarianEnchantment, player).isPresent();
//    }
    @SuppressWarnings("deprecation")
    private static boolean hasWitchEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof WitchEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasShulkerEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ShulkerEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasUndyingEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof UndyingEnchantment, player).isPresent();
    }
    
    // Warrior
    @SuppressWarnings("deprecation")
    private static boolean hasBattleEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof BattleEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasCactusEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof CactusEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasVoidEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof VoidEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasThornyEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ThornyEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasIronGolemEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof IronGolemEnchantment, player).isPresent();
    }
    
    // Explorer
    @SuppressWarnings("deprecation")
    private static boolean hasArchitectEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ArchitectEnchantment, player).isPresent();
    }
//    @SuppressWarnings("deprecation")
//    private static boolean hasEndermanEnchantment(Player player) {
//        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof EndermanEnchantment, player).isPresent();
//    }
//    @SuppressWarnings("deprecation")
//    private static boolean hasArcticEnchantment(Player player) {
//        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ArcticEnchantment, player).isPresent();
//    }
//    @SuppressWarnings("deprecation")
//    private static boolean hasSpectralEnchantment(Player player) {
//        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof SpectralEnchantment, player).isPresent();
//    }
    @SuppressWarnings("deprecation")
    private static boolean hasGlowstoneEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof GlowstoneEnchantment, player).isPresent();
    }
    
    @SuppressWarnings("deprecation")
    private static boolean hasForceOfOverworld(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfOverworld, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    @SuppressWarnings("deprecation")
    private static boolean hasForceOfNature(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfNature, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    @SuppressWarnings("deprecation")
    private static boolean hasForceOfRejectors(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfRejectors, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    @SuppressWarnings("deprecation")
    private static boolean hasForceOfMystic(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfMystic, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    @SuppressWarnings("deprecation")
    private static boolean hasForceOfWarrior(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfWarrior, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    @SuppressWarnings("deprecation")
    private static boolean hasForceOfExplorer(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfExplorer, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    
    @SuppressWarnings("deprecation")
    private static boolean hasSoulOfMinecraft(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof SoulOfMinecraft, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasSoulOfFlightMastery(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof SoulOfFlightMastery, player).isPresent();
    }

}
