package dev.sts15.fargos.sound;

import dev.sts15.fargos.Fargos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Fargos.MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Fargos.MODID);

    public static final RegistryObject<SoundEvent> MUTANT_BOSS_MUSIC = SOUNDS.register("mutant_boss_music", () -> new SoundEvent(new ResourceLocation(Fargos.MODID, "mutant_boss_music")));
    
    public static final RegistryObject<SoundEvent> ABOMINATIONN_BOSS_MUSIC = SOUNDS.register("abominationn_boss_music", () -> new SoundEvent(new ResourceLocation(Fargos.MODID, "abominationn_boss_music")));
    
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
