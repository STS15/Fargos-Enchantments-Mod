package dev.sts15.fargos.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import dev.sts15.fargos.entity.custom.AbominationnEntity;
import dev.sts15.fargos.entity.custom.ArrowEntity;
import dev.sts15.fargos.entity.custom.DeathSickleEntity;
import dev.sts15.fargos.entity.custom.FireRemoverEntity;
import dev.sts15.fargos.entity.custom.LaserSwordEntity;
import dev.sts15.fargos.entity.custom.MutantEntity;
import dev.sts15.fargos.entity.custom.TargetLockedEntity;
import dev.sts15.fargos.Fargos;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Fargos.MODID);
	
	public static final RegistryObject<EntityType<MutantEntity>> MUTANT = register("mutant",
			EntityType.Builder.<MutantEntity>of(MutantEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64)
			.setUpdateInterval(3)
			.setCustomClientFactory(MutantEntity::new)
			.fireImmune()
			.sized(0.6f, 3f)
			);
	
	public static final RegistryObject<EntityType<AbominationnEntity>> ABOMINATIONN = REGISTRY.register("abominationn",
            () -> EntityType.Builder.of(AbominationnEntity::new, MobCategory.MONSTER)
            .sized(1.0f, 2.8f)
            .fireImmune()
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(32)
            .setUpdateInterval(3)
            .build(new ResourceLocation(Fargos.MODID, "abominationn").toString())
            );
	
	public static final RegistryObject<EntityType<TargetLockedEntity>> TARGET_LOCKED = REGISTRY.register("target_locked",
	        () -> EntityType.Builder.<TargetLockedEntity>of(TargetLockedEntity::new, MobCategory.MONSTER)
	        .sized(0.1f, 0.1f)
	        .setTrackingRange(64)
	        .setUpdateInterval(1)
	        .fireImmune()
	        .noSave()
	        .build(new ResourceLocation(Fargos.MODID, "target_locked").toString()));
	
	public static final RegistryObject<EntityType<DeathSickleEntity>> DEATH_SICKLE = REGISTRY.register("death_sickle",
	        () -> EntityType.Builder.<DeathSickleEntity>of(DeathSickleEntity::new, MobCategory.MONSTER)
	        .sized(1.7f, 1.0f)
	        .setTrackingRange(64)
	        .setUpdateInterval(1)
	        .fireImmune()
	        .noSave()
	        .build(new ResourceLocation(Fargos.MODID, "death_sickle").toString()));
	
	public static final RegistryObject<EntityType<LaserSwordEntity>> LASER_SWORD = REGISTRY.register("laser_sword",
	        () -> EntityType.Builder.<LaserSwordEntity>of(LaserSwordEntity::new, MobCategory.MONSTER)
	        .sized(0.1f, 0.1f)
	        .setTrackingRange(64)
	        .setUpdateInterval(1)
	        .fireImmune()
	        .noSave()
	        .build(new ResourceLocation(Fargos.MODID, "laser_sword").toString()));
	
	public static final RegistryObject<EntityType<FireRemoverEntity>> FIRE_REMOVER = REGISTRY.register("fire_remover",
	        () -> EntityType.Builder.<FireRemoverEntity>of(FireRemoverEntity::new, MobCategory.MONSTER)
	        .sized(0.1f, 0.1f)
	        .setTrackingRange(64)
	        .setUpdateInterval(1)
	        .fireImmune()
	        .noSave()
	        .build(new ResourceLocation(Fargos.MODID, "fire_remover").toString()));

	
	public static final RegistryObject<EntityType<ArrowEntity>> MUTANT_PROJECTILE = register("projectile_mutant", EntityType.Builder.<ArrowEntity>of(ArrowEntity::new, MobCategory.MISC)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(ArrowEntity::new).sized(0.5f, 0.5f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			MutantEntity.init();
			//AbominationnEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
	    event.put(MUTANT.get(), MutantEntity.createAttributes().build());
	    event.put(ABOMINATIONN.get(), AbominationnEntity.createAttributes().build());
	    event.put(TARGET_LOCKED.get(), TargetLockedEntity.createAttributes().build());
	    event.put(DEATH_SICKLE.get(), DeathSickleEntity.createAttributes().build());
	    event.put(LASER_SWORD.get(), LaserSwordEntity.createAttributes().build());
	    event.put(FIRE_REMOVER.get(), FireRemoverEntity.createAttributes().build());
	}

	public static void register(IEventBus modEventBus) {
	    REGISTRY.register(modEventBus);
	}
}
