package dev.sts15.fargos.mixins;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.sts15.fargos.init.FargosConfig;
import dev.sts15.fargos.item.enchantments.GoldEnchantment;
import dev.sts15.fargos.item.forces.ForceOfOverworld;
import dev.sts15.fargos.item.souls.SoulOfMinecraft;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(PiglinAi.class)
public abstract class PiglinMixin {

	@Inject(method = "findNearestValidAttackTarget", at = @At("HEAD"), cancellable = true)
	private static void onFindNearestValidAttackTarget(Piglin piglin, CallbackInfoReturnable<Optional<? extends LivingEntity>> cir) {
	    Brain<Piglin> brain = piglin.getBrain();
	    Optional<Player> optionalPlayer = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
	    if (optionalPlayer.isPresent()) {
	        Player player = optionalPlayer.get();
	        if (hasGoldEnchantment(player) || (hasForceOfOverworld(player) && FargosConfig.getConfigValue(player,"gold_enchantment"))) {
	            cir.setReturnValue(Optional.empty());
	        }
	    }
	}

    @SuppressWarnings("deprecation")
    private static boolean hasGoldEnchantment(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof GoldEnchantment, player).isPresent();
    }
    @SuppressWarnings("deprecation")
    private static boolean hasForceOfOverworld(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfOverworld, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    
    @SuppressWarnings("deprecation")
    private static boolean hasSoulOfMinecraft(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof SoulOfMinecraft, player).isPresent();
    }
}
