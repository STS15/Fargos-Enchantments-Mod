package dev.sts15.fargos.item.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import dev.sts15.fargos.entity.custom.ArrowEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.EntityType;

public class MutantVoodoo extends Item {

    public MutantVoodoo(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide() && player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            ServerLevel serverLevel = (ServerLevel) world;

            // Main attack function
            circleSpikeAttack(serverPlayer, serverLevel);

            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    private void circleSpikeAttack(ServerPlayer player, ServerLevel world) {
        int numParticlesCircle = 10;
        double radius = 1.0;
        double lineLength = 5.0;

        // Always show the center circle
        showSoulFireCircle(player, numParticlesCircle, radius, world, 0.0);

        // First round - No offset
        animateLinesLikeClock(player, numParticlesCircle, radius, lineLength, world, 0.0);
        addDelay(500); // Delay in milliseconds

        showAllLines(player, numParticlesCircle, radius, lineLength, world, 0.0);
        addDelay(100); // Delay in milliseconds

        shootArrows(player, numParticlesCircle, radius, lineLength, world, 0.0);
        addDelay(1000); // Delay in milliseconds

        // Second round - With offset
        double angleOffset = Math.PI / numParticlesCircle;
        animateLinesLikeClock(player, numParticlesCircle, radius, lineLength, world, angleOffset);
        addDelay(500); // Delay in milliseconds

        showAllLines(player, numParticlesCircle, radius, lineLength, world, angleOffset);
        addDelay(100); // Delay in milliseconds

        shootArrows(player, numParticlesCircle, radius, lineLength, world, angleOffset);
    }

    private void addDelay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void performAttackSequence(ServerPlayer player, int numParticlesCircle, double radius, double lineLength, ServerLevel world, double angleOffset) {
        showSoulFireCircle(player, numParticlesCircle, radius, world, angleOffset);
        animateLinesLikeClock(player, numParticlesCircle, radius, lineLength, world, angleOffset);
        showAllLines(player, numParticlesCircle, radius, lineLength, world, angleOffset);
        shootArrows(player, numParticlesCircle, radius, lineLength, world, angleOffset);
    }

    private void showSoulFireCircle(ServerPlayer player, int numParticlesCircle, double radius, ServerLevel world, double angleOffset) {
        Vec3 playerPos = player.position();
        double waistHeight = playerPos.y + 0.8;

        for (int i = 0; i < numParticlesCircle; i++) {
            double angle = 2 * Math.PI * i / numParticlesCircle + angleOffset;
            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);

            world.sendParticles(player, ParticleTypes.SOUL_FIRE_FLAME, 
                                true, 
                                playerPos.x + xOffset, 
                                waistHeight, 
                                playerPos.z + zOffset, 
                                1, 0, 0, 0, 0);
        }
    }

    private void animateLinesLikeClock(ServerPlayer player, int numParticlesCircle, double radius, double lineLength, ServerLevel world, double angleOffset) {
        Vec3 playerPos = player.position();
        double waistHeight = playerPos.y + 0.8;

        for (int currentLine = 0; currentLine < numParticlesCircle; currentLine++) {
            double angle = 2 * Math.PI * currentLine / numParticlesCircle + angleOffset;
            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);

            Vec3 circleParticlePos = new Vec3(playerPos.x + xOffset, waistHeight, playerPos.z + zOffset);

            for (int j = 1; j <= lineLength * 2; j++) {
                double x = circleParticlePos.x + (j * 0.5) * Math.cos(angle);
                double z = circleParticlePos.z + (j * 0.5) * Math.sin(angle);

                world.sendParticles(player, ParticleTypes.SOUL_FIRE_FLAME,
                                    true,
                                    x, waistHeight, z,
                                    1, 0, 0, 0, 0);
            }
            
            // Add a delay between each line
            try {
                Thread.sleep(100); // Adjust this value as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAllLines(ServerPlayer player, int numParticlesCircle, double radius, double lineLength, ServerLevel world, double angleOffset) {
        Vec3 playerPos = player.position();
        double waistHeight = playerPos.y + 0.8;
        int numParticlesWide = 3; // Number of particles in width

        for (int i = 0; i < numParticlesCircle; i++) {
            double angle = 2 * Math.PI * i / numParticlesCircle + angleOffset;
            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);

            Vec3 circleParticlePos = new Vec3(playerPos.x + xOffset, waistHeight, playerPos.z + zOffset);

            for (int j = 1; j <= lineLength * 2; j++) {
                double x = circleParticlePos.x + (j * 0.5) * Math.cos(angle);
                double z = circleParticlePos.z + (j * 0.5) * Math.sin(angle);

                // Create a dense line with three particles in width
                for (int k = 0; k < numParticlesWide; k++) {
                    double xOffsetWide = (k - 1) * 0.1; // Adjust this value for the width of the line
                    world.sendParticles(player, ParticleTypes.SOUL_FIRE_FLAME,
                            true,
                            x + xOffsetWide, waistHeight, z,
                            1, 0, 0, 0, 0);
                }
            }
        }
    }


    private void shootArrows(ServerPlayer player, int numParticlesCircle, double radius, double lineLength, ServerLevel world, double angleOffset) {
        Vec3 playerPos = player.position();
        double waistHeight = playerPos.y + 0.8;

        for (int i = 0; i < numParticlesCircle; i++) {
            double angle = 2 * Math.PI * i / numParticlesCircle + angleOffset;
            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);
            Vec3 circleParticlePos = new Vec3(playerPos.x + xOffset, waistHeight, playerPos.z + zOffset);

            Vec3 farthestLineParticlePos = new Vec3(
                circleParticlePos.x + lineLength * Math.cos(angle),
                waistHeight,
                circleParticlePos.z + lineLength * Math.sin(angle)
            );

            // Create an instance of your custom ArrowEntity
            Arrow arrow = new Arrow(world, player);
            arrow.setPos(circleParticlePos.x, circleParticlePos.y, circleParticlePos.z);
            arrow.setNoGravity(false);

            Vec3 directionVec = farthestLineParticlePos.subtract(circleParticlePos).normalize();
            arrow.shoot(directionVec.x, directionVec.y, directionVec.z, 1.0f, 0);

            world.addFreshEntity(arrow);
        }
    }
}