package dev.sts15.fargos.entity.client;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.entity.custom.MutantEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MutantModel extends AnimatedGeoModel<MutantEntity> {
    @Override
    public ResourceLocation getModelResource(MutantEntity object) {
        return new ResourceLocation(Fargos.MODID, "geo/mutant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MutantEntity object) {
        return new ResourceLocation(Fargos.MODID, "textures/entity/mutant_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MutantEntity animatable) {
        return new ResourceLocation(Fargos.MODID, "animations/mutant.animation.json");
    }

}