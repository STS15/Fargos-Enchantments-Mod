package dev.sts15.fargos.entity.summoned;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class SummonedVex extends Vex {

    private UUID summonerUUID;
    private Player summoner;
    private final float followDistance = 8.0F;

    public SummonedVex(EntityType<? extends Vex> entityType, Level level) {
        super(entityType, level);
    }

    public void setSummoner(Player player) {
        this.summoner = player;
        this.summonerUUID = player.getUUID();
    }

    public Player getSummoner() {
        return this.summoner;
    }

    public UUID getSummonerUUID() {
        return this.summonerUUID;
    }

    @Override
    protected void registerGoals() {
        Predicate<LivingEntity> shouldAttack = (entity) -> {
            if (entity instanceof SummonedVex) {
                SummonedVex otherVex = (SummonedVex) entity;
                return !otherVex.getSummonerUUID().equals(this.getSummonerUUID());
            }
            return true;
        };

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(2, new VexCopyOwnerTargetGoal(this));
        this.goalSelector.addGoal(4, new VexChargeAttackGoal(this));
        this.goalSelector.addGoal(8, new TeleportToSummonerGoal(this, followDistance));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = findNearestHostileMob();
        if (target != null) {
        	this.setTarget(target);
        }
    }

    private LivingEntity findNearestHostileMob() {
        Player summoner = getSummoner();
        if (summoner == null) {
            //System.out.println("SummonedVex: No summoner found.");
            return null;
        }

        double searchRange = 10.0;
        AABB searchArea = new AABB(summoner.blockPosition()).inflate(searchRange);
        // System.out.println("SummonedVex: Searching for hostile mobs around player at
        // " + summoner.blockPosition());

        List<LivingEntity> potentialTargets = this.level.getEntitiesOfClass(LivingEntity.class, searchArea,
                entity -> entity instanceof Monster && entity != this && entity.isAlive());

        if (potentialTargets.isEmpty()) {
            //System.out.println("SummonedVex: No potential targets found.");
            return null;
        }

        LivingEntity nearestTarget = null;
        double nearestDistance = Double.MAX_VALUE;

        for (LivingEntity target : potentialTargets) {
            double distance = summoner.distanceToSqr(target);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestTarget = target;
            }
        }

        if (nearestTarget != null) {
        	if (nearestTarget instanceof SummonedVex) {} else {
        		return nearestTarget;
        	}
        } else {
            return null;
        }
		return nearestTarget;
    }

    static class VexChargeAttackGoal extends Goal {
        private final SummonedVex vex;

        public VexChargeAttackGoal(SummonedVex vex) {
            this.vex = vex;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingentity = vex.getTarget();
            if (livingentity != null && livingentity.isAlive() && !vex.getMoveControl().hasWanted() && vex.random.nextInt(reducedTickDelay(7)) == 0) {
                return vex.distanceToSqr(livingentity) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return vex.getMoveControl().hasWanted() && vex.isCharging() && vex.getTarget() != null && vex.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = vex.getTarget();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.getEyePosition();
                vex.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
            }

            vex.setIsCharging(true);
            vex.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
        }

        public void stop() {
            vex.setIsCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = vex.getTarget();
            if (livingentity != null) {
                if (vex.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    vex.doHurtTarget(livingentity);
                    vex.setIsCharging(false);
                } else {
                    double d0 = vex.distanceToSqr(livingentity);
                    if (d0 < 9.0D) {
                        Vec3 vec3 = livingentity.getEyePosition();
                        vex.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                    }
                }
            }
        }
    }

    static class FollowSummonerGoal extends Goal {
        private final SummonedVex vex;
        private final double speed;
        private final float maxDist;

        private LivingEntity target;
        private boolean initialTargetSet = false;

        public FollowSummonerGoal(SummonedVex vex, double speed, float maxDist) {
            this.vex = vex;
            this.speed = speed;
            this.maxDist = maxDist;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            Player summoner = vex.getSummoner();
            if (summoner == null || summoner.isSpectator()) {
                return false;
            }

            // Check if the Vex has an initial target
            if (!initialTargetSet) {
                target = vex.getTarget();
                if (target != null && target.isAlive() && summoner.distanceToSqr(target) <= maxDist * maxDist) {
                    initialTargetSet = true;
                    return true;
                }
            } else {
                // Follow the initial target
                if (target != null && target.isAlive()) {
                    return true;
                } else {
                    // If the initial target is dead, teleport back to the player
                    initialTargetSet = false;
                    return summoner.distanceToSqr(vex) > maxDist * maxDist;
                }
            }

            return false;
        }

        @Override
        public void start() {
            if (initialTargetSet) {
                this.vex.getNavigation().moveTo(target, this.speed);
            } else {
                this.vex.getNavigation().moveTo(this.vex.summoner, this.speed);
            }
        }

        @Override
        public void tick() {
            if (initialTargetSet) {
                if (this.vex.distanceToSqr(target) > maxDist * maxDist || !target.isAlive()) {
                    // If the target is out of range or dead, teleport back to the player
                    initialTargetSet = false;
                    this.vex.teleportTo(this.vex.summoner.getX(), this.vex.summoner.getY(), this.vex.summoner.getZ());
                }
            } else {
                if (this.vex.distanceToSqr(this.vex.summoner) > maxDist * maxDist) {
                    // If the Vex is out of range, teleport back to the player
                    this.vex.teleportTo(this.vex.summoner.getX(), this.vex.summoner.getY(), this.vex.summoner.getZ());
                }
            }
        }
    }

    class TeleportToSummonerGoal extends Goal {
        private final SummonedVex vex;
        private final float maxDistance;

        public TeleportToSummonerGoal(SummonedVex vex, float maxDistance) {
            this.vex = vex;
            this.maxDistance = maxDistance;
        }

        @Override
        public boolean canUse() {
            Player summoner = vex.getSummoner();
            return summoner != null && summoner.isAlive() && vex.getTarget() == null && vex.distanceToSqr(summoner) > maxDistance * maxDistance;
        }

        @Override
        public void start() {
            Player summoner = vex.getSummoner();
            if (summoner != null) {
                vex.teleportTo(summoner.getX(), summoner.getY(), summoner.getZ());
            }
        }
    }


    static class VexCopyOwnerTargetGoal extends TargetGoal {
        private final SummonedVex vex;
        private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

        public VexCopyOwnerTargetGoal(SummonedVex vex) {
            super(vex, false);
            this.vex = vex;
        }

        public boolean canUse() {
            LivingEntity owner = vex.getOwner();
            if (owner != null) {
                LivingEntity ownerTarget = owner.getLastHurtMob();
                LivingEntity vexTarget = vex.getTarget();

                return ownerTarget != null && ownerTarget.isAlive() &&
                       (vexTarget == null || !vexTarget.isAlive() || vexTarget != ownerTarget) &&
                       this.canAttack(ownerTarget, this.copyOwnerTargeting);
            }
            return false;
        }

        public void start() {
        	System.out.println("Owner attacked, switching to target: "+ vex.getOwner().getTarget() + " -- " +vex.getOwner().getTarget().position());
            vex.setTarget(vex.getOwner().getTarget());
            super.start();
        }
    }

}
