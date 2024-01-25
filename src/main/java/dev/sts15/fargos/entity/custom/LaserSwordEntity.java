package dev.sts15.fargos.entity.custom;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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

public class LaserSwordEntity extends LivingEntity implements IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int slamTimer = 0;
    private final Map<UUID, Boolean> alreadyHitPlayers = new HashMap<>();
    private static final float DAMAGE_AMOUNT = 25.0F;
    private boolean isPlayerControlled = false;
    private Player controllingPlayer = null;
    

    public LaserSwordEntity(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }
    
    public void setPlayer(Player player) {
    	this.isPlayerControlled = true;
        this.controllingPlayer = player;
    }

    @Override
    public void tick() {
        super.tick();
        slamTimer++;
        if (slamTimer == 80) {
            this.remove(RemovalReason.DISCARDED);
        }
        int lifeTicks = this.tickCount;
        if (lifeTicks == 45) {
        	performRaycastAndDamage();
        }
        if (isPlayerControlled && controllingPlayer != null) {
        	//Vec3 lookVec = controllingPlayer.getLookAngle();
            //Vec3 frontPos = controllingPlayer.position().add(lookVec.scale(0.5));
            //this.setPos(frontPos.x, frontPos.y+1, frontPos.z);
            //this.setYRot(controllingPlayer.getYHeadRot());
        }
    }
    
    private void performRaycastAndDamage() {
    	Vec3 baseStart = this.position();
        Vec3 direction;

        if (isPlayerControlled) {
            direction = Vec3.directionFromRotation(0, this.getYRot());
        } else {
            direction = Vec3.directionFromRotation(0, this.getYRot());
        }

        Vec3[] offsets = new Vec3[]{
            new Vec3(0, 0, 0),
            new Vec3(0.5, 0, 0),
            new Vec3(-0.5, 0, 0)
        };

        for (Vec3 offset : offsets) {
            Vec3 start = baseStart.add(offset);
            Vec3 end = start.add(direction.scale(15));
            AABB rayArea = new AABB(start, end);

            List<Entity> entities = this.level.getEntities(this, rayArea, e -> e instanceof LivingEntity);
            
            for (Entity entity : entities) {
            	if (!isPlayerControlled) {
            		if (entity instanceof Player) {
                        Player player = (Player) entity;
                        UUID playerId = player.getUUID();

                        if (isPlayerControlled && this.getServer().equals(player)) {
                            continue;
                        }

                        if (!alreadyHitPlayers.containsKey(playerId)) {
                            player.hurt(DamageSource.FLY_INTO_WALL, DAMAGE_AMOUNT);
                            alreadyHitPlayers.put(playerId, true);
                        }
                    }
            	} else {
            		UUID playerId = entity.getUUID();
            		if (!alreadyHitPlayers.containsKey(playerId)) {
                        entity.hurt(DamageSource.FLY_INTO_WALL, DAMAGE_AMOUNT);
                        alreadyHitPlayers.put(playerId, true);
                    }
            	} 
            }
        }
    }
    
    @Override
	public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
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
    event.getController().setAnimation(new AnimationBuilder().addAnimation("laser_sword.slam", true));
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
