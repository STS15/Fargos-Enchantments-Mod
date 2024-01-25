package dev.sts15.fargos.entity.goals.mutant;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import dev.sts15.fargos.entity.custom.DeathSickleEntity;
import dev.sts15.fargos.entity.custom.LaserSwordEntity;
import dev.sts15.fargos.entity.custom.MutantEntity;
import dev.sts15.fargos.init.ModEntities;

public class SummonLaserSwordGoal extends Goal {
    private final MutantEntity mutant;
    private final double offsetX, offsetY, offsetZ;
    private long lastUseTime;
    private static final long COOLDOWN = 80;

    public SummonLaserSwordGoal(MutantEntity mutant, double offsetX, double offsetY, double offsetZ) {
        this.mutant = mutant;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.lastUseTime = 0;
    }

    @Override
    public boolean canUse() {
        long currentTime = this.mutant.level.getGameTime();
        return mutant.getTarget() != null && (currentTime >= lastUseTime + COOLDOWN);
    }

    @Override
    public void start() {
        super.start();
        lastUseTime = this.mutant.level.getGameTime();
        LaserSwordEntity laserSword = new LaserSwordEntity(ModEntities.LASER_SWORD.get(), mutant.level);
        Vec3 swordPosition = mutant.position().add(offsetX, offsetY, offsetZ);
        laserSword.setPos(swordPosition.x, swordPosition.y, swordPosition.z);
        laserSword.setNoGravity(true);
        
        // Set rotation towards the nearest player
        Player nearestPlayer = mutant.level.getNearestPlayer(mutant, 50); // Adjust range as needed
        if (nearestPlayer != null) {
            Vec3 playerPos = nearestPlayer.position();
            Vec3 swordPos = laserSword.position();
            double deltaX = playerPos.x - swordPos.x;
            double deltaZ = playerPos.z - swordPos.z;
            float yaw = (float)Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F; // Calculate yaw angle

            laserSword.setYRot(yaw);
            laserSword.setYHeadRot(yaw);
        }

        mutant.level.addFreshEntity(laserSword);
    }

}

