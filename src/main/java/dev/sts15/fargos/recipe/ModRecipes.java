package dev.sts15.fargos.recipe;

import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import dev.sts15.fargos.Fargos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModRecipes::registerRecipes);
    }

    private static void registerRecipes() {
    	ShapedRecipe myRecipe = createCustom4x4Recipe();
    }

    private static ShapedRecipe createCustom4x4Recipe() {
        ResourceLocation recipeId = new ResourceLocation(Fargos.MODID, "crucible_of_the_cosmos");
        NonNullList<Ingredient> inputs = NonNullList.withSize(16, Ingredient.EMPTY);

        inputs.set(0, Ingredient.of(Items.IRON_INGOT)); // Top-left corner

        ItemStack output = new ItemStack(Items.EMERALD); // Output of the recipe
        return new ShapedRecipe(recipeId, "", 4, 4, inputs, output);
    }
}