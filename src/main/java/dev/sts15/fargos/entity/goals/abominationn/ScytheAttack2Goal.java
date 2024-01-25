package dev.sts15.fargos.entity.goals.abominationn;

import dev.sts15.fargos.entity.custom.AbominationnEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class ScytheAttack2Goal extends Goal {
    private final AbominationnEntity entity;
    private final double range;
    private int cooldown;

    public ScytheAttack2Goal(AbominationnEntity entity, double range, int cooldown) {
        this.entity = entity;
        this.range = range;
        this.cooldown = cooldown;
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null &&
               this.entity.distanceToSqr(this.entity.getTarget()) <= range * range &&
               cooldown <= 0;
    }

    @Override
    public void start() {
        this.entity.setAttackType(AbominationnEntity.AttackType.SCYTHE_2);
        this.entity.setAttacking(true);
        cooldown = 100;
    }
}
