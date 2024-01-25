package dev.sts15.fargos.entity.goals.mutant;

import java.util.EnumSet;
import dev.sts15.fargos.entity.custom.MutantEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import java.util.Random;

public class RandomFlyingGoal extends Goal {
    protected final MutantEntity entity;
    protected double x;
    protected double y;
    protected double z;
    private final int range;
    private int cooldownTimer = 100;

    public RandomFlyingGoal(MutantEntity entity, double speed, int range) {
        this.entity = entity;
        this.range = range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }
    
    @Override
    public void tick() {
    	decrementCooldownTimer();
    }

    @Override
    public boolean canUse() {
        // Check if the entity is currently targeting something or if the cooldown timer is active
        if (this.entity.getTarget() != null || cooldownTimer > 0) {
            return false;
        }

        // Generate a random flying position
        Vec3 randomPos = generateRandomFlyingPosition();
        if (randomPos == null) {
            return false;
        } else {
            // Set the target position
            this.x = randomPos.x;
            this.y = randomPos.y;
            this.z = randomPos.z;
            cooldownTimer = calculateDynamicCooldown();
            return true;
        }
    }


    private Vec3 generateRandomFlyingPosition() {
        if (range <= 0) {
            return null; // Ensure range is positive
        }

        Random random = new Random();
        int dx = random.nextInt(range * 2 + 1) - range;
        int dy = random.nextInt(range + 1) - range / 2; // Adjusted range for Y coordinate
        int dz = random.nextInt(range * 2 + 1) - range;

        BlockPos targetPos = new BlockPos(this.entity.getX() + dx, this.entity.getY() + dy, this.entity.getZ() + dz);

        if (this.entity.level.isEmptyBlock(targetPos)) {
            return new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ());
        } else {
            return null; // Check if the target position is not an empty block
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.entity.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1.0);
        System.out.println("RandomFlyingGoal: Goal started"); // Debugging output
    }
    
    public void resetCooldown() {
        cooldownTimer = 100; // Adjust this value as needed
    }
    
    private void decrementCooldownTimer() {
        if (cooldownTimer > 0) {
            cooldownTimer--;
        }
    }
    
    private int calculateDynamicCooldown() {
        // Example: Cooldown based on entity's current health
        return (int) (100 * (this.entity.getHealth() / this.entity.getMaxHealth()));
    }
    
    
}
