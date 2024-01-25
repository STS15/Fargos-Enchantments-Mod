package dev.sts15.fargos.entity.goals.mutant;

import dev.sts15.fargos.entity.custom.MutantEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import java.util.EnumSet;

public class CircleAttack extends Goal {
	protected final MutantEntity entity;
    private final int range;
    private final double heightAbovePlayer = 3;
    private final double circleRadius = 6;
    private double angle = 0.0;
    private Vec3 currentTarget = null;
    private boolean isFirstTick = true;
    private final int maxRotations = 2;
    private int rotationCount = 0;
    private final long cooldownTicks = 15 * 20; // 15 seconds * 20 ticks per second
    private long lastUseTick = 0;
    private double initialAngle = 0.0;
    private boolean initialAngleSet = false;

    public CircleAttack(MutantEntity entity, int range) {
        this.entity = entity;
        this.range = range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
    	if (this.entity.level.getGameTime() - lastUseTick < cooldownTicks) {
            return false; // Cooldown period
        }
        if (this.entity.getTarget() == null) {
            return false;
        }

        double distanceToTarget = this.entity.distanceToSqr(this.entity.getTarget().getX(), this.entity.getTarget().getY(), this.entity.getTarget().getZ());
        // Activate the goal when the target is within 6 blocks, deactivate if within 3 blocks
        return distanceToTarget <= 9 * 9 && distanceToTarget > 4 * 4;
    }

    @Override
    public void tick() {
    	if (this.entity.getTarget() == null) {
            return;
        }

        LivingEntity target = this.entity.getTarget();
        Vec3 targetPos = target.position();
        
     // Initialize currentTarget if it's null or if it's the first tick of the goal
        if (currentTarget == null || isFirstTick) {
            isFirstTick = false;
            initialAngleSet = true;

            Vec3 directionToPlayer = targetPos.subtract(this.entity.position());
            initialAngle = Math.atan2(directionToPlayer.z, directionToPlayer.x);
            angle = initialAngle;

            currentTarget = new Vec3(targetPos.x + circleRadius * Math.cos(angle),
                                     targetPos.y + heightAbovePlayer,
                                     targetPos.z + circleRadius * Math.sin(angle));
        }

        if (this.entity.getHealth() <= this.entity.getMaxHealth() * 0.25) {
            // Health below 25%, teleport randomly on the circle
            if (this.entity.tickCount % 3 == 0) {
                double randomAngle = 2 * Math.PI * this.entity.getRandom().nextDouble();
                currentTarget = new Vec3(targetPos.x + circleRadius * Math.cos(randomAngle),
                                         targetPos.y + heightAbovePlayer,
                                         targetPos.z + circleRadius * Math.sin(randomAngle));
                this.entity.setPos(currentTarget.x, currentTarget.y, currentTarget.z);
            }
        } else {
        	if (!initialAngleSet || isFirstTick) {
                isFirstTick = false;
                initialAngleSet = true;

                initialAngle = Math.atan2(targetPos.z - this.entity.getZ(), targetPos.x - this.entity.getX());
                angle = initialAngle;
                rotationCount = 0; // Reset the rotation count
            }

            double oldAngle = angle;
            angle += Math.PI / 90; // Increment by 2 degrees
            if (angle >= 2 * Math.PI) {
                angle -= 2 * Math.PI;
            }

            if (oldAngle > angle) {
                rotationCount++; // Completed a full rotation
            }

            currentTarget = new Vec3(targetPos.x + circleRadius * Math.cos(angle),
                                     targetPos.y + heightAbovePlayer,
                                     targetPos.z + circleRadius * Math.sin(angle));
            this.entity.setPos(currentTarget.x, currentTarget.y, currentTarget.z);

            if (rotationCount >= maxRotations) {
                lastUseTick = this.entity.level.getGameTime(); // Start cooldown
                initialAngleSet = false; // Reset for next use
                this.stop(); // Stop the current goal
            }
        }

     // Determine the frequency and inaccuracy of arrow shooting
        int arrowShootFrequency = this.entity.getHealth() <= this.entity.getMaxHealth() * 0.25 ? 15 : 40; // Shoot every second if health below 25%
        float inaccuracy = this.entity.getHealth() <= this.entity.getMaxHealth() * 0.25 ? 6.0f : 3.0f; // Double the inaccuracy if health below 25%

        // Shoot an arrow based on the determined frequency
        if (this.entity.tickCount % arrowShootFrequency == 0) { 
            Vec3 entityPos = this.entity.position();
            double waistHeight = entityPos.y + this.entity.getEyeHeight();

            Arrow arrow = new Arrow(this.entity.level, this.entity);
            arrow.setPos(entityPos.x, waistHeight, entityPos.z);

            Vec3 directionVec = targetPos.subtract(entityPos).normalize();
            arrow.shoot(directionVec.x, directionVec.y, directionVec.z, 1.6f, inaccuracy);

            this.entity.level.addFreshEntity(arrow);
        }

        // Rotation to face the player
        Vec3 directionToTarget = targetPos.subtract(this.entity.position());
        double angleToTarget = Math.atan2(directionToTarget.z, directionToTarget.x);
        this.entity.yBodyRot = (float) Math.toDegrees(angleToTarget) - 90.0f;
        this.entity.setYRot(this.entity.yBodyRot); // Aligning body rotation with head rotation

        // Particle effects
        if (this.entity.level instanceof ServerLevel) {
            ((ServerLevel) this.entity.level).sendParticles(ParticleTypes.FLAME, currentTarget.x, currentTarget.y, currentTarget.z, 1, 0, 0, 0, 0.01);
            spawnSoulFireCircle((ServerLevel) this.entity.level, targetPos, circleRadius);
        }
    }

    private void spawnSoulFireCircle(ServerLevel world, Vec3 center, double radius) {
        for (int i = 0; i < 360; i += 10) { // Adjust the step for more or fewer particles
            double angle = Math.toRadians(i);
            double particleX = center.x + radius * Math.cos(angle);
            double particleY = center.y + heightAbovePlayer; // Height offset
            double particleZ = center.z + radius * Math.sin(angle);

            world.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, 
                                particleX, particleY, particleZ, 
                                1, 0, 0, 0, 0);
        }
    }
}
