package dev.sts15.fargos.init;

import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.item.enchantments.*;
import dev.sts15.fargos.item.forces.*;
import dev.sts15.fargos.item.items.*;
import dev.sts15.fargos.item.souls.*;
import dev.sts15.fargos.item.weapons.*;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Fargos.MODID);
    
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }


    // Enchantments
    
    public static final RegistryObject<RedstoneEnchantment> REDSTONE_ENCHANTMENT = ITEMS.register("redstone_enchantment", 
            () -> new RedstoneEnchantment());

    public static final RegistryObject<AmathystEnchantment> AMETHYST_ENCHANTMENT = ITEMS.register("amathyst_enchantment", 
            () -> new AmathystEnchantment());

    public static final RegistryObject<DiamondEnchantment> DIAMOND_ENCHANTMENT = ITEMS.register("diamond_enchantment", 
            () -> new DiamondEnchantment());

    public static final RegistryObject<LapisEnchantment> LAPIS_ENCHANTMENT = ITEMS.register("lapis_enchantment", 
            () -> new LapisEnchantment());

    public static final RegistryObject<IronEnchantment> IRON_ENCHANTMENT = ITEMS.register("iron_enchantment", 
            () -> new IronEnchantment());

    public static final RegistryObject<GoldEnchantment> GOLD_ENCHANTMENT = ITEMS.register("gold_enchantment", 
            () -> new GoldEnchantment());

    public static final RegistryObject<EmeraldEnchantment> EMERALD_ENCHANTMENT = ITEMS.register("emerald_enchantment", 
            () -> new EmeraldEnchantment());

    public static final RegistryObject<CopperEnchantment> COPPER_ENCHANTMENT = ITEMS.register("copper_enchantment", 
            () -> new CopperEnchantment());
    
    public static final RegistryObject<WitherEnchantment> WITHER_ENCHANTMENT = ITEMS.register("wither_enchantment", 
            () -> new WitherEnchantment());
    
    public static final RegistryObject<EarthEnchantment> EARTH_ENCHANTMENT = ITEMS.register("earth_enchantment", 
            () -> new EarthEnchantment());
    
    public static final RegistryObject<WaterEnchantment> WATER_ENCHANTMENT = ITEMS.register("water_enchantment", 
            () -> new WaterEnchantment());
    
    public static final RegistryObject<FireEnchantment> FIRE_ENCHANTMENT = ITEMS.register("fire_enchantment", 
            () -> new FireEnchantment());
    
    public static final RegistryObject<AirEnchantment> AIR_ENCHANTMENT = ITEMS.register("air_enchantment", 
            () -> new AirEnchantment());
    
    public static final RegistryObject<NetherStarEnchantment> NETHER_STAR_ENCHANTMENT = ITEMS.register("nether_star_enchantment", 
            () -> new NetherStarEnchantment());
    
    public static final RegistryObject<AppleEnchantment> APPLE_ENCHANTMENT = ITEMS.register("apple_enchantment", 
            () -> new AppleEnchantment());
    
    public static final RegistryObject<ObsidianEnchantment> OBSIDIAN_ENCHANTMENT = ITEMS.register("obsidian_enchantment", 
            () -> new ObsidianEnchantment());
    
    public static final RegistryObject<DragonEnchantment> DRAGON_ENCHANTMENT = ITEMS.register("dragon_enchantment", 
            () -> new DragonEnchantment());
    
    public static final RegistryObject<MooshroomEnchantment> MOOSHROOM_ENCHANTMENT = ITEMS.register("mooshroom_enchantment", 
            () -> new MooshroomEnchantment());
    
    public static final RegistryObject<SkeletonEnchantment> SKELETON_ENCHANTMENT = ITEMS.register("skeleton_enchantment", 
            () -> new SkeletonEnchantment());
    
    public static final RegistryObject<ZombieEnchantment> ZOMBIE_ENCHANTMENT = ITEMS.register("zombie_enchantment", 
            () -> new ZombieEnchantment());
    
    public static final RegistryObject<BlazeEnchantment> BLAZE_ENCHANTMENT = ITEMS.register("blaze_enchantment", 
            () -> new BlazeEnchantment());
    
    public static final RegistryObject<PickaxeEnchantment> PICKAXE_ENCHANTMENT = ITEMS.register("pickaxe_enchantment", 
            () -> new PickaxeEnchantment());
    
    public static final RegistryObject<CreeperEnchantment> CREEPER_ENCHANTMENT = ITEMS.register("creeper_enchantment", 
            () -> new CreeperEnchantment());
    
    public static final RegistryObject<GhastEnchantment> GHAST_ENCHANTMENT = ITEMS.register("ghast_enchantment", 
            () -> new GhastEnchantment());
    
    public static final RegistryObject<VindicatorEnchantment> VINDICATOR_ENCHANTMENT = ITEMS.register("vindicator_enchantment", 
            () -> new VindicatorEnchantment());
    
    public static final RegistryObject<VampiricEnchantment> VAMPIRIC_ENCHANTMENT = ITEMS.register("vampiric_enchantment", 
            () -> new VampiricEnchantment());
    
    public static final RegistryObject<EnchantingEnchantment> ENCHANTING_ENCHANTMENT = ITEMS.register("enchanting_enchantment", 
            () -> new EnchantingEnchantment());
    
    public static final RegistryObject<LibrarianEnchantment> LIBRARIAN_ENCHANTMENT = ITEMS.register("librarian_enchantment", 
            () -> new LibrarianEnchantment());
    
    public static final RegistryObject<WitchEnchantment> WITCH_ENCHANTMENT = ITEMS.register("witch_enchantment", 
            () -> new WitchEnchantment());
    
    public static final RegistryObject<ShulkerEnchantment> SHULKER_ENCHANTMENT = ITEMS.register("shulker_enchantment", 
            () -> new ShulkerEnchantment());
    
    public static final RegistryObject<UndyingEnchantment> UNDYING_ENCHANTMENT = ITEMS.register("undying_enchantment", 
            () -> new UndyingEnchantment());
    
    public static final RegistryObject<BattleEnchantment> BATTLE_ENCHANTMENT = ITEMS.register("battle_enchantment", 
            () -> new BattleEnchantment());
    
    public static final RegistryObject<CactusEnchantment> CACTUS_ENCHANTMENT = ITEMS.register("cactus_enchantment", 
            () -> new CactusEnchantment());
    
    public static final RegistryObject<VoidEnchantment> VOID_ENCHANTMENT = ITEMS.register("void_enchantment", 
            () -> new VoidEnchantment());
    
    public static final RegistryObject<ThornyEnchantment> THORNY_ENCHANTMENT = ITEMS.register("thorny_enchantment", 
            () -> new ThornyEnchantment());
    
    public static final RegistryObject<IronGolemEnchantment> IRON_GOLEM_ENCHANTMENT = ITEMS.register("iron_golem_enchantment", 
            () -> new IronGolemEnchantment());
    
    public static final RegistryObject<ArchitectEnchantment> ARCHITECT_ENCHANTMENT = ITEMS.register("architect_enchantment", 
            () -> new ArchitectEnchantment());
    
    public static final RegistryObject<EndermanEnchantment> ENDERMAN_ENCHANTMENT = ITEMS.register("enderman_enchantment", 
            () -> new EndermanEnchantment());
    
    public static final RegistryObject<ArcticEnchantment> ARCTIC_ENCHANTMENT = ITEMS.register("arctic_enchantment", 
            () -> new ArcticEnchantment());
    
    public static final RegistryObject<SpectralEnchantment> SPECTRAL_ENCHANTMENT = ITEMS.register("spectral_enchantment", 
            () -> new SpectralEnchantment());
    
    public static final RegistryObject<GlowstoneEnchantment> GLOWSTONE_ENCHANTMENT = ITEMS.register("glowstone_enchantment", 
            () -> new GlowstoneEnchantment());
    
    // Forces
    
    public static final RegistryObject<ForceOfOverworld> FORCE_OF_OVERWORLD = ITEMS.register("force_of_overworld", 
            () -> new ForceOfOverworld());
    
    public static final RegistryObject<ForceOfNature> FORCE_OF_NATURE = ITEMS.register("force_of_nature", 
            () -> new ForceOfNature());
    
    public static final RegistryObject<ForceOfRejectors> FORCE_OF_REJECTORS = ITEMS.register("force_of_rejectors", 
            () -> new ForceOfRejectors());
    
    public static final RegistryObject<ForceOfMystic> FORCE_OF_MYSTIC = ITEMS.register("force_of_mystic", 
            () -> new ForceOfMystic());
    
    public static final RegistryObject<ForceOfWarrior> FORCE_OF_WARRIOR = ITEMS.register("force_of_warrior", 
            () -> new ForceOfWarrior());
    
    public static final RegistryObject<ForceOfExplorer> FORCE_OF_EXPLORER = ITEMS.register("force_of_explorer", 
            () -> new ForceOfExplorer());
    
    // Items
    
    public static final RegistryObject<AbominableEnergy> ABOMINABLE_ENERGY = ITEMS.register("abominable_energy", () -> new AbominableEnergy());
    
    //public static final RegistryObject<LuminiteIngot> LUMINITE_INGOT = ITEMS.register("luminite_ingot", () -> new LuminiteIngot());
    
    public static final RegistryObject<SoulOfMinecraft> SOUL_OF_MINECRAFT = ITEMS.register("soul_of_minecraft", () -> new SoulOfMinecraft());
    public static final RegistryObject<SoulOfFlightMastery> SOUL_OF_FLIGHT_MASTERY = ITEMS.register("soul_of_flight_mastery", () -> new SoulOfFlightMastery());
    public static final RegistryObject<SoulOfColossus> SOUL_OF_COLOSSUS = ITEMS.register("soul_of_colossus", () -> new SoulOfColossus());
    public static final RegistryObject<SoulOfSupersonic> SOUL_OF_SUPERSONIC = ITEMS.register("soul_of_supersonic", () -> new SoulOfSupersonic());
    //public static final RegistryObject<SoulOfTrawler> SOUL_OF_TRAWLER = ITEMS.register("soul_of_trawler", () -> new SoulOfTrawler());
    //public static final RegistryObject<CraftingComponent> SOUL_OF_DIMENSIONS = ITEMS.register("soul_of_dimensions", () -> new CraftingComponent());
    
    // Crafting Items
    public static final RegistryObject<CraftingComponent> MULTITASK_CENTER = ITEMS.register("multitask_center", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> ELEMENTAL_ASSEMBLER = ITEMS.register("elemental_assembler", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> NETHERITE_ANVIL = ITEMS.register("netherite_anvil", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> ANCIENT_WINGS_ELYTRA = ITEMS.register("ancient_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> ASTRAL_WINGS_ELYTRA = ITEMS.register("astral_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> BLAZING_WINGS_ELYTRA = ITEMS.register("blazing_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> DRAGON_WINGS_ELYTRA = ITEMS.register("dragon_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> DUSTY_WINGS_ELYTRA = ITEMS.register("dusty_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> ENCHANTED_WINGS_ELYTRA = ITEMS.register("enchanted_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> ENDER_WINGS_ELYTRA = ITEMS.register("ender_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> FOREST_WINGS_ELYTRA = ITEMS.register("forest_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> FROZEN_WINGS_ELYTRA = ITEMS.register("frozen_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> GHASTLY_WINGS_ELYTRA = ITEMS.register("ghastly_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> OCEANS_FINS_ELYTRA = ITEMS.register("oceans_fins_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> PHANTOM_WINGS_ELYTRA = ITEMS.register("phantom_wings_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> VOLCANIC_ASH_ELYTRA = ITEMS.register("volcanic_ash_elytra", () -> new CraftingComponent());
    public static final RegistryObject<CraftingComponent> WITHER_WINGS_ELYTRA = ITEMS.register("wither_wings_elytra", () -> new CraftingComponent());
    
    public static final RegistryObject<DungeonLoot> HAND_WARMER = ITEMS.register("hand_warmer", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> OBSIDIAN_HORSESHOE = ITEMS.register("obsidian_horseshoe", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> WORM_SCARF = ITEMS.register("worm_scarf", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> BRAIN_OF_CONFUSION = ITEMS.register("brain_of_confusion", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> POCKET_MIRROR = ITEMS.register("pocket_mirror", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> CHARM_OF_MYTHS = ITEMS.register("charm_of_myths", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> BEE_CLOAK = ITEMS.register("bee_cloak", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> STAR_VEIL = ITEMS.register("star_veil", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> SHINY_STONE = ITEMS.register("shiny_stone", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> HERO_SHIELD = ITEMS.register("hero_shield", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> FROZEN_SHIELD = ITEMS.register("frozen_shield", () -> new DungeonLoot());
    public static final RegistryObject<DungeonLoot> ANKH_SHIELD = ITEMS.register("ankh_shield", () -> new DungeonLoot());
    
    public static final RegistryObject<DungeonDrops> AEOLUS_BOOTS = ITEMS.register("aeolus_boots", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> BRAIN_SCRAMBLER = ITEMS.register("brain_scrambler", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> REINDEER_BELLS = ITEMS.register("reindeer_bells", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> ANCIENT_HORN = ITEMS.register("ancient_horn", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> MECHANICAL_CART = ITEMS.register("mechanical_cart", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> BLESSED_APPLE = ITEMS.register("blessed_apple", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> MASTER_NINJA_GEAR = ITEMS.register("master_ninja_gear", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> SHIELD_OF_CTHULHU = ITEMS.register("shield_of_cthulhu", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> BUNDLE_OF_HORSESHOE_BALOONS = ITEMS.register("bundle_of_horseshoe_balloons", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> AMBER_HORSESHOE_BALOON = ITEMS.register("amber_horseshoe_balloon", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> SWEETHEART_NECKLACE = ITEMS.register("sweetheart_necklace", () -> new DungeonDrops());
    public static final RegistryObject<DungeonDrops> FLYING_CARPET = ITEMS.register("flying_carpet", () -> new DungeonDrops());
    
    // Summons
    
//    public static final RegistryObject<Item> MUTANTS_CURSE = ITEMS.register("mutants_curse", 
//            () -> new MutantsCurse(ModEntities.MUTANT, 
//                new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
//    
//    public static final RegistryObject<Item> ABOMINATIONNS_CURSE = ITEMS.register("abominationns_curse", 
//            () -> new AbominationnsCurse(ModEntities.ABOMINATIONN, 
//                new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
//    
//    public static final RegistryObject<Item> MUTANT_VOODOO = ITEMS.register("mutant_voodoo", 
//            () -> new MutantVoodoo(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    
    // Weapons
    
//    public static final RegistryObject<Item> DRAGONS_DEMISE = ITEMS.register("dragons_demise", () -> new DragonsDemise(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    
    public static final RegistryObject<StyxGazer> STYX_GAZER = ITEMS.register("styx_gazer",
            () -> new StyxGazer(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    
    public static final RegistryObject<SwordOfTheFates> SWORD_OF_THE_FATES = ITEMS.register("sword_of_the_fates",
            () -> new SwordOfTheFates(Tiers.NETHERITE, 0, 0, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    
//    public static final RegistryObject<TestSummoner> TEST_SUMMONER = ITEMS.register("test_summoner",
//            () -> new TestSummoner(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

    
    // Music Discs
    
    public static final RegistryObject<Item> MUTANT_MUSIC_DISC = ITEMS.register("mutant_music_disc", () -> new RecordItem(15, ModSounds.MUTANT_BOSS_MUSIC, new Item.Properties()
    		.tab(CreativeModeTab.TAB_MISC).stacksTo(1), 320));
    
    public static final RegistryObject<Item> ABOMINATIONN_MUSIC_DISC = ITEMS.register("abominationn_music_disc", () -> new RecordItem(15, ModSounds.ABOMINATIONN_BOSS_MUSIC, new Item.Properties()
    		.tab(CreativeModeTab.TAB_MISC).stacksTo(1), 320));
}

