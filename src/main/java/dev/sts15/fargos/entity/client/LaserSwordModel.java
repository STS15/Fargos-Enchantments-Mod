package dev.sts15.fargos.entity.client;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.entity.custom.LaserSwordEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LaserSwordModel extends AnimatedGeoModel<LaserSwordEntity> {
    @Override
    public ResourceLocation getModelResource(LaserSwordEntity object) {
        return new ResourceLocation(Fargos.MODID, "geo/laser_sword.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LaserSwordEntity object) {
        return new ResourceLocation(Fargos.MODID, "textures/entity/laser_sword_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LaserSwordEntity animatable) {
        return new ResourceLocation(Fargos.MODID, "animations/laser_sword.animation.json");
    }

}