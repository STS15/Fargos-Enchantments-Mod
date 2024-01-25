package dev.sts15.fargos.entity.goals;

import java.util.EnumSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class FollowPlayerGoal extends Goal {
    private final Mob entity;
    private final double speed;
    private final float maxTargetDistance;
    private final float minTargetDistance;
    private Player targetPlayer;

    public FollowPlayerGoal(Mob entity, double speed, float maxTargetDistance, float minTargetDistance) {
        this.entity = entity;
        this.speed = speed;
        this.maxTargetDistance = maxTargetDistance;
        this.minTargetDistance = minTargetDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.targetPlayer = this.entity.level.getNearestPlayer(this.entity, this.maxTargetDistance);
        return this.targetPlayer != null && this.targetPlayer.distanceToSqr(this.entity) > (this.minTargetDistance * this.minTargetDistance);
    }

    @Override
    public void start() {
        this.entity.getNavigation().moveTo(this.targetPlayer, this.speed);
    }

    @Override
    public void stop() {
        this.targetPlayer = null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetPlayer != null && this.targetPlayer.distanceToSqr(this.entity) > (this.minTargetDistance * this.minTargetDistance) && !this.entity.getNavigation().isDone();
    }
}