package dev.sts15.fargos.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.entity.custom.LaserSwordEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class LaserSwordRenderer extends GeoEntityRenderer<LaserSwordEntity> {
    public LaserSwordRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LaserSwordModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(LaserSwordEntity instance) {
        return new ResourceLocation(Fargos.MODID, "textures/entity/laser_sword_texture.png");
    }

    @Override
    public RenderType getRenderType(LaserSwordEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1.0f, 1.0f, 1.0f); // Adjust scale as necessary
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
