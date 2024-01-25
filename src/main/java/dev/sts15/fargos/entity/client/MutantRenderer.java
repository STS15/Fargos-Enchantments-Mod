package dev.sts15.fargos.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.entity.custom.MutantEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MutantRenderer extends GeoEntityRenderer<MutantEntity> {
    public MutantRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MutantModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(MutantEntity instance) {
        return new ResourceLocation(Fargos.MODID, "textures/entity/mutant_texture.png");
    }

    @Override
    public RenderType getRenderType(MutantEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(2.0f, 2.0f, 2.0f);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}