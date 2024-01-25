package dev.sts15.fargos.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.entity.custom.AbominationnEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class AbominationnRenderer extends GeoEntityRenderer<AbominationnEntity> {
    public AbominationnRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AbominationnModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(AbominationnEntity instance) {
        return new ResourceLocation(Fargos.MODID, "textures/entity/abominationn_texture.png");
    }

    @Override
    public RenderType getRenderType(AbominationnEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1.0f, 1.0f, 1.0f);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}