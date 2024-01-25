package dev.sts15.fargos.blocks.crucibleofthecosmos;

import com.google.common.collect.ImmutableList;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.JEI.CrucibleOfTheCosmosRecipe;
import dev.sts15.fargos.init.ItemInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.Enchantment;

import net.minecraft.world.level.ItemLike;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrucibleOfTheCosmosRecipeRegistry {
    private static final List<CrucibleOfTheCosmosRecipe> recipes = new ArrayList<>();

    public static void init() {
    	Fargos.LOGGER.debug("Adding custom Crucible of the Cosmos recipes");
    	airEnchantment();
        amethystEnchantment();
        appleEnchantment();
        architectEnchantment();
        arcticEnchantment();
        battleEnchantment();
        blazeEnchantment();
        cactusEnchantment();
        copperEnchantment();
        creeperEnchantment();
        diamondEnchantment();
        dragonEnchantment();
        earthEnchantment();
        emeraldEnchantment();
        enchantingEnchantment();
        endermanEnchantment();
        fireEnchantment();
        ghastEnchantment();
        goldEnchantment();
        ironGolemEnchantment();
        lapisEnchantment();
        librarianEnchantment();
        mooshroomEnchantment();
        netherStarEnchantment();
        obsidianEnchantment();
        pickaxeEnchantment();
        redstoneEnchantment();
        shulkerEnchantment();
        skeletonEnchantment();
        spectralEnchantment();
        undyingEnchantment();
        vampiricEnchantment();
        vindicatorEnchantment();
        voidEnchantment();
        waterEnchantment();
        witchEnchantment();
        witherEnchantment();
        zombieEnchantment();
        
        forceOfOverworld();
        forceOfNature();
        forceOfRejectors();
        forceOfMystic();
        forceOfWarrior();
        forceOfExplorer();
        
        soulOfMinecraft();
        soulOfFlightMastery();
        soulOfColossus();
        soulOfSupersonic();
        //soulOfTrawler();
        //soulOfDimensions();
    }

    public static void addRecipe(CrucibleOfTheCosmosRecipe recipe) {
        recipes.add(recipe);
    }

    public static ItemStack getOutput(NonNullList<ItemStack> inputs) {
        for (CrucibleOfTheCosmosRecipe recipe : recipes) {
            if (recipe.matches(inputs)) {
                return recipe.getOutput().copy();
            }
        }
        return ItemStack.EMPTY;
    }

    public static ImmutableList<CrucibleOfTheCosmosRecipe> getRecipes() {
        return ImmutableList.copyOf(recipes);
    }
    
    public static void airEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.FEATHER));
        EnchantmentIngredients.set(1, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(2, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(3, new ItemStack(Items.FEATHER));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.ELYTRA));
        EnchantmentIngredients.set(7, new ItemStack(Items.PHANTOM_MEMBRANE));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(9, new ItemStack(Items.SLIME_BLOCK));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.PHANTOM_MEMBRANE));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.FEATHER));
        EnchantmentIngredients.set(13, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(14, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(15, new ItemStack(Items.FEATHER));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:air_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void amethystEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(1, new ItemStack(Items.AMETHYST_BLOCK));
        EnchantmentIngredients.set(2, new ItemStack(Items.AMETHYST_BLOCK));
        EnchantmentIngredients.set(3, new ItemStack(Items.NETHERITE_INGOT));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.AMETHYST_BLOCK));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(6, new ItemStack(Items.SHIELD));
        EnchantmentIngredients.set(7, new ItemStack(Items.AMETHYST_BLOCK));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.AMETHYST_BLOCK));
        EnchantmentIngredients.set(9, new ItemStack(Items.SHIELD));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(11, new ItemStack(Items.AMETHYST_BLOCK));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(13, new ItemStack(Items.AMETHYST_BLOCK));
        EnchantmentIngredients.set(14, new ItemStack(Items.AMETHYST_BLOCK));
        EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:amathyst_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void appleEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(1, new ItemStack(Items.APPLE));
        EnchantmentIngredients.set(2, new ItemStack(Items.RABBIT_STEW));
        EnchantmentIngredients.set(3, new ItemStack(Items.NETHERITE_INGOT));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.CAKE));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(6, new ItemStack(Items.GOLDEN_APPLE));
        EnchantmentIngredients.set(7, new ItemStack(Items.COOKIE));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.PUMPKIN_PIE));
        EnchantmentIngredients.set(9, new ItemStack(Items.GOLDEN_APPLE));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(11, new ItemStack(Items.BEETROOT_SOUP));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(13, new ItemStack(Items.RABBIT_STEW));
        EnchantmentIngredients.set(14, new ItemStack(Items.APPLE));
        EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:apple_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void architectEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(1, new ItemStack(Items.CHORUS_FRUIT));
        EnchantmentIngredients.set(2, new ItemStack(Items.ENDER_PEARL));
        EnchantmentIngredients.set(3, new ItemStack(Items.NETHERITE_INGOT));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.CHORUS_FLOWER));
        EnchantmentIngredients.set(5, new ItemStack(Items.ENDER_EYE));
        EnchantmentIngredients.set(6, new ItemStack(Items.ENDER_CHEST));
        EnchantmentIngredients.set(7, new ItemStack(Items.BEACON));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.BEACON));
        EnchantmentIngredients.set(9, new ItemStack(Items.ENDER_CHEST));
        EnchantmentIngredients.set(10, new ItemStack(Items.ENDER_EYE));
        EnchantmentIngredients.set(11, new ItemStack(Items.CHORUS_FLOWER));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(13, new ItemStack(Items.ENDER_PEARL));
        EnchantmentIngredients.set(14, new ItemStack(Items.CHORUS_FRUIT));
        EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:architect_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void arcticEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.POWDER_SNOW_BUCKET));
        EnchantmentIngredients.set(1, new ItemStack(Items.SNOW_BLOCK));
        EnchantmentIngredients.set(2, new ItemStack(Items.SNOW_BLOCK));
        EnchantmentIngredients.set(3, new ItemStack(Items.POWDER_SNOW_BUCKET));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.SNOWBALL));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.LEATHER_CHESTPLATE));
        EnchantmentIngredients.set(7, new ItemStack(Items.SNOW_BLOCK));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.SNOW_BLOCK));
        EnchantmentIngredients.set(9, new ItemStack(Items.LEATHER_LEGGINGS));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.SNOWBALL));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.POWDER_SNOW_BUCKET));
        EnchantmentIngredients.set(13, new ItemStack(Items.SNOW_BLOCK));
        EnchantmentIngredients.set(14, new ItemStack(Items.SNOW_BLOCK));
        EnchantmentIngredients.set(15, new ItemStack(Items.POWDER_SNOW_BUCKET));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:arctic_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void battleEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(1, new ItemStack(Items.COOKED_RABBIT));
        EnchantmentIngredients.set(2, new ItemStack(Items.COOKED_MUTTON));
        EnchantmentIngredients.set(3, new ItemStack(Items.NETHERITE_INGOT));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.COOKED_BEEF));
        EnchantmentIngredients.set(5, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(7, new ItemStack(Items.COOKED_COD));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.COOKED_CHICKEN));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(10, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
        EnchantmentIngredients.set(11, new ItemStack(Items.COOKED_PORKCHOP));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(13, new ItemStack(Items.COOKED_SALMON));
        EnchantmentIngredients.set(14, new ItemStack(Items.COOKED_BEEF));
        EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:battle_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void blazeEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(1, new ItemStack(Items.BLAZE_ROD));
        EnchantmentIngredients.set(2, new ItemStack(Items.BLAZE_ROD));
        EnchantmentIngredients.set(3, new ItemStack(Items.NETHERITE_INGOT));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.BLAZE_ROD));
        EnchantmentIngredients.set(5, new ItemStack(Items.FLINT_AND_STEEL));
        EnchantmentIngredients.set(6, new ItemStack(Items.COAL_BLOCK));
        EnchantmentIngredients.set(7, new ItemStack(Items.BLAZE_ROD));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.BLAZE_ROD));
        EnchantmentIngredients.set(9, new ItemStack(Items.MAGMA_CREAM));
        EnchantmentIngredients.set(10, new ItemStack(Items.MAGMA_BLOCK));
        EnchantmentIngredients.set(11, new ItemStack(Items.BLAZE_ROD));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(13, new ItemStack(Items.BLAZE_ROD));
        EnchantmentIngredients.set(14, new ItemStack(Items.BLAZE_ROD));
        EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:blaze_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }

    public static void cactusEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        ItemStack harmingPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HARMING);

        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.CACTUS));
        EnchantmentIngredients.set(1, new ItemStack(Items.SHIELD));
        EnchantmentIngredients.set(2, new ItemStack(Items.SHIELD));
        EnchantmentIngredients.set(3, new ItemStack(Items.CACTUS));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.POINTED_DRIPSTONE));
        EnchantmentIngredients.set(5, new ItemStack(Items.WITHER_ROSE));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(7, new ItemStack(Items.POINTED_DRIPSTONE));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.POINTED_DRIPSTONE));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(10, harmingPotion);
        EnchantmentIngredients.set(11, new ItemStack(Items.POINTED_DRIPSTONE));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.CACTUS));
        EnchantmentIngredients.set(13, new ItemStack(Items.SHIELD));
        EnchantmentIngredients.set(14, new ItemStack(Items.SHIELD));
        EnchantmentIngredients.set(15, new ItemStack(Items.CACTUS));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:cactus_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void copperEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.COPPER_BLOCK));
        EnchantmentIngredients.set(1, new ItemStack(Items.COPPER_INGOT));
        EnchantmentIngredients.set(2, new ItemStack(Items.COPPER_INGOT));
        EnchantmentIngredients.set(3, new ItemStack(Items.COPPER_BLOCK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.COPPER_INGOT));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.DIAMOND));
        EnchantmentIngredients.set(7, new ItemStack(Items.COPPER_INGOT));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.COPPER_INGOT));
        EnchantmentIngredients.set(9, new ItemStack(Items.DIAMOND));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.COPPER_INGOT));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.COPPER_BLOCK));
        EnchantmentIngredients.set(13, new ItemStack(Items.COPPER_INGOT));
        EnchantmentIngredients.set(14, new ItemStack(Items.COPPER_INGOT));
        EnchantmentIngredients.set(15, new ItemStack(Items.COPPER_BLOCK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:copper_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void creeperEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.CREEPER_HEAD));
        EnchantmentIngredients.set(1, new ItemStack(Items.TNT));
        EnchantmentIngredients.set(2, new ItemStack(Items.TNT));
        EnchantmentIngredients.set(3, new ItemStack(Items.CREEPER_HEAD));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.GUNPOWDER));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.TNT_MINECART));
        EnchantmentIngredients.set(7, new ItemStack(Items.GUNPOWDER));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.GUNPOWDER));
        EnchantmentIngredients.set(9, new ItemStack(Items.TNT_MINECART));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.GUNPOWDER));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.CREEPER_HEAD));
        EnchantmentIngredients.set(13, new ItemStack(Items.TNT));
        EnchantmentIngredients.set(14, new ItemStack(Items.TNT));
        EnchantmentIngredients.set(15, new ItemStack(Items.CREEPER_HEAD));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:creeper_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void diamondEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.DIAMOND_BLOCK));
        EnchantmentIngredients.set(1, new ItemStack(Items.DIAMOND));
        EnchantmentIngredients.set(2, new ItemStack(Items.DIAMOND));
        EnchantmentIngredients.set(3, new ItemStack(Items.DIAMOND_BLOCK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(ItemInit.IRON_ENCHANTMENT.get()));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(7, new ItemStack(ItemInit.IRON_ENCHANTMENT.get()));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(ItemInit.IRON_ENCHANTMENT.get()));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(11, new ItemStack(ItemInit.IRON_ENCHANTMENT.get()));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.DIAMOND_BLOCK));
        EnchantmentIngredients.set(13, new ItemStack(Items.DIAMOND));
        EnchantmentIngredients.set(14, new ItemStack(Items.DIAMOND));
        EnchantmentIngredients.set(15, new ItemStack(Items.DIAMOND_BLOCK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:diamond_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void dragonEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.DRAGON_BREATH));
        EnchantmentIngredients.set(1, new ItemStack(Items.END_ROD));
        EnchantmentIngredients.set(2, new ItemStack(Items.END_ROD));
        EnchantmentIngredients.set(3, new ItemStack(Items.DRAGON_BREATH));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.END_ROD));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.DRAGON_HEAD));
        EnchantmentIngredients.set(7, new ItemStack(Items.END_ROD));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.END_ROD));
        EnchantmentIngredients.set(9, new ItemStack(Items.DRAGON_EGG));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.END_ROD));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.DRAGON_BREATH));
        EnchantmentIngredients.set(13, new ItemStack(Items.END_ROD));
        EnchantmentIngredients.set(14, new ItemStack(Items.END_ROD));
        EnchantmentIngredients.set(15, new ItemStack(Items.DRAGON_BREATH));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:dragon_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void earthEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.STONE));
        EnchantmentIngredients.set(1, new ItemStack(Items.DIRT));
        EnchantmentIngredients.set(2, new ItemStack(Items.GRASS_BLOCK));
        EnchantmentIngredients.set(3, new ItemStack(Items.STONE));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.STONE));
        EnchantmentIngredients.set(5, new ItemStack(Items.RAW_IRON));
        EnchantmentIngredients.set(6, new ItemStack(Items.DIAMOND));
        EnchantmentIngredients.set(7, new ItemStack(Items.STONE));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.STONE));
        EnchantmentIngredients.set(9, new ItemStack(Items.DIAMOND));
        EnchantmentIngredients.set(10, new ItemStack(Items.CLAY));
        EnchantmentIngredients.set(11, new ItemStack(Items.STONE));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.STONE));
        EnchantmentIngredients.set(13, new ItemStack(Items.GRASS_BLOCK));
        EnchantmentIngredients.set(14, new ItemStack(Items.DIRT));
        EnchantmentIngredients.set(15, new ItemStack(Items.STONE));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:earth_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void emeraldEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.EMERALD_BLOCK));
        EnchantmentIngredients.set(1, new ItemStack(Items.EMERALD));
        EnchantmentIngredients.set(2, new ItemStack(Items.EMERALD));
        EnchantmentIngredients.set(3, new ItemStack(Items.EMERALD_BLOCK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.EMERALD));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.BONE_MEAL));
        EnchantmentIngredients.set(7, new ItemStack(Items.EMERALD));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.EMERALD));
        EnchantmentIngredients.set(9, new ItemStack(Items.CROSSBOW));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.EMERALD));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.EMERALD_BLOCK));
        EnchantmentIngredients.set(13, new ItemStack(Items.EMERALD));
        EnchantmentIngredients.set(14, new ItemStack(Items.EMERALD));
        EnchantmentIngredients.set(15, new ItemStack(Items.EMERALD_BLOCK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:emerald_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void enchantingEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        ItemStack fortuneBook = new ItemStack(Items.ENCHANTED_BOOK);
        Map<Enchantment, Integer> fortuneEnchants = new HashMap<>();
        fortuneEnchants.put(Enchantments.BLOCK_FORTUNE, 3); // Fortune III
        EnchantmentHelper.setEnchantments(fortuneEnchants, fortuneBook);
        
        ItemStack lootingBook = new ItemStack(Items.ENCHANTED_BOOK);
        Map<Enchantment, Integer> lootingEnchants = new HashMap<>();
        lootingEnchants.put(Enchantments.MOB_LOOTING, 3); // Looting III
        EnchantmentHelper.setEnchantments(lootingEnchants, lootingBook);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.BOOKSHELF));
        EnchantmentIngredients.set(1, new ItemStack(Items.ENDER_EYE));
        EnchantmentIngredients.set(2, new ItemStack(Items.ENCHANTING_TABLE));
        EnchantmentIngredients.set(3, new ItemStack(Items.BOOKSHELF));
        
        // Row 2
        EnchantmentIngredients.set(4, fortuneBook);
        EnchantmentIngredients.set(5, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(7, new ItemStack(Items.BOOK));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.BOOK));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(10, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
        EnchantmentIngredients.set(11, lootingBook);

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.BOOKSHELF));
        EnchantmentIngredients.set(13, new ItemStack(Items.ENCHANTING_TABLE));
        EnchantmentIngredients.set(14, new ItemStack(Items.ENDER_EYE));
        EnchantmentIngredients.set(15, new ItemStack(Items.BOOKSHELF));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:enchanting_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void endermanEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.ENDER_PEARL));
        EnchantmentIngredients.set(1, new ItemStack(Items.CARVED_PUMPKIN));
        EnchantmentIngredients.set(2, new ItemStack(Items.CARVED_PUMPKIN));
        EnchantmentIngredients.set(3, new ItemStack(Items.ENDER_PEARL));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.CARVED_PUMPKIN));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.ENDER_EYE));
        EnchantmentIngredients.set(7, new ItemStack(Items.CARVED_PUMPKIN));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.CARVED_PUMPKIN));
        EnchantmentIngredients.set(9, new ItemStack(Items.ENDER_EYE));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.CARVED_PUMPKIN));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.ENDER_PEARL));
        EnchantmentIngredients.set(13, new ItemStack(Items.CARVED_PUMPKIN));
        EnchantmentIngredients.set(14, new ItemStack(Items.CARVED_PUMPKIN));
        EnchantmentIngredients.set(15, new ItemStack(Items.ENDER_PEARL));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:enderman_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void fireEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.BLAZE_ROD));
        EnchantmentIngredients.set(1, new ItemStack(Items.OAK_PLANKS));
        EnchantmentIngredients.set(2, new ItemStack(Items.OAK_PLANKS));
        EnchantmentIngredients.set(3, new ItemStack(Items.FLINT_AND_STEEL));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.OAK_PLANKS));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(6, new ItemStack(Items.FIRE_CHARGE));
        EnchantmentIngredients.set(7, new ItemStack(Items.MAGMA_CREAM));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.MAGMA_CREAM));
        EnchantmentIngredients.set(9, new ItemStack(Items.CAMPFIRE));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.OAK_PLANKS));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.FLINT_AND_STEEL));
        EnchantmentIngredients.set(13, new ItemStack(Items.OAK_PLANKS));
        EnchantmentIngredients.set(14, new ItemStack(Items.OAK_PLANKS));
        EnchantmentIngredients.set(15, new ItemStack(Items.BLAZE_ROD));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:fire_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void ghastEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.GHAST_TEAR));
        EnchantmentIngredients.set(1, new ItemStack(Items.GLOWSTONE_DUST));
        EnchantmentIngredients.set(2, new ItemStack(Items.GLOWSTONE_DUST));
        EnchantmentIngredients.set(3, new ItemStack(Items.GHAST_TEAR));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.GLOWSTONE_DUST));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(6, new ItemStack(Items.WITHER_ROSE));
        EnchantmentIngredients.set(7, new ItemStack(Items.GLOWSTONE_DUST));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.GLOWSTONE_DUST));
        EnchantmentIngredients.set(9, new ItemStack(Items.WITHER_SKELETON_SKULL));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(11, new ItemStack(Items.GLOWSTONE_DUST));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.GHAST_TEAR));
        EnchantmentIngredients.set(13, new ItemStack(Items.GLOWSTONE_DUST));
        EnchantmentIngredients.set(14, new ItemStack(Items.GLOWSTONE_DUST));
        EnchantmentIngredients.set(15, new ItemStack(Items.GHAST_TEAR));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:ghast_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void goldEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.GOLD_BLOCK));
        EnchantmentIngredients.set(1, new ItemStack(Items.GOLD_INGOT));
        EnchantmentIngredients.set(2, new ItemStack(Items.GOLD_INGOT));
        EnchantmentIngredients.set(3, new ItemStack(Items.GOLD_BLOCK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.GOLD_INGOT));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(6, new ItemStack(Items.CROSSBOW));
        EnchantmentIngredients.set(7, new ItemStack(Items.GOLD_INGOT));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.GOLD_INGOT));
        EnchantmentIngredients.set(9, new ItemStack(Items.SHIELD));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(11, new ItemStack(Items.GOLD_INGOT));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.GOLD_BLOCK));
        EnchantmentIngredients.set(13, new ItemStack(Items.GOLD_INGOT));
        EnchantmentIngredients.set(14, new ItemStack(Items.GOLD_INGOT));
        EnchantmentIngredients.set(15, new ItemStack(Items.GOLD_BLOCK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:gold_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void ironGolemEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        ItemStack customPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_REGENERATION);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.IRON_BLOCK));
        EnchantmentIngredients.set(1, new ItemStack(Items.CARVED_PUMPKIN));
        EnchantmentIngredients.set(2, new ItemStack(Items.CARVED_PUMPKIN));
        EnchantmentIngredients.set(3, new ItemStack(Items.IRON_BLOCK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.IRON_BLOCK));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(6, new ItemStack(Items.POPPED_CHORUS_FRUIT));
        EnchantmentIngredients.set(7, new ItemStack(Items.IRON_BLOCK));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.IRON_BLOCK));
        EnchantmentIngredients.set(9, customPotion);
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(11, new ItemStack(Items.IRON_BLOCK));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.IRON_BLOCK));
        EnchantmentIngredients.set(13, new ItemStack(Items.ROSE_BUSH));
        EnchantmentIngredients.set(14, new ItemStack(Items.ROSE_BUSH));
        EnchantmentIngredients.set(15, new ItemStack(Items.IRON_BLOCK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:iron_golem_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void lapisEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.LAPIS_BLOCK));
        EnchantmentIngredients.set(1, new ItemStack(Items.LAPIS_LAZULI));
        EnchantmentIngredients.set(2, new ItemStack(Items.LAPIS_LAZULI));
        EnchantmentIngredients.set(3, new ItemStack(Items.LAPIS_BLOCK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.LAPIS_LAZULI));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.ENCHANTING_TABLE));
        EnchantmentIngredients.set(7, new ItemStack(Items.LAPIS_LAZULI));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.LAPIS_LAZULI));
        EnchantmentIngredients.set(9, new ItemStack(Items.BOOK));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.LAPIS_LAZULI));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.LAPIS_BLOCK));
        EnchantmentIngredients.set(13, new ItemStack(Items.LAPIS_LAZULI));
        EnchantmentIngredients.set(14, new ItemStack(Items.LAPIS_LAZULI));
        EnchantmentIngredients.set(15, new ItemStack(Items.LAPIS_BLOCK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:lapis_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void librarianEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.BOOK));
        EnchantmentIngredients.set(1, new ItemStack(Items.PAPER));
        EnchantmentIngredients.set(2, new ItemStack(Items.PAPER));
        EnchantmentIngredients.set(3, new ItemStack(Items.BOOK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.BOOK));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(7, new ItemStack(Items.BOOK));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.BOOK));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(11, new ItemStack(Items.BOOK));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.BOOK));
        EnchantmentIngredients.set(13, new ItemStack(Items.PAPER));
        EnchantmentIngredients.set(14, new ItemStack(Items.PAPER));
        EnchantmentIngredients.set(15, new ItemStack(Items.BOOK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:librarian_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void mooshroomEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.MUSHROOM_STEM));
        EnchantmentIngredients.set(1, new ItemStack(Items.BROWN_MUSHROOM));
        EnchantmentIngredients.set(2, new ItemStack(Items.RED_MUSHROOM));
        EnchantmentIngredients.set(3, new ItemStack(Items.MUSHROOM_STEM));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.BROWN_MUSHROOM_BLOCK));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(6, new ItemStack(Items.BEEF));
        EnchantmentIngredients.set(7, new ItemStack(Items.RED_MUSHROOM_BLOCK));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.RED_MUSHROOM_BLOCK));
        EnchantmentIngredients.set(9, new ItemStack(Items.BEEF));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(11, new ItemStack(Items.BROWN_MUSHROOM_BLOCK));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.MUSHROOM_STEM));
        EnchantmentIngredients.set(13, new ItemStack(Items.RED_MUSHROOM));
        EnchantmentIngredients.set(14, new ItemStack(Items.BROWN_MUSHROOM));
        EnchantmentIngredients.set(15, new ItemStack(Items.MUSHROOM_STEM));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:mooshroom_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void netherStarEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(1, new ItemStack(Items.SOUL_SAND));
        EnchantmentIngredients.set(2, new ItemStack(Items.SOUL_SAND));
        EnchantmentIngredients.set(3, new ItemStack(Items.NETHER_STAR));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.SOUL_SAND));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(6, new ItemStack(Items.WITHER_SKELETON_SKULL));
        EnchantmentIngredients.set(7, new ItemStack(Items.SOUL_SAND));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.SOUL_SAND));
        EnchantmentIngredients.set(9, new ItemStack(Items.WITHER_SKELETON_SKULL));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(11, new ItemStack(Items.SOUL_SAND));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(13, new ItemStack(Items.SOUL_SAND));
        EnchantmentIngredients.set(14, new ItemStack(Items.SOUL_SAND));
        EnchantmentIngredients.set(15, new ItemStack(Items.NETHER_STAR));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:nether_star_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void obsidianEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.OBSIDIAN));
        EnchantmentIngredients.set(1, new ItemStack(Items.CRYING_OBSIDIAN));
        EnchantmentIngredients.set(2, new ItemStack(Items.CRYING_OBSIDIAN));
        EnchantmentIngredients.set(3, new ItemStack(Items.OBSIDIAN));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.CRYING_OBSIDIAN));
        EnchantmentIngredients.set(5, new ItemStack(Items.TNT));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(7, new ItemStack(Items.CRYING_OBSIDIAN));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.CRYING_OBSIDIAN));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(10, new ItemStack(Items.TNT));
        EnchantmentIngredients.set(11, new ItemStack(Items.CRYING_OBSIDIAN));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.OBSIDIAN));
        EnchantmentIngredients.set(13, new ItemStack(Items.CRYING_OBSIDIAN));
        EnchantmentIngredients.set(14, new ItemStack(Items.CRYING_OBSIDIAN));
        EnchantmentIngredients.set(15, new ItemStack(Items.OBSIDIAN));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:obsidian_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void pickaxeEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.WOODEN_PICKAXE));
        EnchantmentIngredients.set(1, new ItemStack(Items.ANDESITE));
        EnchantmentIngredients.set(2, new ItemStack(Items.DIORITE));
        EnchantmentIngredients.set(3, new ItemStack(Items.IRON_PICKAXE));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.RAW_COPPER));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.GOLDEN_PICKAXE));
        EnchantmentIngredients.set(7, new ItemStack(Items.RAW_GOLD));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.RAW_IRON));
        EnchantmentIngredients.set(9, new ItemStack(Items.GOLDEN_PICKAXE));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.DIAMOND));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.DIAMOND_PICKAXE));
        EnchantmentIngredients.set(13, new ItemStack(Items.GRANITE));
        EnchantmentIngredients.set(14, new ItemStack(Items.STONE));
        EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_PICKAXE));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:pickaxe_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void redstoneEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.REDSTONE_BLOCK));
        EnchantmentIngredients.set(1, new ItemStack(Items.REDSTONE));
        EnchantmentIngredients.set(2, new ItemStack(Items.REDSTONE));
        EnchantmentIngredients.set(3, new ItemStack(Items.REDSTONE_BLOCK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.REDSTONE));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(6, new ItemStack(Items.REDSTONE_LAMP));
        EnchantmentIngredients.set(7, new ItemStack(Items.REDSTONE));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.REDSTONE));
        EnchantmentIngredients.set(9, new ItemStack(Items.REPEATER));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(11, new ItemStack(Items.REDSTONE));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.REDSTONE_BLOCK));
        EnchantmentIngredients.set(13, new ItemStack(Items.REDSTONE));
        EnchantmentIngredients.set(14, new ItemStack(Items.REDSTONE));
        EnchantmentIngredients.set(15, new ItemStack(Items.REDSTONE_BLOCK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:redstone_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void shulkerEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.SHULKER_SHELL));
        EnchantmentIngredients.set(1, new ItemStack(Items.FEATHER));
        EnchantmentIngredients.set(2, new ItemStack(Items.FEATHER));
        EnchantmentIngredients.set(3, new ItemStack(Items.SHULKER_SHELL));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.ELYTRA));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(7, new ItemStack(Items.ELYTRA));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.SLIME_BALL));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(11, new ItemStack(Items.SLIME_BALL));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.SHULKER_SHELL));
        EnchantmentIngredients.set(13, new ItemStack(Items.SLIME_BLOCK));
        EnchantmentIngredients.set(14, new ItemStack(Items.SLIME_BLOCK));
        EnchantmentIngredients.set(15, new ItemStack(Items.SHULKER_SHELL));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:shulker_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void skeletonEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        ItemStack tippedArrow = PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW, 1), Potions.POISON);

        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.SKELETON_SKULL));
        EnchantmentIngredients.set(1, new ItemStack(Items.BONE));
        EnchantmentIngredients.set(2, new ItemStack(Items.BONE));
        EnchantmentIngredients.set(3, new ItemStack(Items.SKELETON_SKULL));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.BONE));
        EnchantmentIngredients.set(5, new ItemStack(Items.BOW));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(7, new ItemStack(Items.BONE));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.BONE));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(10, tippedArrow);
        EnchantmentIngredients.set(11, new ItemStack(Items.BONE));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.SKELETON_SKULL));
        EnchantmentIngredients.set(13, new ItemStack(Items.BONE));
        EnchantmentIngredients.set(14, new ItemStack(Items.BONE));
        EnchantmentIngredients.set(15, new ItemStack(Items.SKELETON_SKULL));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:skeleton_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void spectralEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.SPECTRAL_ARROW));
        EnchantmentIngredients.set(1, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(2, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(3, new ItemStack(Items.SPECTRAL_ARROW));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.DIAMOND_HELMET));
        EnchantmentIngredients.set(7, new ItemStack(Items.PHANTOM_MEMBRANE));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(9, new ItemStack(Items.TURTLE_HELMET));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.PHANTOM_MEMBRANE));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.SPECTRAL_ARROW));
        EnchantmentIngredients.set(13, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(14, new ItemStack(Items.PHANTOM_MEMBRANE));
        EnchantmentIngredients.set(15, new ItemStack(Items.SPECTRAL_ARROW));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:spectral_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void undyingEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
        EnchantmentIngredients.set(1, new ItemStack(Items.GOLD_BLOCK));
        EnchantmentIngredients.set(2, new ItemStack(Items.GOLD_BLOCK));
        EnchantmentIngredients.set(3, new ItemStack(Items.TOTEM_OF_UNDYING));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.CROSSBOW));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(7, new ItemStack(Items.EMERALD_BLOCK));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.ENDER_EYE));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_BLOCK));
        EnchantmentIngredients.set(11, new ItemStack(Items.ENDER_PEARL));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.TOTEM_OF_UNDYING));
        EnchantmentIngredients.set(13, new ItemStack(Items.GOLD_BLOCK));
        EnchantmentIngredients.set(14, new ItemStack(Items.GOLD_BLOCK));
        EnchantmentIngredients.set(15, new ItemStack(Items.TOTEM_OF_UNDYING));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:undying_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void vampiricEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.REDSTONE_BLOCK));
        EnchantmentIngredients.set(1, new ItemStack(Items.GHAST_TEAR));
        EnchantmentIngredients.set(2, new ItemStack(Items.ROTTEN_FLESH));
        EnchantmentIngredients.set(3, new ItemStack(Items.REDSTONE_BLOCK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.CRYING_OBSIDIAN));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.OBSIDIAN));
        EnchantmentIngredients.set(7, new ItemStack(Items.LAPIS_BLOCK));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.HEART_OF_THE_SEA));
        EnchantmentIngredients.set(9, new ItemStack(Items.OBSERVER));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(11, new ItemStack(Items.IRON_DOOR));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.REDSTONE_BLOCK));
        EnchantmentIngredients.set(13, new ItemStack(Items.ROTTEN_FLESH));
        EnchantmentIngredients.set(14, new ItemStack(Items.GHAST_TEAR));
        EnchantmentIngredients.set(15, new ItemStack(Items.REDSTONE_BLOCK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:vampiric_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void vindicatorEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.DARK_OAK_LOG));
        EnchantmentIngredients.set(1, new ItemStack(Items.EMERALD_BLOCK));
        EnchantmentIngredients.set(2, new ItemStack(Items.EMERALD_BLOCK));
        EnchantmentIngredients.set(3, new ItemStack(Items.DARK_OAK_LOG));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.NETHERITE_AXE));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(6, new ItemStack(Items.EMERALD));
        EnchantmentIngredients.set(7, new ItemStack(Items.NETHERITE_AXE));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.DIAMOND_AXE));
        EnchantmentIngredients.set(9, new ItemStack(Items.EMERALD));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(11, new ItemStack(Items.IRON_AXE));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.DARK_OAK_LOG));
        EnchantmentIngredients.set(13, new ItemStack(Items.EMERALD_BLOCK));
        EnchantmentIngredients.set(14, new ItemStack(Items.EMERALD_BLOCK));
        EnchantmentIngredients.set(15, new ItemStack(Items.DARK_OAK_LOG));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:vindicator_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void voidEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.SCULK));
        EnchantmentIngredients.set(1, new ItemStack(Items.SHIELD));
        EnchantmentIngredients.set(2, new ItemStack(Items.ECHO_SHARD));
        EnchantmentIngredients.set(3, new ItemStack(Items.SCULK));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.SCULK_CATALYST));
        EnchantmentIngredients.set(5, new ItemStack(Items.ELYTRA));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHERITE_CHESTPLATE));
        EnchantmentIngredients.set(7, new ItemStack(Items.SCULK_SENSOR));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.SCULK_SENSOR));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHERITE_LEGGINGS));
        EnchantmentIngredients.set(10, new ItemStack(Items.ELYTRA));
        EnchantmentIngredients.set(11, new ItemStack(Items.SCULK_CATALYST));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.SCULK));
        EnchantmentIngredients.set(13, new ItemStack(Items.ECHO_SHARD));
        EnchantmentIngredients.set(14, new ItemStack(Items.SHIELD));
        EnchantmentIngredients.set(15, new ItemStack(Items.SCULK));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:void_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void waterEnchantment() {

        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.WATER_BUCKET));
        EnchantmentIngredients.set(1, new ItemStack(Items.PRISMARINE_SHARD));
        EnchantmentIngredients.set(2, new ItemStack(Items.LILY_PAD));
        EnchantmentIngredients.set(3, new ItemStack(Items.WATER_BUCKET));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.HEART_OF_THE_SEA));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(6, new ItemStack(Items.GRAVEL));
        EnchantmentIngredients.set(7, new ItemStack(Items.CLAY));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.CLAY));
        EnchantmentIngredients.set(9, new ItemStack(Items.GRAVEL));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(11, new ItemStack(Items.SAND));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.WATER_BUCKET));
        EnchantmentIngredients.set(13, new ItemStack(Items.LILY_PAD));
        EnchantmentIngredients.set(14, new ItemStack(Items.PRISMARINE_SHARD));
        EnchantmentIngredients.set(15, new ItemStack(Items.WATER_BUCKET));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:water_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void witchEnchantment() {
    	  NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
    	  ItemStack poisonPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_POISON);
    	  ItemStack slowPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOWNESS);
    	  
    	  // Row 1
    	  EnchantmentIngredients.set(0, new ItemStack(Items.FERMENTED_SPIDER_EYE));
    	  EnchantmentIngredients.set(1, new ItemStack(Items.POISONOUS_POTATO));
    	  EnchantmentIngredients.set(2, poisonPotion);
    	  EnchantmentIngredients.set(3, new ItemStack(Items.FERMENTED_SPIDER_EYE));
    	  
    	  // Row 2
    	  EnchantmentIngredients.set(4, new ItemStack(Items.REDSTONE_BLOCK));
    	  EnchantmentIngredients.set(5, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(6, new ItemStack(Items.EXPERIENCE_BOTTLE));
    	  EnchantmentIngredients.set(7, new ItemStack(Items.GLOWSTONE));

    	  // Row 3
    	  EnchantmentIngredients.set(8, new ItemStack(Items.GLOWSTONE));
    	  EnchantmentIngredients.set(9, new ItemStack(Items.EXPERIENCE_BOTTLE));
    	  EnchantmentIngredients.set(10, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(11, new ItemStack(Items.REDSTONE_BLOCK));

    	  // Row 4
    	  EnchantmentIngredients.set(12, new ItemStack(Items.FERMENTED_SPIDER_EYE));
    	  EnchantmentIngredients.set(13, slowPotion);
    	  EnchantmentIngredients.set(14, new ItemStack(Items.POISONOUS_POTATO));
    	  EnchantmentIngredients.set(15, new ItemStack(Items.FERMENTED_SPIDER_EYE));

    	  ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:witch_enchantment")));
    	  recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    	}
    
    public static void witherEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.WITHER_SKELETON_SKULL));
        EnchantmentIngredients.set(1, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(2, new ItemStack(Items.SOUL_SAND));
        EnchantmentIngredients.set(3, new ItemStack(Items.WITHER_SKELETON_SKULL));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.SOUL_SAND));
        EnchantmentIngredients.set(5, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(6, new ItemStack(Items.WITHER_ROSE));
        EnchantmentIngredients.set(7, new ItemStack(Items.NETHERITE_INGOT));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(9, new ItemStack(Items.WITHER_ROSE));
        EnchantmentIngredients.set(10, new ItemStack(Items.NETHER_STAR));
        EnchantmentIngredients.set(11, new ItemStack(Items.SOUL_SAND));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.WITHER_SKELETON_SKULL));
        EnchantmentIngredients.set(13, new ItemStack(Items.SOUL_SAND));
        EnchantmentIngredients.set(14, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(15, new ItemStack(Items.WITHER_SKELETON_SKULL));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:wither_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void zombieEnchantment() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(Items.ZOMBIE_HEAD));
        EnchantmentIngredients.set(1, new ItemStack(Items.ROTTEN_FLESH));
        EnchantmentIngredients.set(2, new ItemStack(Items.ROTTEN_FLESH));
        EnchantmentIngredients.set(3, new ItemStack(Items.ZOMBIE_HEAD));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(Items.CARROT));
        EnchantmentIngredients.set(5, new ItemStack(Items.DIAMOND_AXE));
        EnchantmentIngredients.set(6, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(7, new ItemStack(Items.POTATO));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(Items.BAKED_POTATO));
        EnchantmentIngredients.set(9, new ItemStack(Items.NETHERITE_INGOT));
        EnchantmentIngredients.set(10, new ItemStack(Items.DIAMOND_SWORD));
        EnchantmentIngredients.set(11, new ItemStack(Items.CARROT_ON_A_STICK));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(Items.ZOMBIE_HEAD));
        EnchantmentIngredients.set(13, new ItemStack(Items.ROTTEN_FLESH));
        EnchantmentIngredients.set(14, new ItemStack(Items.ROTTEN_FLESH));
        EnchantmentIngredients.set(15, new ItemStack(Items.ZOMBIE_HEAD));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:zombie_enchantment")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void forceOfOverworld() {
    	  NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
    	  
    	  // Row 1
    	  EnchantmentIngredients.set(0, new ItemStack(ItemInit.AMETHYST_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(1, new ItemStack(ItemInit.REDSTONE_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(2, new ItemStack(ItemInit.GOLD_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(3, new ItemStack(ItemInit.LAPIS_ENCHANTMENT.get()));
    	  
    	  // Row 2
    	  EnchantmentIngredients.set(4, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(6, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(7, new ItemStack(Items.NETHERITE_INGOT));

    	  // Row 3
    	  EnchantmentIngredients.set(8, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(9, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(11, new ItemStack(Items.NETHERITE_INGOT));

    	  // Row 4
    	  EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(13, new ItemStack(ItemInit.DIAMOND_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(14, new ItemStack(ItemInit.EMERALD_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

    	  ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:force_of_overworld")));
    	  recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    	}
    
    public static void forceOfNature() {
    	  NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
    	  
    	  // Row 1
    	  EnchantmentIngredients.set(0, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(1, new ItemStack(ItemInit.EARTH_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(2, new ItemStack(ItemInit.WATER_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(3, new ItemStack(Items.NETHERITE_INGOT));
    	  
    	  // Row 2
    	  EnchantmentIngredients.set(4, new ItemStack(Items.LILAC));
    	  EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(6, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(7, new ItemStack(Items.ROSE_BUSH));

    	  // Row 3
    	  EnchantmentIngredients.set(8, new ItemStack(Items.SEA_PICKLE));
    	  EnchantmentIngredients.set(9, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(11, new ItemStack(Items.SUNFLOWER));

    	  // Row 4
    	  EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(13, new ItemStack(ItemInit.FIRE_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(14, new ItemStack(ItemInit.AIR_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

    	  ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:force_of_nature")));
    	  recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    	}
    
    public static void forceOfRejectors() {
    	  NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
    	  
    	  // Row 1
    	  EnchantmentIngredients.set(0, new ItemStack(ItemInit.DRAGON_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(1, new ItemStack(ItemInit.CREEPER_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(2, new ItemStack(ItemInit.ZOMBIE_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(3, new ItemStack(ItemInit.BLAZE_ENCHANTMENT.get()));
    	  
    	  // Row 2
    	  EnchantmentIngredients.set(4, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(6, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(7, new ItemStack(Items.NETHERITE_INGOT));

    	  // Row 3
    	  EnchantmentIngredients.set(8, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(9, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(11, new ItemStack(Items.NETHERITE_INGOT));

    	  // Row 4
    	  EnchantmentIngredients.set(12, new ItemStack(ItemInit.GHAST_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(13, new ItemStack(ItemInit.SKELETON_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(14, new ItemStack(ItemInit.WITHER_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(15, new ItemStack(ItemInit.VINDICATOR_ENCHANTMENT.get()));

    	  ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:force_of_rejectors")));
    	  recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    	}
    
    public static void forceOfMystic() {
    	  NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
    	  
    	  // Row 1
    	  EnchantmentIngredients.set(0, new ItemStack(ItemInit.WITCH_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(1, new ItemStack(ItemInit.VAMPIRIC_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(2, new ItemStack(ItemInit.LIBRARIAN_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(3, new ItemStack(ItemInit.ENCHANTING_ENCHANTMENT.get()));
    	  
    	  // Row 2
    	  EnchantmentIngredients.set(4, new ItemStack(Items.DRAGON_BREATH));
    	  EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(6, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(7, new ItemStack(Items.DRAGON_BREATH));

    	  // Row 3
    	  EnchantmentIngredients.set(8, new ItemStack(Items.DRAGON_BREATH));
    	  EnchantmentIngredients.set(9, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(11, new ItemStack(Items.DRAGON_BREATH));

    	  // Row 4
    	  EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(13, new ItemStack(ItemInit.UNDYING_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(14, new ItemStack(ItemInit.SHULKER_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

    	  ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:force_of_mystic")));
    	  recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    	}
    
    public static void forceOfWarrior() {
    	  NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
    	  
    	  // Row 1
    	  EnchantmentIngredients.set(0, new ItemStack(ItemInit.BATTLE_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(1, new ItemStack(ItemInit.CACTUS_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(2, new ItemStack(ItemInit.IRON_GOLEM_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(3, new ItemStack(ItemInit.VOID_ENCHANTMENT.get()));
    	  
    	  // Row 2
    	  EnchantmentIngredients.set(4, new ItemStack(Items.IRON_CHESTPLATE));
    	  EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(6, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(7, new ItemStack(Items.DIAMOND_CHESTPLATE));

    	  // Row 3
    	  EnchantmentIngredients.set(8, new ItemStack(Items.SHIELD));
    	  EnchantmentIngredients.set(9, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(11, new ItemStack(Items.NETHERITE_INGOT));

    	  // Row 4
    	  EnchantmentIngredients.set(12, new ItemStack(Items.GOLDEN_SWORD));
    	  EnchantmentIngredients.set(13, new ItemStack(ItemInit.THORNY_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(14, new ItemStack(ItemInit.THORNY_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

    	  ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:force_of_warrior")));
    	  recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    	}
    
    public static void forceOfExplorer() {
    	  NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
    	  
    	  // Row 1
    	  EnchantmentIngredients.set(0, new ItemStack(ItemInit.ENDERMAN_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(1, new ItemStack(ItemInit.SPECTRAL_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(2, new ItemStack(ItemInit.ARCTIC_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(3, new ItemStack(ItemInit.ARCHITECT_ENCHANTMENT.get()));
    	  
    	  // Row 2
    	  EnchantmentIngredients.set(4, new ItemStack(Items.OAK_BOAT));
    	  EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(6, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(7, new ItemStack(Items.BIRCH_CHEST_BOAT));

    	  // Row 3
    	  EnchantmentIngredients.set(8, new ItemStack(Items.CHEST_MINECART));
    	  EnchantmentIngredients.set(9, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
    	  EnchantmentIngredients.set(11, new ItemStack(Items.MINECART));

    	  // Row 4
    	  EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_INGOT));
    	  EnchantmentIngredients.set(13, new ItemStack(ItemInit.GLOWSTONE_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(14, new ItemStack(ItemInit.GLOWSTONE_ENCHANTMENT.get()));
    	  EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_INGOT));

    	  ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:force_of_explorer")));
    	  recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    	}
    
    public static void soulOfMinecraft() {
  	  NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
  	  
  	  // Row 1
  	  EnchantmentIngredients.set(0, new ItemStack(ItemInit.FORCE_OF_OVERWORLD.get()));
  	  EnchantmentIngredients.set(1, new ItemStack(ItemInit.FORCE_OF_NATURE.get()));
  	  EnchantmentIngredients.set(2, new ItemStack(ItemInit.FORCE_OF_REJECTORS.get()));
  	  EnchantmentIngredients.set(3, new ItemStack(ItemInit.FORCE_OF_MYSTIC.get()));
  	  
  	  // Row 2
  	  EnchantmentIngredients.set(4, new ItemStack(Items.NETHERITE_BLOCK));
  	  EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
  	  EnchantmentIngredients.set(6, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
  	  EnchantmentIngredients.set(7, new ItemStack(Items.NETHERITE_BLOCK));

  	  // Row 3
  	  EnchantmentIngredients.set(8, new ItemStack(Items.NETHERITE_BLOCK));
  	  EnchantmentIngredients.set(9, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
  	  EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
  	  EnchantmentIngredients.set(11, new ItemStack(Items.NETHERITE_BLOCK));

  	  // Row 4
  	  EnchantmentIngredients.set(12, new ItemStack(Items.NETHERITE_BLOCK));
  	  EnchantmentIngredients.set(13, new ItemStack(ItemInit.FORCE_OF_WARRIOR.get()));
  	  EnchantmentIngredients.set(14, new ItemStack(ItemInit.FORCE_OF_EXPLORER.get()));
  	  EnchantmentIngredients.set(15, new ItemStack(Items.NETHERITE_BLOCK));

  	  ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:soul_of_minecraft")));
  	  recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
  	}
    
    public static void soulOfFlightMastery() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(ItemInit.ANCIENT_WINGS_ELYTRA.get()));
        EnchantmentIngredients.set(1, new ItemStack(ItemInit.ASTRAL_WINGS_ELYTRA.get()));
        EnchantmentIngredients.set(2, new ItemStack(ItemInit.BLAZING_WINGS_ELYTRA.get()));
        EnchantmentIngredients.set(3, new ItemStack(ItemInit.DRAGON_WINGS_ELYTRA.get()));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(ItemInit.DUSTY_WINGS_ELYTRA.get()));
        EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(6, new ItemStack(ItemInit.ENDER_WINGS_ELYTRA.get()));
        EnchantmentIngredients.set(7, new ItemStack(ItemInit.FOREST_WINGS_ELYTRA.get()));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(ItemInit.FROZEN_WINGS_ELYTRA.get()));
        EnchantmentIngredients.set(9, new ItemStack(ItemInit.GHASTLY_WINGS_ELYTRA.get()));
        EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(11, new ItemStack(ItemInit.PHANTOM_WINGS_ELYTRA.get()));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(ItemInit.VOLCANIC_ASH_ELYTRA.get()));
        EnchantmentIngredients.set(13, new ItemStack(ItemInit.WITHER_WINGS_ELYTRA.get()));
        EnchantmentIngredients.set(14, new ItemStack(ItemInit.ENCHANTED_WINGS_ELYTRA.get()));
        EnchantmentIngredients.set(15, new ItemStack(ItemInit.OCEANS_FINS_ELYTRA.get()));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:soul_of_flight_mastery")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void soulOfColossus() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(ItemInit.HAND_WARMER.get()));
        EnchantmentIngredients.set(1, new ItemStack(ItemInit.OBSIDIAN_HORSESHOE.get()));
        EnchantmentIngredients.set(2, new ItemStack(ItemInit.WORM_SCARF.get()));
        EnchantmentIngredients.set(3, new ItemStack(ItemInit.BRAIN_OF_CONFUSION.get()));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(ItemInit.POCKET_MIRROR.get()));
        EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(6, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(7, new ItemStack(ItemInit.CHARM_OF_MYTHS.get()));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(ItemInit.BEE_CLOAK.get()));
        EnchantmentIngredients.set(9, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(11, new ItemStack(ItemInit.STAR_VEIL.get()));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(ItemInit.SHINY_STONE.get()));
        EnchantmentIngredients.set(13, new ItemStack(ItemInit.HERO_SHIELD.get()));
        EnchantmentIngredients.set(14, new ItemStack(ItemInit.FROZEN_SHIELD.get()));
        EnchantmentIngredients.set(15, new ItemStack(ItemInit.ANKH_SHIELD.get()));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:soul_of_colossus")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }
    
    public static void soulOfSupersonic() {
        NonNullList<ItemStack> EnchantmentIngredients = NonNullList.withSize(16, ItemStack.EMPTY);
        
        // Row 1
        EnchantmentIngredients.set(0, new ItemStack(ItemInit.AEOLUS_BOOTS.get()));
        EnchantmentIngredients.set(1, new ItemStack(ItemInit.BRAIN_SCRAMBLER.get()));
        EnchantmentIngredients.set(2, new ItemStack(ItemInit.REINDEER_BELLS.get()));
        EnchantmentIngredients.set(3, new ItemStack(ItemInit.ANCIENT_HORN.get()));
        
        // Row 2
        EnchantmentIngredients.set(4, new ItemStack(ItemInit.MECHANICAL_CART.get()));
        EnchantmentIngredients.set(5, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(6, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(7, new ItemStack(ItemInit.BLESSED_APPLE.get()));

        // Row 3
        EnchantmentIngredients.set(8, new ItemStack(ItemInit.MASTER_NINJA_GEAR.get()));
        EnchantmentIngredients.set(9, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(10, new ItemStack(ItemInit.ABOMINABLE_ENERGY.get()));
        EnchantmentIngredients.set(11, new ItemStack(ItemInit.SHIELD_OF_CTHULHU.get()));

        // Row 4
        EnchantmentIngredients.set(12, new ItemStack(ItemInit.BUNDLE_OF_HORSESHOE_BALOONS.get()));
        EnchantmentIngredients.set(13, new ItemStack(ItemInit.AMBER_HORSESHOE_BALOON.get()));
        EnchantmentIngredients.set(14, new ItemStack(ItemInit.SWEETHEART_NECKLACE.get()));
        EnchantmentIngredients.set(15, new ItemStack(ItemInit.FLYING_CARPET.get()));

        ItemStack output = new ItemStack(Registry.ITEM.get(new ResourceLocation("fargos:soul_of_supersonic")));
        recipes.add(new CrucibleOfTheCosmosRecipe(EnchantmentIngredients, output));
    }


}