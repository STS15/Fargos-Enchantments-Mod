package dev.sts15.fargos.JEI;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.init.BlockInit;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CrucibleOfTheCosmosRecipeCategory implements IRecipeCategory<CrucibleOfTheCosmosRecipe> {
    public static final RecipeType<CrucibleOfTheCosmosRecipe> CRUCIBLE_RECIPE_TYPE = RecipeType.create(Fargos.MODID, "crucible_of_the_cosmos", CrucibleOfTheCosmosRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    

    public CrucibleOfTheCosmosRecipeCategory(IGuiHelper guiHelper) {
    	ResourceLocation backgroundImageLocation = new ResourceLocation(Fargos.MODID, "textures/gui/crucible_of_the_cosmos_jei_gui.png");
        this.background = guiHelper.createDrawable(backgroundImageLocation, 0, 0, 160, 73);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.CRUCIBLE_OF_THE_COSMOS.get()));
    }

    @Override
    public RecipeType<CrucibleOfTheCosmosRecipe> getRecipeType() {
        return CRUCIBLE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.fargos.crucible_of_the_cosmos");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrucibleOfTheCosmosRecipe recipe, IFocusGroup focuses) {
        // Positioning the 4x4 crafting grid slots
        int xStart = 3;
        int yStart = 3;
        int xGap = 18; // Horizontal gap between slots
        int yGap = 17; // Vertical gap between slots

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            int xPosition = xStart + (i % 4) * xGap;
            int yPosition = yStart + (i / 4) * yGap;
            builder.addSlot(RecipeIngredientRole.INPUT, xPosition, yPosition)
                   .addItemStack(recipe.getIngredients().get(i));
        }

        // Positioning the result slot
        builder.addSlot(RecipeIngredientRole.OUTPUT, 129, 28)
               .addItemStack(recipe.getOutput());
    }


}