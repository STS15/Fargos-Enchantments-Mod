package dev.sts15.fargos.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.sts15.fargos.init.FargosConfig;
import dev.sts15.fargos.item.enchantments.EnchantingEnchantment;
import dev.sts15.fargos.item.forces.ForceOfMystic;
import dev.sts15.fargos.item.souls.SoulOfMinecraft;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import net.minecraft.world.inventory.EnchantmentMenu;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentTableMixin {


	@Shadow private int[] costs;
    private Player currentEnchantmentMenuPlayer;

    @Inject(method = "stillValid", at = @At("HEAD"))
    private void onStillValid(Player player, CallbackInfoReturnable<Boolean> ci) {
        this.currentEnchantmentMenuPlayer = player;
        //System.out.println("Player set!");
    }

    @Inject(method = "slotsChanged", at = @At("RETURN"), cancellable = true)
    private void modifyEnchantmentCosts(Container container, CallbackInfo ci) {
    	//System.out.println("Slot changed!");
        if (this.currentEnchantmentMenuPlayer != null && (hasEnchantingEnchantment(this.currentEnchantmentMenuPlayer) || (hasForceOfMystic(this.currentEnchantmentMenuPlayer) && FargosConfig.getConfigValue("enchanting_enchantment")))) {
            for (int i = 0; i < this.costs.length; i++) {
                this.costs[i] = Math.max(1, this.costs[i] - (this.costs[i] / 3));
                //System.out.println("Modifying enchantment cost for slot: " + i);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean hasEnchantingEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof EnchantingEnchantment, player).isPresent();
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

