package dev.sts15.fargos.entity.goals.mutant;
import java.util.EnumSet;

import dev.sts15.fargos.entity.custom.MutantEntity;


import dev.sts15.fargos.entity.goals.CustomFlyingMoveControl;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class FlyToTargetAndAttackGoal extends Goal {
    private final MutantEntity entity;
    private final double speed;
    private LivingEntity target;
    private int attackCooldown;

    public FlyToTargetAndAttackGoal(MutantEntity entity, double speed) {
        this.entity = entity;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

//    @Override
//    public boolean canUse() {
//        this.target = this.entity.getTarget();
//        //return this.target != null && this.target.isAlive() && this.entity.isFlying();
//    }

    @Override
    public boolean canContinueToUse() {
        return canUse() && !this.entity.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.entity.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        this.entity.getNavigation().moveTo(this.target, this.speed);
        this.attackCooldown = 0;
    }

    @Override
    public void tick() {
        // Rotate towards the target before moving
        this.entity.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        double distanceSq = this.entity.distanceToSqr(this.target);

        // If close enough, attack
        if (distanceSq < getAttackReachSqr(this.target)) {
            this.entity.doHurtTarget(this.target);
            //this.entity.setAttacking(true); // Indicate that the entity is attacking
        } else {
            //this.entity.setAttacking(false);
            if (distanceSq > 1) { // Check if the entity is not too close
                // Move towards the target
                CustomFlyingMoveControl moveControl = (CustomFlyingMoveControl) this.entity.getMoveControl();
                moveControl.setWantedPosition(this.target.getX(), this.target.getY(), this.target.getZ(), this.speed);
            }
        }
    }



    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return this.entity.getBbWidth() * 2.0F * this.entity.getBbWidth() * 2.0F + attackTarget.getBbWidth();
    }

	@Override
	public boolean canUse() {
		// TODO Auto-generated method stub
		return false;
	}
}
