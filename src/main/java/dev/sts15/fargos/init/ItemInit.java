package dev.sts15.fargos.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;
import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.item.enchantments.*;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Fargos.MODID);

    // Register Redstone Enchantment
    public static final RegistryObject<RedstoneEnchantment> REDSTONE_ENCHANTMENT = ITEMS.register("redstone_enchantment", 
            () -> new RedstoneEnchantment());

    // Register Amethyst Enchantment
    public static final RegistryObject<AmathystEnchantment> AMETHYST_ENCHANTMENT = ITEMS.register("amathyst_enchantment", 
            () -> new AmathystEnchantment());

    // Register Diamond Enchantment
    public static final RegistryObject<DiamondEnchantment> DIAMOND_ENCHANTMENT = ITEMS.register("diamond_enchantment", 
            () -> new DiamondEnchantment());

    // Register Lapis Enchantment
    public static final RegistryObject<LapisEnchantment> LAPIS_ENCHANTMENT = ITEMS.register("lapis_enchantment", 
            () -> new LapisEnchantment());

    // Register Iron Enchantment
    public static final RegistryObject<IronEnchantment> IRON_ENCHANTMENT = ITEMS.register("iron_enchantment", 
            () -> new IronEnchantment());

    // Register Gold Enchantment
    public static final RegistryObject<GoldEnchantment> GOLD_ENCHANTMENT = ITEMS.register("gold_enchantment", 
            () -> new GoldEnchantment());

    // Register Emerald Enchantment
    public static final RegistryObject<EmeraldEnchantment> EMERALD_ENCHANTMENT = ITEMS.register("emerald_enchantment", 
            () -> new EmeraldEnchantment());

    // Register Copper Enchantment
    public static final RegistryObject<CopperEnchantment> COPPER_ENCHANTMENT = ITEMS.register("copper_enchantment", 
            () -> new CopperEnchantment());
}
