package dev.sts15.fargos.init;

import dev.sts15.fargos.Fargos;
import dev.sts15.fargos.effect.CooldownEffect;
import dev.sts15.fargos.effect.FlightEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.caelus.api.CaelusApi;

public class EffectsInit {

	private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Fargos.MODID);
	
    public static final RegistryObject<MobEffect> COOLDOWN_EFFECT = MOB_EFFECTS.register("active_ability_cooldown", CooldownEffect::new);
    public static final RegistryObject<MobEffect> FLIGHT_EFFECT = MOB_EFFECTS.register("flight", FlightEffect::new);
    
    public static void register(IEventBus eventBus) {
    	MOB_EFFECTS.register(eventBus);
    }
    
}
