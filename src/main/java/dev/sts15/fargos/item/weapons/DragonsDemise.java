package dev.sts15.fargos.item.weapons;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.util.Random;

public class DragonsDemise extends Item {
    private static final double BASE_VELOCITY = 0.8;
    private int currentTick = 0;

    public DragonsDemise(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (world.isClientSide) {
            spawnFireParticles(world, player);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    private void spawnFireParticles(Level world, Player player) {
        double range = 10.0; // Example range for particle collision detection
        Random random = new Random();
        double horizontalAngle = player.getYRot() * (Math.PI / 180);
        double verticalAngle = player.getXRot() * (Math.PI / 180);

        double horizontalLookX = -Math.sin(horizontalAngle);
        double horizontalLookZ = Math.cos(horizontalAngle);
        double verticalLookY = -Math.sin(verticalAngle);

        double forwardOffset = 1.0; // 1 block forward
        double rightArmOffset = 0.45; // Offset to the right (player's arm)

        double spawnX = player.getX() + horizontalLookX * forwardOffset - horizontalLookZ * rightArmOffset;
        double spawnY = player.getY() + player.getEyeHeight() + verticalLookY * forwardOffset - 0.3;
        double spawnZ = player.getZ() + horizontalLookZ * forwardOffset + horizontalLookX * rightArmOffset;

        for (int i = 0; i < 1000; i++) {
            double spread = 0.3; // Adjust for wider or narrower cone
            double velocityX = BASE_VELOCITY * horizontalLookX + (random.nextDouble() - 0.5) * spread;
            double velocityY = BASE_VELOCITY * verticalLookY + (random.nextDouble() - 0.5) * spread;
            double velocityZ = BASE_VELOCITY * horizontalLookZ + (random.nextDouble() - 0.5) * spread;

            if (i % 5 == 0) {
                world.addParticle(ParticleTypes.DRAGON_BREATH, spawnX, spawnY, spawnZ, velocityX, velocityY, velocityZ);
            } else {
                world.addParticle(ParticleTypes.SMOKE, spawnX, spawnY, spawnZ, velocityX, velocityY, velocityZ);
            }
            if (i % 10 == 0) {
            	if (!world.isClientSide) {
            		//DamageParticleEntity damageParticle = new DamageParticleEntity(world, player, velocityX, velocityY, velocityZ, 1.0, 200);
                    //world.addFreshEntity(damageParticle);
            	}
            }
         
        }
    }


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level.isClientSide) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offHandItem = player.getOffhandItem();

            if ((mainHandItem.getItem() instanceof DragonsDemise) || (offHandItem.getItem() instanceof DragonsDemise)) {
                spawnFireParticles(player.level, player);
                currentTick++;
            } else {
                currentTick = 0;
            }
        }
    }
}
