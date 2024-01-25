package dev.sts15.fargos.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.sts15.fargos.init.FargosConfig;
import dev.sts15.fargos.item.enchantments.EnchantingEnchantment;
import dev.sts15.fargos.item.forces.ForceOfMystic;
import dev.sts15.fargos.item.souls.SoulOfMinecraft;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;

@Mixin(AnvilMenu.class) // or the correct class for the Anvil
public abstract class AnvilMixin extends ItemCombinerMenu {
	
	public AnvilMixin() {
        super(null, 0, null, null);
    }

	@Shadow private DataSlot cost;
	
	@Inject(method = "createResult", at = @At("RETURN"), cancellable = true)
    private void onCalculateCost(CallbackInfo ci) {
        Player interactingPlayer = this.player;
        if (interactingPlayer != null && hasEnchantingEnchantment(interactingPlayer) || (hasForceOfMystic(interactingPlayer)&& FargosConfig.getConfigValue("enchanting_enchantment"))) {
            int originalCost = this.cost.get();
            int modifiedCost = Math.max(1, originalCost-(originalCost / 3));
            this.cost.set(modifiedCost);
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

