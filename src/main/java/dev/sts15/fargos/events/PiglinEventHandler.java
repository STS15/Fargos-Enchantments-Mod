package dev.sts15.fargos.events;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

import dev.sts15.fargos.item.enchantments.GoldEnchantment;

@Mod.EventBusSubscriber(modid = "fargos")
public class PiglinEventHandler {

    @SubscribeEvent
    public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
        if (event.getEntity() instanceof Piglin && event.getTarget() instanceof Player) {
            Piglin piglin = (Piglin) event.getEntity();
            Player player = (Player) event.getTarget();

            if ( CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof GoldEnchantment, player).isPresent()) {
                piglin.setTarget(null);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (!player.level.isClientSide && event.phase == TickEvent.Phase.END) {
        	if ( CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof GoldEnchantment, player).isPresent()) {
                checkNearbyPiglins(player);
            }
        }
    }

    private static void checkNearbyPiglins(Player player) {
        int radius = 10;
        AABB area = new AABB(player.blockPosition()).inflate(radius);
        List<Piglin> piglins = player.level.getEntitiesOfClass(Piglin.class, area);

        for (Piglin piglin : piglins) {
            if (piglin.getTarget() instanceof Player && piglin.getTarget().equals(player)) {
            	//System.out.println("Piglin should stop pathing");
            	piglin.getNavigation().stop();
            	piglin.setAggressive(false);
            	piglin.setTarget(null);
            }
        }
    }
    
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        // Check if the attacker is a Piglin and the target is a Player
        if (event.getSource().getEntity() instanceof Piglin && event.getEntity() instanceof Player) {
            Piglin piglin = (Piglin) event.getSource().getEntity();
            Player player = (Player) event.getEntity();

            // Check if the player has an item with the GoldEnchantment in the Curios slot
            if (CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof GoldEnchantment, player).isPresent()) {
                // Cancel the attack event
                event.setCanceled(true);
                
                // Additional actions to reset Piglin state (if needed)
                piglin.attackAnim = 0;
                piglin.animationPosition = 0;
                piglin.swinging = false;
                piglin.setTarget(null);
                piglin.setAggressive(false);
                piglin.getNavigation().stop();
            }
        }
    }
   
}
