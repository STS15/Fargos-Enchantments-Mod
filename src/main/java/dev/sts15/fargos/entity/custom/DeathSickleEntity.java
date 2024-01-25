package dev.sts15.fargos.entity.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Collections;

public class DeathSickleEntity extends LivingEntity implements IAnimatable {
	
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private Map<UUID, Integer> lastDamageTicks = new HashMap<>();
    private final int spawnTick;
    private int lifespan;
    private boolean setLifespan = false;
    private Player circlingPlayer;
    private double circleRadius = 4.0;
    private double angle = 0.0;
    private double assignedAngle;

    public DeathSickleEntity(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
        this.spawnTick = this.tickCount;
    }
    
    public void setLifespan(int ticks) {
        this.lifespan = ticks;
        this.setLifespan = true;
    }
    
    public void setPlayerToCircle(Player player, double angle) {
        this.circlingPlayer = player;
        this.assignedAngle = angle;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
        	if (this.tickCount - this.spawnTick >= 6000) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }
        	if (!setLifespan) {
        		this.level.getEntities(this, this.getBoundingBox(), e -> e instanceof Player)
                .forEach(entity -> {
                    Player player = (Player) entity;
                    UUID playerId = player.getUUID();
                    int currentTick = this.tickCount;

                    if (lastDamageTicks.getOrDefault(playerId, 0) <= currentTick - 10) {
                        player.setHealth((player.getHealth() * 0.75F) - 1.0F);
                        lastDamageTicks.put(playerId, currentTick);
                    }
                });
        	}
        	if (this.setLifespan) {
                // Define the bounding box for collision detection
                AABB collisionBox = this.getBoundingBox().inflate(0.5);

                // Find entities within the bounding box
                List<Entity> nearbyEntities = this.level.getEntities(this, collisionBox, e -> 
                    (e instanceof LivingEntity) && e != this && !(e instanceof DeathSickleEntity)
                );

                // Apply magic damage to each entity
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.hurt(DamageSource.MAGIC, 8.0F); // Apply 8 magic damage
                    }
                }
            }
        }
        if (setLifespan) {
        	if (this.lifespan > 0) {
                this.lifespan--;
                if (this.lifespan <= 0) {
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }
        if (circlingPlayer != null) {
            this.assignedAngle += Math.PI / 20; // Update the angle to move in a circle
            if (this.assignedAngle > 2 * Math.PI) {
                this.assignedAngle -= 2 * Math.PI;
            }

            double dx = circleRadius * Math.sin(this.assignedAngle);
            double dz = circleRadius * Math.cos(this.assignedAngle);
            BlockPos newPos = new BlockPos(circlingPlayer.getX() + dx, circlingPlayer.getY() + 1, circlingPlayer.getZ() + dz);
            this.moveTo(newPos, 0.0F, 0.0F);
        }
        
    }


    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0);
    }
    
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
    event.getController().setAnimation(new AnimationBuilder().addAnimation("death_sickle.idle", true));
    return PlayState.CONTINUE;
    }

	@Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

	@Override
	public AnimationFactory getFactory() {
	    return this.factory;
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
	    return Collections.emptyList(); // Return an empty list
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot) {
	    return ItemStack.EMPTY; // Return an empty item stack
	}

	@Override
	public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
	    // If your entity does not use equipment, you can leave this empty
	}

	@Override
	public HumanoidArm getMainArm() {
	    return HumanoidArm.RIGHT; // Or LEFT, if you prefer
	}

}
