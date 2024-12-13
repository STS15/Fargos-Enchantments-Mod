package dev.sts15.fargos.mixins;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import dev.sts15.fargos.render.AngelWingsLayer;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
//    @Inject(method = "<init>*", at = @At("TAIL"))
//    private void fargos$init(EntityRendererProvider.Context context, boolean slim, CallbackInfo ci) {
//        PlayerRenderer renderer = (PlayerRenderer)(Object)this;
//        this.addLayer(new AngelWingsLayer<>(renderer));
//    }
//
//    @Shadow
//    protected abstract void addLayer(RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> layer);
}


