package dev.sts15.fargos.effect; // Ensure this matches the package where you've placed this class

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class CooldownEffect extends MobEffect {
    
    // Constructor should be public and match the super constructor
    public CooldownEffect() {
        // MobEffectCategory can be NEUTRAL or HARMLESS since it doesn't have a real effect
        super(MobEffectCategory.NEUTRAL, 0xFFFFFF); // Color can be changed as needed
    }

    // Additional methods or overrides (if needed)
}
