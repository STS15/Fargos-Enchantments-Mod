package dev.sts15.fargos.init;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.screen.crucibleofthecosmos.CrucibleOfTheCosmosMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Fargos.MODID);

    public static final RegistryObject<MenuType<CrucibleOfTheCosmosMenu>> CRUCIBLE_OF_THE_COSMOS_MENU = 
        MENUS.register("crucible_of_the_cosmos_menu", 
        () -> IForgeMenuType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            Level level = inv.player.level; // Get the level directly from the player's inventory
            return new CrucibleOfTheCosmosMenu(windowId, inv, null, pos, level);
        }));

    public static void register(IEventBus modEventBus) {
        MENUS.register(modEventBus);
    }
}
