package dev.sts15.fargos.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import dev.sts15.fargos.entity.goals.FollowPlayerGoal;
import dev.sts15.fargos.entity.goals.abominationn.BlasterAttack1Goal;
import dev.sts15.fargos.entity.goals.abominationn.BlasterAttack2Goal;
import dev.sts15.fargos.entity.goals.abominationn.ScytheAttack1Goal;
import dev.sts15.fargos.entity.goals.abominationn.ScytheAttack2Goal;

import java.util.HashSet;

public class AbominationnEntity extends Monster implements IAnimatable {
    private final ServerBossEvent bossInfo;
    private final AnimationFactory factory;
    private boolean isAttacking;
    private boolean isBossMusicPlaying;
    private Set<UUID> playersWithStoppedMusic;
    private int scytheAttackType;
    private int blasterAttackType;
    private AttackType currentAttackType;
    
    public enum AttackType {
        SCYTHE_1,
        SCYTHE_2,
        BLASTER_1,
        BLASTER_2
    }

    public AbominationnEntity(EntityType<AbominationnEntity> type, Level world) {
        super(type, world);
        this.bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.PROGRESS);
        this.factory = GeckoLibUtil.createFactory(this);
        this.playersWithStoppedMusic = new HashSet<>();
        this.isAttacking = false;
        this.isBossMusicPlaying = false;
        this.initEntityAttributes();
    }

    private void initEntityAttributes() {
        
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        // Other goals
        //this.goalSelector.addGoal(1, new FollowPlayerGoal(this, 1.0D, 10.0F, 2.0F));

        // Custom attack goals
        //this.goalSelector.addGoal(2, new ScytheAttack1Goal(this, 3.0, 100));
        //this.goalSelector.addGoal(2, new ScytheAttack2Goal(this, 3.0, 120));
        this.goalSelector.addGoal(2, new BlasterAttack1Goal(this, 3.0, 150));
        //this.goalSelector.addGoal(2, new BlasterAttack2Goal(this, 3.0, 180));

        // Targeting player
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 1.5)
                .add(Attributes.MAX_HEALTH, 1024)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ATTACK_DAMAGE, 25)
                .add(Attributes.FOLLOW_RANGE, 50)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10)
                .add(Attributes.ATTACK_KNOCKBACK, 2);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isAttacking()) {
            switch (this.getAttackType()) {
                case SCYTHE_1:
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("abominationn.scythe_attack1", false));
                    break;
                case SCYTHE_2:
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("abominationn.scythe_attack2", false));
                    break;
                case BLASTER_1:
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("abominationn.blaster_attack1", false));
                    break;
                case BLASTER_2:
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("abominationn.blaster_attack2", false));
                    break;
                default:
                    break;
            }
        } else {
            // Default or other animations
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }
    
	public void setAttacking(boolean b) {
		isAttacking = b;
	}
	
	private boolean isAttacking() {
		return isAttacking;
	}
	
	public AttackType getAttackType() {
	    return this.currentAttackType;
	}

	public void setAttackType(AttackType attackType) {
	    this.currentAttackType = attackType;
	}
}
