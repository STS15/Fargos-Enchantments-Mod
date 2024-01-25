package dev.sts15.fargos.events;

import net.minecraft.world.damagesource.DamageSource;

public class TrueDamageSource extends DamageSource {
    public TrueDamageSource(String damageTypeIn) {
        super(damageTypeIn);
        this.bypassArmor();
    }
}
