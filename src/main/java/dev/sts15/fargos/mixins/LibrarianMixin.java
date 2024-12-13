package dev.sts15.fargos.mixins;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.sts15.fargos.init.FargosConfig;
import dev.sts15.fargos.item.enchantments.LibrarianEnchantment;
import dev.sts15.fargos.item.forces.ForceOfMystic;
import dev.sts15.fargos.item.souls.SoulOfMinecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(Player.class)
public abstract class LibrarianMixin {
	
	@Shadow private int totalExperience;

    @Shadow private int experienceLevel;

    @Shadow private float experienceProgress;

    @Shadow public abstract void increaseScore(int score);

    @Shadow public abstract void giveExperienceLevels(int levels);

    @Shadow public abstract int getXpNeededForNextLevel();

	@Inject(method = "giveExperiencePoints", at = @At("HEAD"), cancellable = true)
    private void modifyExperiencePoints(int p_36291_, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if (hasLibrarianEnchantment(player) || (hasForceOfMystic(player)&& FargosConfig.getConfigValue(player,"librarian_enchantment"))) {
			int modifiedExperience = Math.round(p_36291_ * 1.5f);
            this.increaseScore(modifiedExperience);
            this.experienceProgress += (float)modifiedExperience / (float)this.getXpNeededForNextLevel();
            this.totalExperience = Mth.clamp(this.totalExperience + modifiedExperience, 0, Integer.MAX_VALUE);
            while(this.experienceProgress < 0.0F) {
                float f = this.experienceProgress * (float)this.getXpNeededForNextLevel();
                if (this.experienceLevel > 0) {
                    this.giveExperienceLevels(-1);
                    this.experienceProgress = 1.0F + f / (float)this.getXpNeededForNextLevel();
                } else {
                    this.giveExperienceLevels(-1);
                    this.experienceProgress = 0.0F;
                }
            }
            while(this.experienceProgress >= 1.0F) {
                this.experienceProgress = (this.experienceProgress - 1.0F) * (float)this.getXpNeededForNextLevel();
                this.giveExperienceLevels(1);
                this.experienceProgress /= (float)this.getXpNeededForNextLevel();
            }

            ci.cancel();
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean hasLibrarianEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof LibrarianEnchantment, player).isPresent();
    }
    
    @SuppressWarnings("deprecation")
    private static boolean hasForceOfMystic(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfMystic, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    @SuppressWarnings("deprecation")
    private static boolean hasSoulOfMinecraft(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof SoulOfMinecraft, player).isPresent();
    }
}

