package dev.sts15.fargos.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import dev.sts15.fargos.init.FargosConfig;
import dev.sts15.fargos.item.enchantments.WitchEnchantment;
import dev.sts15.fargos.item.forces.ForceOfMystic;
import dev.sts15.fargos.item.souls.SoulOfMinecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(Witch.class)
public abstract class WitchMixin extends Entity {
    
    public WitchMixin(EntityType<? extends Entity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    public void injectPerformRangedAttack(LivingEntity target, float distanceFactor, CallbackInfo ci) {
        if (target instanceof Player && hasWitchEnchantment((Player) target)) {
            throwBeneficialPotion((Witch) (Object) this, (Player) target);
            ci.cancel();
        }
    }
    
    private void throwBeneficialPotion(Witch witch, Player target) {
    	
    	Potion[] beneficialPotions = new Potion[] {
    	        Potions.REGENERATION,
    	        Potions.HEALING,
    	        Potions.NIGHT_VISION,
    	        Potions.STRENGTH,
    	        Potions.LUCK,
    	        Potions.SWIFTNESS
    	    };
    	
    	Potion selectedPotion = beneficialPotions[witch.level.random.nextInt(beneficialPotions.length)];
        ItemStack potionStack = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), selectedPotion);

        ThrownPotion thrownPotion = new ThrownPotion(witch.level, witch);
        thrownPotion.setItem(potionStack);

        double targetX = target.getX() - witch.getX();
        double targetY = target.getEyeY() - thrownPotion.getY();
        double targetZ = target.getZ() - witch.getZ();
        double distance = Math.sqrt(targetX * targetX + targetZ * targetZ);
        double speed = 0.75F;
        double inaccuracy = 1.0F;

        Vec3 direction = new Vec3(targetX, targetY + distance * 0.2D, targetZ).normalize().scale(speed);

        direction = direction.add(
            witch.level.random.nextGaussian() * 0.0075D * inaccuracy,
            witch.level.random.nextGaussian() * 0.0075D * inaccuracy,
            witch.level.random.nextGaussian() * 0.0075D * inaccuracy
        );

        thrownPotion.setDeltaMovement(direction);

        witch.level.playSound(null, witch.getX(), witch.getY(), witch.getZ(), SoundEvents.WITCH_THROW, SoundSource.HOSTILE, 1.0F, 0.8F + witch.level.random.nextFloat() * 0.4F);

        witch.level.addFreshEntity(thrownPotion);
    }

    private static boolean hasWitchEnchantment(Player player) {
    	
        @SuppressWarnings("deprecation")
		boolean hasCurio = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof WitchEnchantment, player).isPresent();
        boolean hasForceConfig = FargosConfig.getConfigValue("witch_enchantment");
        boolean hasForce = hasForceOfMystic(player);
        
        return hasCurio || (hasForce && hasForceConfig);
    }
    @SuppressWarnings("deprecation")
    private static boolean hasForceOfMystic(Player player) {
    	boolean hasForce = CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof ForceOfMystic, player).isPresent();
        boolean hasSoul = hasSoulOfMinecraft(player);
        return hasForce || hasSoul;
    }
    @SuppressWarnings("deprecation")
    private static boolean hasSoulOfMinecraft(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof SoulOfMinecraft, player).isPresent();
    }

}

