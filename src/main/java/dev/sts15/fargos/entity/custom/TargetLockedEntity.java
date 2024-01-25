package dev.sts15.fargos.entity.custom;

import java.util.Collections;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class TargetLockedEntity extends LivingEntity implements IAnimatable {
    private int explosionTimer = 0;
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private BlockPos spawnPosition;

    public TargetLockedEntity(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
		this.spawnPosition = null;
    }

    @Override
    public void tick() {
        super.tick();
        explosionTimer++;
        if (explosionTimer == 60) {
            if (this.level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel) this.level;
                serverLevel.explode(this, this.getX(), this.getY(), this.getZ(), 4.0F, false, Explosion.BlockInteraction.NONE);
            }
        }
        if (explosionTimer == 80) {
            this.remove(RemovalReason.DISCARDED);
        }
        if (this.spawnPosition != null) {
            this.setPos(this.spawnPosition.getX(), this.spawnPosition.getY(), this.spawnPosition.getZ());
        }
    }
    
    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.spawnPosition == null) {
            this.spawnPosition = this.blockPosition();
        }
    }
    
    @Override
    public void setPos(double x, double y, double z) {
        // Prevent changing the position if the spawn position is set
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
    public boolean hurt(DamageSource source, float amount) {
        if (source.isExplosion()) {
            return false; // Prevents damage and knockback from explosions
        }
        return super.hurt(source, amount); // Normal behavior for other damage sources
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
    event.getController().setAnimation(new AnimationBuilder().addAnimation("target_locked.targeted", true));
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
