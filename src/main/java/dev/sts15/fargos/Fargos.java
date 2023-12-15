package dev.sts15.fargos;

import dev.sts15.fargos.effect.*;
import dev.sts15.fargos.init.ItemInit;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(Fargos.MODID)
public class Fargos {

    public static final String MODID = "fargos";

    // Create a Deferred Register for MobEffects
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);

    // Register the CooldownEffect
    public static final RegistryObject<MobEffect> COOLDOWN_EFFECT = MOB_EFFECTS.register("active_ability_cooldown", CooldownEffect::new);

    public Fargos() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Attach the Deferred Register to the Mod Event Bus
        MOB_EFFECTS.register(modEventBus);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::enqueueIMC);
        ItemInit.ITEMS.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Setup logic
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, 
            () -> SlotTypePreset.CHARM.getMessageBuilder().build());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // Client-only setup logic
    }

    // ... Rest of your class ...
}
