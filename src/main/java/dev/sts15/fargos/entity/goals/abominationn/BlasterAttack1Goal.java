package dev.sts15.fargos.entity.goals.abominationn;

import dev.sts15.fargos.entity.custom.AbominationnEntity;
import dev.sts15.fargos.entity.custom.ArrowEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class BlasterAttack1Goal extends Goal {
    private final AbominationnEntity entity;
    private final double range;
    private int cooldown;
    private long lastAttackTime;

    public BlasterAttack1Goal(AbominationnEntity entity, double range, int cooldown) {
        this.entity = entity;
        this.range = range;
        this.cooldown = cooldown;
        this.lastAttackTime = 0;
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null &&
               this.entity.distanceToSqr(this.entity.getTarget()) <= range * range &&
               cooldown <= 0 &&
               (System.currentTimeMillis() - lastAttackTime) >= cooldown;
    }

    @Override
    public void start() {
        this.entity.setAttackType(AbominationnEntity.AttackType.BLASTER_1);
        this.entity.setAttacking(true);
        this.lastAttackTime = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() - this.lastAttackTime >= 420) { // 0.42 seconds
            this.fireArrow();
            this.cooldown = 100; // Reset cooldown time after attack
        }
    }

    public void fireArrow() {
        if (this.entity.level.isClientSide) {
            return; // Ensure this runs on server side only
        }

        LivingEntity target = this.entity.getTarget();
        if (target == null) {
            return; // No target to shoot at
        }

        Vec3 entityPos = this.entity.position();
        double waistHeight = entityPos.y + this.entity.getEyeHeight();

        Arrow arrow = new Arrow(this.entity.level, this.entity);
        arrow.setPos(entityPos.x, waistHeight, entityPos.z);

        Vec3 directionVec = target.position().subtract(entityPos).normalize();
        float inaccuracy = 3.0f; // Adjust inaccuracy as needed
        arrow.shoot(directionVec.x, directionVec.y, directionVec.z, 1.6f, inaccuracy);

        this.entity.level.addFreshEntity(arrow);
    }
}
