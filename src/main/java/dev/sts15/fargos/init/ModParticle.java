package dev.sts15.fargos.init;

import dev.sts15.fargos.Fargos;

import com.mojang.serialization.Codec;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticle {
	public static final DeferredRegister<ParticleType<?>> PARTICLE = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Fargos.MODID);


    public static final RegistryObject<SimpleParticleType> DEATH_SICKLE = PARTICLE.register("death_sickle", ()-> new SimpleParticleType(false));
    
}
