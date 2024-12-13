package dev.sts15.fargos.entity.custom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.entity.goals.mutant.CircleSickleGoal;
import dev.sts15.fargos.entity.goals.mutant.CustomLookControl;
import dev.sts15.fargos.entity.goals.mutant.SummonLaserSwordGoal;
import dev.sts15.fargos.entity.goals.mutant.SummonTargetLockedGoal;
import dev.sts15.fargos.init.ModEntities;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.Difficulty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import java.util.ArrayList;

public class MutantEntity extends Monster implements IAnimatable {
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.RED, ServerBossEvent.BossBarOverlay.PROGRESS);
	private List<DeathSickleEntity> spawnedSickles;
	
	private boolean isAttackAnimationPlaying = false;
	private boolean isAttacking = false;
	private boolean isBossMusicPlaying = false;
	private final ServerBossEvent bossBar;
	private Set<UUID> playersWithStoppedMusic = new HashSet<>();
	private boolean isFlying = false;
	private float customPitch = 0.0F;
	private BlockPos spawnPosition;
	
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public MutantEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ModEntities.MUTANT.get(), world);
	}

	@SuppressWarnings("deprecation")
	public MutantEntity(EntityType<MutantEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 0;
		setNoAi(false);
		setCustomName(Component.literal("Mutant"));
		setCustomNameVisible(true);
		this.moveControl = new FlyingMoveControl(this, 10, true);
		this.lookControl = new CustomLookControl(this);
		this.bossBar = null;
		this.spawnPosition = null;
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
	    System.out.println("MutantEntity hurt method called!");

	    // Check for damage from explosions
	    if (source.isExplosion()) {
	        System.out.println("Damage source: Explosion");
	        return false; // Prevents damage from explosions
	    }

	    Entity directEntity = source.getDirectEntity();

	    if (directEntity != null) {
	        // Check if the direct entity is a projectile (e.g., arrow)
	        if (directEntity instanceof Projectile) {
	            Entity shooter = ((Projectile) directEntity).getOwner();

	            // Check if the shooter is a player
	            if (shooter instanceof Player) {
	                double distance = shooter.distanceTo(this);
	                System.out.println("Damage source: Player's projectile");
	                System.out.println("Player is " + distance + " blocks away.");

	                // Check if the player is farther than 15 blocks
	                if (distance > 16.5) {
	                    System.out.println("Damage ignored.");
	                    return false; // Ignore damage if player is more than 15 blocks away
	                } else {
	                    System.out.println("Damage allowed.");
	                }
	            }
	        }
	    }

	    // Normal behavior for other damage sources
	    return super.hurt(source, amount);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new FlyingPathNavigation(this, world);
	}

	@Override
	protected void registerGoals() {
	    super.registerGoals();

	    // Targeting goal: high priority, as it's fundamental for other attack goals
	    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true) {
	        @Override
	        protected AABB getTargetSearchArea(double targetDistance) {
	            return this.mob.getBoundingBox().inflate(15.0);
	        }
	    });

	    // SummonTargetLockedGoal: This can be a mid-priority goal
	    this.goalSelector.addGoal(4, new SummonTargetLockedGoal(this, 15.0));

	    // CircleSickleGoal: Adjust the priority as needed
	    this.spawnedSickles = new ArrayList<>();
	    this.goalSelector.addGoal(2, new CircleSickleGoal(this, 8.0, spawnedSickles));

	    // SummonLaserSwordGoal: Lower priority as it might not need to run as often
	    this.goalSelector.addGoal(3, new SummonLaserSwordGoal(this, 0, 1, 0));
	}


	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}
	
	@Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.spawnPosition == null) {
            this.spawnPosition = this.blockPosition();
        }
        this.startBossMusic();
    }
	
	@Override
    public void setPos(double x, double y, double z) {
        // Prevent position change if spawn position is set
        if (this.spawnPosition != null) {
            super.setPos(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());
        } else {
            super.setPos(x, y, z);
        }
    }

    @Override
    public void push(double x, double y, double z) {
        // Prevent push effects
    }

	@Override
	public void setNoGravity(boolean ignored) {
		super.setNoGravity(false);
	}

	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
	}

	@SuppressWarnings("deprecation")
	public static void init() {
		SpawnPlacements.register(ModEntities.MUTANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 1.5);
		builder = builder.add(Attributes.MAX_HEALTH, 1024);
		builder = builder.add(Attributes.ARMOR, 10);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 25);
		builder = builder.add(Attributes.FOLLOW_RANGE, 50);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 10);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2);
		builder = builder.add(Attributes.FLYING_SPEED, 2);
		return builder;
	}

	@Override
	public AnimationFactory getFactory() {
	    return this.factory;
	}

	@Override
  public void tick() {
		super.tick();
		if (!this.level.isClientSide && this.level instanceof ServerLevel serverLevel) {
			final double MAX_DISTANCE_SQUARED = 50.0 * 50.0;
		    Player nearestPlayer = this.level.getNearestPlayer(this, MAX_DISTANCE_SQUARED);
		}
		if (!this.level.isClientSide && this.spawnPosition != null) {
	        Player nearestPlayer = this.level.getNearestPlayer(this, -1); // Get the closest player
	        if (nearestPlayer != null) {
	            double dx = nearestPlayer.getX() - this.getX();
	            double dz = nearestPlayer.getZ() - this.getZ();
	            float angle = (float)(Math.atan2(dz, dx) * (180 / Math.PI) - 90); // Calculate the angle

	            // Update rotation to face the player
	            this.setYRot(angle);
	            this.yHeadRot = angle;
	            this.yBodyRot = angle;
	        }
	    }
	}
	
	private void stopBossMusic(ServerLevel serverLevel) {
        final double MAX_DISTANCE = 50.0;
        List<ServerPlayer> players = serverLevel.players();
        for (ServerPlayer player : players) {
            if (this.distanceToSqr(player) < MAX_DISTANCE * MAX_DISTANCE) {
                isBossMusicPlaying = false;
                serverLevel.getServer().getCommands().performPrefixedCommand(serverLevel.getServer().createCommandSourceStack().withSuppressedOutput().withPermission(4), "stopsound " + player.getName().getString() + " * " + Fargos.MODID + ":mutant_boss_music");
            }
        }
    }
  
    public boolean isPlayingCustomBossMusic() {
        return this.isBossMusicPlaying;
    }

    public void setPlayingCustomBossMusic(boolean playing) {
        this.isBossMusicPlaying = playing;
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.level.isClientSide && this.level instanceof ServerLevel serverLevel) {
            final double MAX_DISTANCE = 50.0; // Adjust the range as needed
            List<ServerPlayer> players = serverLevel.players();
            for (ServerPlayer player : players) {
                if (this.distanceToSqr(player) < MAX_DISTANCE * MAX_DISTANCE) {
                	isBossMusicPlaying = false;
                	stopBossMusic(serverLevel);
                }
            }
        }
        for (DeathSickleEntity sickle : spawnedSickles) {
            if (sickle != null && !sickle.isRemoved()) {
                sickle.discard(); // Remove the sickle
            }
        }
        spawnedSickles.clear();
    }
    
    private void startBossMusic() {
        if (!this.level.isClientSide && this.level instanceof ServerLevel serverLevel) {
            // Play boss music for all players within a certain distance
            final double MAX_DISTANCE = 50.0;
            List<ServerPlayer> players = serverLevel.players();
            for (ServerPlayer player : players) {
                if (this.distanceToSqr(player) < MAX_DISTANCE * MAX_DISTANCE) {
                    serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack().withSuppressedOutput().withPermission(4), 
                        "playsound " + Fargos.MODID + ":mutant_boss_music master " + player.getName().getString()
                    );
                }
            }
            isBossMusicPlaying = true;
        }
    }

    
    @Override
    public void setXRot(float pitch) {
        super.setXRot(pitch);
        this.customPitch = pitch;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        // Handle animations
        if (isAttackAnimationPlaying || this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mutant.attack", false));
            isAttackAnimationPlaying = true;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mutant.flying", true));
        }
        return PlayState.CONTINUE;
    }

	@Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    protected float getSoundVolume() {
        return 0.2F;
    }
    
	public void setAttacking(boolean b) {
		isAttacking = b;
	}
	
	private boolean isAttacking() {
		return isAttacking;
	}

}