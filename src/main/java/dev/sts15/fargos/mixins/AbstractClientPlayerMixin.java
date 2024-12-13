package dev.sts15.fargos.mixins;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.sts15.fargos.init.EffectsInit;
import dev.sts15.fargos.render.ArmorCapeProvider;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @Inject(method = "getCloakTextureLocation", at = @At(value = "HEAD"), cancellable = true)
    public void getCloakTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
//        var player = (Player) (Object) this;
//        ItemStack itemstack = player.getItemBySlot(EquipmentSlot.CHEST);
//        if (itemstack.getItem() instanceof ArmorCapeProvider capeProvider && !player.hasEffect(EffectsInit.ANGEL_WINGS.get())) {
//            cir.setReturnValue(capeProvider.getCapeResourceLocation());
//        }
    }

    @Inject(method = "isCapeLoaded", at = @At(value = "HEAD"), cancellable = true)
    public void isCapeLoaded(CallbackInfoReturnable<Boolean> cir) {
        var player = (Player) (Object) this;
        ItemStack itemstack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (itemstack.getItem() instanceof ArmorCapeProvider capeProvider) {
            cir.setReturnValue(true);
        }
    }
}
