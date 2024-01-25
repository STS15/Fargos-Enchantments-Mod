package dev.sts15.fargos.entity.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkHooks;

public class FireRemoverEntity extends LivingEntity {
	private int tickCount = 0;
    private List<BlockPos> firePositions = new ArrayList<>();
    //private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public FireRemoverEntity(EntityType<? extends FireRemoverEntity> type, Level world) {
        super(type, world);
    }

    public void setFirePositions(List<BlockPos> positions) {
        this.firePositions = positions;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            tickCount++;
            if (tickCount >= 60) { // 3 seconds
                for (BlockPos pos : firePositions) {
                    if (this.level.getBlockState(pos).is(Blocks.FIRE)) {
                        this.level.removeBlock(pos, false);
                    }
                }
                this.remove(RemovalReason.DISCARDED); // Remove this entity
            }
        }
    }
    
//    @Override
//	public AnimationFactory getFactory() {
//	    return this.factory;
//	}
    
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
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
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
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
	public void setItemSlot(EquipmentSlot slot, ItemStack stack) {}

	@Override
	public HumanoidArm getMainArm() {
	    return HumanoidArm.RIGHT;
	}

//	@Override
//	public void registerControllers(AnimationData data) {
//		// TODO Auto-generated method stub
//		
//	}
}

