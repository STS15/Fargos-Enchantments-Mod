package dev.sts15.fargos.init;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.blocks.crucibleofthecosmos.CrucibleOfTheCosmos;
import dev.sts15.fargos.blocks.netheriteanvil.NetheriteAnvil;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Fargos.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Fargos.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Fargos.MODID);

    public static final RegistryObject<Block> CRUCIBLE_OF_THE_COSMOS = BLOCKS.register("crucible_of_the_cosmos", CrucibleOfTheCosmos::new);
    public static final RegistryObject<Item> CRUCIBLE_OF_THE_COSMOS_ITEM = ITEMS.register("crucible_of_the_cosmos", () -> new BlockItem(CRUCIBLE_OF_THE_COSMOS.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    
//    public static final RegistryObject<Block> NETHERITE_ANVIL = BLOCKS.register("netherite_anvil", 
//            () -> new NetheriteAnvil(Block.Properties.of(Material.HEAVY_METAL).strength(5.0f, 1200.0f).requiresCorrectToolForDrops()));
//    public static final RegistryObject<Item> NETHERITE_ANVIL_ITEM = ITEMS.register("netherite_anvil", () -> new BlockItem(NETHERITE_ANVIL.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(CRUCIBLE_OF_THE_COSMOS.get(), RenderType.translucent());
        });
    }
}
