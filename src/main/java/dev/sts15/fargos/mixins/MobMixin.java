package dev.sts15.fargos.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.Mob;
import dev.sts15.fargos.init.FargosConfig;
import dev.sts15.fargos.item.enchantments.SpectralEnchantment;
import dev.sts15.fargos.item.forces.ForceOfExplorer;
import dev.sts15.fargos.item.souls.SoulOfMinecraft;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(Mob.class)
public abstract class MobMixin {

	@Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        if ((Object) this instanceof Phantom) {
            Phantom phantom = (Phantom) (Object) this;
            if (phantom.getTarget() instanceof Player) {
                Player targetPlayer = (Player) phantom.getTarget();
                if (hasSpectralEnchantment(targetPlayer) || (hasForceOfExplorer(targetPlayer) && FargosConfig.getConfigValue("spectral_enchantment"))) {
                    phantom.setTarget(null);
                }
            }
        }
    }

	private boolean hasSpectralEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof SpectralEnchantment, player).isPresent();
    }

	@SuppressWarnings("deprecation")
    private static boolean hasForceOfExplorer(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfExplorer, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    
    @SuppressWarnings("deprecation")
    private static boolean hasSoulOfMinecraft(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof SoulOfMinecraft, player).isPresent();
    }
}
