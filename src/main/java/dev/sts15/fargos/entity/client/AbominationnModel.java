package dev.sts15.fargos.entity.client;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.entity.custom.AbominationnEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AbominationnModel extends AnimatedGeoModel<AbominationnEntity> {
    @Override
    public ResourceLocation getModelResource(AbominationnEntity object) {
        return new ResourceLocation(Fargos.MODID, "geo/abominationn.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AbominationnEntity object) {
        return new ResourceLocation(Fargos.MODID, "textures/entity/abominationn_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbominationnEntity animatable) {
        return new ResourceLocation(Fargos.MODID, "animations/abominationn.animation.json");
    }

}