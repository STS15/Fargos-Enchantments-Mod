package dev.sts15.fargos.events;

//Import statements
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.ItemStack;
import dev.sts15.fargos.item.weapons.DragonsDemise;
import net.minecraft.world.entity.player.Player;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ItemUseEventHandler {

 @SubscribeEvent
 public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
     if (event.getEntity() instanceof Player) {
         ItemStack itemStack = event.getItem();
         if (itemStack.getItem() instanceof DragonsDemise) {
             // Suppress arm swing
             event.setDuration(0);
         }
     }
 }
}
