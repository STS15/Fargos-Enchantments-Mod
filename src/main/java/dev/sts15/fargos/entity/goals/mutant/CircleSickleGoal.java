package dev.sts15.fargos.entity.goals.mutant;

import net.minecraft.world.entity.ai.goal.Goal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import dev.sts15.fargos.entity.custom.DeathSickleEntity;
import dev.sts15.fargos.entity.custom.MutantEntity;
import dev.sts15.fargos.init.ModEntities;

public class CircleSickleGoal extends Goal {
	private final MutantEntity boss;
    private final double radius;
    private final List<DeathSickleEntity> sickles;
    private double currentAngle = 0;
    private static final double ANGLE_STEP = Math.toRadians(5);
    private long startTick;
    private static final long ACTIVE_DURATION_TICKS = 10 * 20;
    private long lastActivationTime;
    private static final long COOLDOWN_TICKS = 10 * 20;
    

    public CircleSickleGoal(MutantEntity boss, double radius, List<DeathSickleEntity> sickles) {
        this.boss = boss;
        this.radius = radius;
        this.sickles = sickles;
        this.lastActivationTime = -COOLDOWN_TICKS;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        long currentTime = this.boss.level.getGameTime();
        return currentTime >= lastActivationTime + COOLDOWN_TICKS;
    }

    @Override
    public boolean canContinueToUse() {
        long currentTime = this.boss.level.getGameTime();
        return currentTime < startTick + ACTIVE_DURATION_TICKS;
    }

    @Override
    public void start() {
    	this.startTick = this.boss.level.getGameTime();
        this.lastActivationTime = startTick; // Update last activation time
        //System.out.println("CircleSickleGoal started at tick: " + startTick);
        for (int i = 0; i < 360; i += 20) {
            double angle = Math.toRadians(i);
            double x = boss.getX() + radius * Math.cos(angle);
            double y = boss.getY();
            double z = boss.getZ() + radius * Math.sin(angle);

            DeathSickleEntity sickle = new DeathSickleEntity(ModEntities.DEATH_SICKLE.get(), boss.level);
            sickle.setNoGravity(true); // Make sure they are not affected by gravity
            sickle.setPos(x, y, z);
            boss.level.addFreshEntity(sickle);
            sickles.add(sickle);
        }
    }

    @Override
    public void stop() {
    	long stopTime = this.boss.level.getGameTime();
        //System.out.println("CircleSickleGoal stopped at tick: " + stopTime);
    	super.stop();
        for (DeathSickleEntity sickle : sickles) {
            sickle.discard();
        }
        sickles.clear();
        
    }

    @Override
    public void tick() {
        long currentTime = this.boss.level.getGameTime();

        // Check if we are still within the active duration
        if (currentTime < startTick + ACTIVE_DURATION_TICKS) {
            // Update the current angle for rotation
            currentAngle += ANGLE_STEP;
            if (currentAngle >= 2 * Math.PI) {
                currentAngle -= 2 * Math.PI; // Reset the angle after a full rotation
            }

            // Update positions of each sickle in the circle
            int index = 0;
            for (int i = 0; i < 360; i += 20) {
                if (index >= sickles.size()) {
                    break; // Exit if there are no more sickles to position
                }

                double angle = Math.toRadians(i) + currentAngle;
                double x = boss.getX() + radius * Math.cos(angle);
                double y = boss.getY(); // You might want to adjust this if sickles need to be at a specific height
                double z = boss.getZ() + radius * Math.sin(angle);

                DeathSickleEntity sickle = sickles.get(index);
                sickle.setPos(x, y, z); // Update sickle position
                index++;
            }
        } else {
            this.stop(); // Stop the goal after the active duration has elapsed
        }
    }

}