package dev.sts15.fargos;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.sts15.fargos.blocks.crucibleofthecosmos.CrucibleOfTheCosmosRecipeRegistry;
import dev.sts15.fargos.command.ToggleEnchantmentsCommand;
import dev.sts15.fargos.effect.CooldownEffect;
import dev.sts15.fargos.effect.FlightEffect;
import dev.sts15.fargos.entity.client.*;
import dev.sts15.fargos.init.*;
import dev.sts15.fargos.recipe.ModRecipes;
import dev.sts15.fargos.screen.crucibleofthecosmos.CrucibleOfTheCosmosScreen;
import dev.sts15.fargos.sound.ModSounds;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib3.GeckoLib;
import top.theillusivec4.curios.api.SlotTypeMessage;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Fargos.MODID)
public class Fargos {

    public static final String MODID = "fargos";
    public static final Logger LOGGER = LoggerFactory.getLogger("Fargos");

    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    public static final RegistryObject<SoundEvent> MUTANT_BOSS_MUSIC = SOUNDS.register("mutant_boss_music", () -> new SoundEvent(new ResourceLocation(MODID, "mutant_boss_music")));
    public static final RegistryObject<SoundEvent> ABOMINATIONN_BOSS_MUSIC = SOUNDS.register("abominationn_boss_music", () -> new SoundEvent(new ResourceLocation(MODID, "abominationn_boss_music")));
    public static final RegistryObject<MobEffect> COOLDOWN_EFFECT = MOB_EFFECTS.register("active_ability_cooldown", CooldownEffect::new);
    public static final RegistryObject<MobEffect> FLIGHT_EFFECT = MOB_EFFECTS.register("flight", FlightEffect::new);

    @SuppressWarnings("static-access")
	public Fargos() {
    	
    	ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT.COMMON, FargosConfig.SERVER_CONFIG);
    	MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(ModRecipes::onCommonSetup);
    	
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MOB_EFFECTS.register(modEventBus);
        SOUNDS.register(modEventBus);
        
        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        BlockInit.BLOCK_ENTITIES.register(modEventBus);
        BlockInit.ITEMS.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticle.PARTICLE.register(modEventBus);
        ModEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        
        

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::enqueueIMC);
        

        GeckoLib.initialize();

    }

    private void setup(final FMLCommonSetupEvent event) {
    	CrucibleOfTheCosmosRecipeRegistry.init();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    	registerCurioSlot("charm", 4, false, null);
    }
    
    public void registerCommands(RegisterCommandsEvent event)
	{
		ToggleEnchantmentsCommand.register(event.getDispatcher()); 
	}

    private void doClientStuff(final FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.MUTANT.get(), MutantRenderer::new);
        EntityRenderers.register(ModEntities.ABOMINATIONN.get(), AbominationnRenderer::new);
        EntityRenderers.register(ModEntities.TARGET_LOCKED.get(), TargetLockedRenderer::new);
        EntityRenderers.register(ModEntities.DEATH_SICKLE.get(), DeathSickleRenderer::new);
        EntityRenderers.register(ModEntities.LASER_SWORD.get(), LaserSwordRenderer::new);
        
        //ItemBlockRenderTypes.setRenderLayer(BlockInit.CRUCIBLE_OF_THE_COSMOS.get(), RenderType.cutout());
        
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(BlockInit.CRUCIBLE_OF_THE_COSMOS.get(), RenderType.translucent());
        });
        
        MenuScreens.register(ModMenus.CRUCIBLE_OF_THE_COSMOS_MENU.get(), CrucibleOfTheCosmosScreen::new);
    
    }
    
    public static void registerCurioSlot(final String identifier, final int slots, final boolean isHidden, @Nullable final ResourceLocation icon) {
        final SlotTypeMessage.Builder message = new SlotTypeMessage.Builder(identifier);

        message.size(slots);

        if (isHidden) {
            message.hide();
        }

        if (icon != null) {
            message.icon(icon);
        }

        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> message.build());

    }

}
