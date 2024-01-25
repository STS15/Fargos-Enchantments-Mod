package dev.sts15.fargos.entity.client;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.entity.custom.DeathSickleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DeathSickleModel extends AnimatedGeoModel<DeathSickleEntity> {
    @Override
    public ResourceLocation getModelResource(DeathSickleEntity object) {
        return new ResourceLocation(Fargos.MODID, "geo/death_sickle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DeathSickleEntity object) {
        return new ResourceLocation(Fargos.MODID, "textures/entity/death_sickle_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DeathSickleEntity animatable) {
        return new ResourceLocation(Fargos.MODID, "animations/death_sickle.animation.json");
    }

}