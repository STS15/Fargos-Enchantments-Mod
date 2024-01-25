package dev.sts15.fargos.screen.crucibleofthecosmos;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.sts15.fargos.Fargos;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CrucibleOfTheCosmosScreen extends AbstractContainerScreen<CrucibleOfTheCosmosMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Fargos.MODID, "textures/gui/crucible_of_the_cosmos_gui.png"); // Replace with your mod ID

    public CrucibleOfTheCosmosScreen(CrucibleOfTheCosmosMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    protected void init() {
        super.init();
    }
    
    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        // Intentionally left blank to not render any labels like the title or inventory text
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        this.renderTooltip(pPoseStack, mouseX, mouseY);
    }
}
