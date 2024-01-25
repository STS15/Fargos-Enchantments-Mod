package dev.sts15.fargos.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FlightEffect extends MobEffect {
    public FlightEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.getAbilities().mayfly = true;
            //player.getAbilities().flying = true;
            player.getAbilities().setFlyingSpeed(0.05f);
            player.onUpdateAbilities();
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    public static void checkAndDisableFlight(Player player, MobEffect flightEffect) {
        if (!player.hasEffect(flightEffect) && !player.isCreative() && !player.isSpectator()) {
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
    }
}
