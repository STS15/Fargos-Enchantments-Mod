package dev.sts15.fargos.entity.client;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.entity.custom.TargetLockedEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TargetLockedModel extends AnimatedGeoModel<TargetLockedEntity> {
    @Override
    public ResourceLocation getModelResource(TargetLockedEntity object) {
        return new ResourceLocation(Fargos.MODID, "geo/target_locked.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TargetLockedEntity object) {
        return new ResourceLocation(Fargos.MODID, "textures/entity/target_locked_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TargetLockedEntity animatable) {
        return new ResourceLocation(Fargos.MODID, "animations/target_locked.animation.json");
    }

}