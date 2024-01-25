package dev.sts15.fargos.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.monster.EnderMan;
import dev.sts15.fargos.init.FargosConfig;
import dev.sts15.fargos.item.enchantments.EndermanEnchantment;
import dev.sts15.fargos.item.forces.ForceOfExplorer;
import dev.sts15.fargos.item.souls.SoulOfMinecraft;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(EnderMan.class)
public abstract class EndermanMixin {

    @Inject(method = "isLookingAtMe", at = @At("HEAD"), cancellable = true)
    private void onShouldAttackPlayer(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (player != null && (hasEndermanEnchantment(player) || (hasForceOfExplorer(player) && FargosConfig.getConfigValue("enderman_enchantment")))) {
        	cir.setReturnValue(false);
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean hasEndermanEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof EndermanEnchantment, player).isPresent();
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

