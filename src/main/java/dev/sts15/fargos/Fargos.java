package dev.sts15.fargos;

import dev.sts15.fargos.network.NetworkSetup;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.sts15.fargos.blocks.crucibleofthecosmos.CrucibleOfTheCosmosRecipeRegistry;
import dev.sts15.fargos.command.ToggleEnchantmentsCommand;
import dev.sts15.fargos.entity.client.*;
import dev.sts15.fargos.init.*;
import dev.sts15.fargos.loot.LootRegistry;
import dev.sts15.fargos.render.AngelWingsLayer;
import dev.sts15.fargos.render.AngelWingsModel;
import dev.sts15.fargos.screen.crucibleofthecosmos.CrucibleOfTheCosmosScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;
import top.theillusivec4.curios.api.SlotTypeMessage;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Fargos.MODID)
public class Fargos {

    public static final String MODID = "fargos";
    public static final Logger LOGGER = LoggerFactory.getLogger("Fargos");

	public Fargos() {
    	
    	ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FargosConfig.SERVER_CONFIG);
    	MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ItemInit.register(modEventBus);
        BlockInit.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticle.register(modEventBus);
        ModEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        LootRegistry.register(modEventBus);
        EffectsInit.register(modEventBus);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::enqueueIMC);
        
        GeckoLib.initialize();

    }

    private void setup(final FMLCommonSetupEvent event) {
        CrucibleOfTheCosmosRecipeRegistry.init();
        NetworkSetup.register();
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
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(BlockInit.CRUCIBLE_OF_THE_COSMOS.get(), RenderType.translucent());
//            EntityRenderer<? extends Player> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().get("default");
//            if (renderer instanceof PlayerRenderer) {
//                PlayerRenderer playerRenderer = (PlayerRenderer) renderer;
//                playerRenderer.addLayer(new AngelWingsLayer<>(playerRenderer));
//            }
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
