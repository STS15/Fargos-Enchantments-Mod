package dev.sts15.fargos.entity.goals.mutant;

import dev.sts15.fargos.entity.custom.MutantEntity;
import dev.sts15.fargos.entity.custom.TargetLockedEntity;
import dev.sts15.fargos.init.ModEntities;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class SummonTargetLockedGoal extends Goal {
    private final MutantEntity mutant;
    private Player target;
    private final double range;
    private int spawnCount;
    private int spawnTickCounter;
    private int cooldownTickCounter;
    private static final int SPAWN_INTERVAL_TICKS = 10;
    private static final int MAX_SPAWNS = 4;
    private static final int COOLDOWN_TICKS = 600;

    public SummonTargetLockedGoal(MutantEntity mutant, double range) {
        this.mutant = mutant;
        this.range = range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.spawnCount = 0;
        this.spawnTickCounter = 0;
        this.cooldownTickCounter = COOLDOWN_TICKS;
    }

    @Override
    public boolean canUse() {
        this.target = (Player) this.mutant.getTarget();
        if (this.target == null) {
            return false;
        }
        double distanceToTargetSquared = this.mutant.distanceToSqr(this.target);
        boolean canUse = distanceToTargetSquared <= this.range * this.range;
        return canUse;
    }

    @Override
    public void start() {
        super.start();
        this.spawnCount = 0; // Reset spawnCount when the goal starts
        this.spawnTickCounter = 0; // Reset spawnTickCounter
    }

    @Override
    public void tick() {
        if (this.spawnCount < MAX_SPAWNS) {
            this.spawnTickCounter++;
            if (this.spawnTickCounter >= SPAWN_INTERVAL_TICKS) {
                BlockPos targetPosition = this.target.blockPosition();
                summonTargetLocked(targetPosition);
                this.spawnTickCounter = 0;
                this.spawnCount++;
            }
        } else {
            this.cooldownTickCounter = 0;
            this.stop();
        }
    }

    private void summonTargetLocked(BlockPos position) {
        if (this.mutant.level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) this.mutant.level;
            TargetLockedEntity targetLockedEntity = new TargetLockedEntity(ModEntities.TARGET_LOCKED.get(), serverLevel);
            targetLockedEntity.setPos(position.getX(), position.getY(), position.getZ());
            serverLevel.addFreshEntity(targetLockedEntity);
        }
    }
}
