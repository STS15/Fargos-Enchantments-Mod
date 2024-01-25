package dev.sts15.fargos.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import dev.sts15.fargos.init.FargosConfig;
import dev.sts15.fargos.item.enchantments.ArcticEnchantment;
import dev.sts15.fargos.item.enchantments.SpectralEnchantment;
import dev.sts15.fargos.item.forces.ForceOfExplorer;
import dev.sts15.fargos.item.souls.SoulOfMinecraft;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Inject(method = "canFreeze", at = @At("HEAD"), cancellable = true)
    public void onCanFreeze(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player) {
            Player player = (Player) entity;

            if (hasArcticEnchantment(player) || (hasForceOfExplorer(player) && FargosConfig.getConfigValue("arctic_enchantment"))) {
                cir.setReturnValue(false);
                return;
            }
        }
    }

	private boolean hasArcticEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ArcticEnchantment, player).isPresent();
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
