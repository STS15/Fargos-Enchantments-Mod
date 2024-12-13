package dev.sts15.fargos.jei;

import java.util.ArrayList;
import java.util.List;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.blocks.crucibleofthecosmos.CrucibleOfTheCosmosRecipeRegistry;
import dev.sts15.fargos.init.BlockInit;
import dev.sts15.fargos.init.ModMenus;
import dev.sts15.fargos.screen.crucibleofthecosmos.CrucibleOfTheCosmosMenu;
import dev.sts15.fargos.screen.crucibleofthecosmos.CrucibleOfTheCosmosScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Fargos.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CrucibleOfTheCosmosRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<CrucibleOfTheCosmosRecipe> recipeList = new ArrayList<>(CrucibleOfTheCosmosRecipeRegistry.getRecipes());
        registration.addRecipes(CrucibleOfTheCosmosRecipeCategory.CRUCIBLE_RECIPE_TYPE, recipeList);
    }


    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CrucibleOfTheCosmosScreen.class, 104, 47, 26, 25, CrucibleOfTheCosmosRecipeCategory.CRUCIBLE_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CrucibleOfTheCosmosMenu.class, ModMenus.CRUCIBLE_OF_THE_COSMOS_MENU.get(), CrucibleOfTheCosmosRecipeCategory.CRUCIBLE_RECIPE_TYPE, 0, 16, 17, 36);
    }



    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockInit.CRUCIBLE_OF_THE_COSMOS.get()), CrucibleOfTheCosmosRecipeCategory.CRUCIBLE_RECIPE_TYPE);
    }
}