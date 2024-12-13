package dev.sts15.fargos.item.weapons;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import java.util.ArrayList;
import java.util.List;
import dev.sts15.fargos.entity.summoned.SummonedVex;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TestSummoner extends Item {

    public TestSummoner(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
    	System.out.println("Item used by: " + player.getName().getString());
        if (!world.isClientSide()) {
            cleanSummonedEntities(player);
            int summonLimit = 3; // Base summon limit

            // Iterate through player's armor and increase summon limit for each tagged item
            for (ItemStack armorItem : player.getArmorSlots()) {
                if (armorItem.hasTag() && armorItem.getTag().contains("summon_limit_2")) {
                    summonLimit += 2;
                }
            }

            System.out.println("Current summon limit: " + summonLimit);
            List<Integer> summonedEntities = getSummonedEntities(player);
            System.out.println("Currently summoned entities: " + summonedEntities.size());
            if (summonedEntities.size() < summonLimit) {
                ServerLevel serverWorld = (ServerLevel) world;
                SummonedVex summonedVex = new SummonedVex(EntityType.VEX, serverWorld);
                summonedVex.setSummoner(player);
                summonedVex.setPos(player.getX(), player.getY() + 2, player.getZ());
                serverWorld.addFreshEntity(summonedVex);
                summonedEntities.add(summonedVex.getId());
                updateSummonedEntities(player, summonedEntities);
                return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
            } else {
            	player.sendSystemMessage(Component.literal("You have reached your summon limit of "+summonLimit+"!"));
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
    
    private static List<Integer> getSummonedEntities(Player player) {
        CompoundTag playerData = player.getPersistentData();
        List<Integer> entityIds = new ArrayList<>();
        if (playerData.contains("SummonedEntities", 10)) {
            CompoundTag summonData = playerData.getCompound("SummonedEntities");
            for (String key : summonData.getAllKeys()) {
                entityIds.add(summonData.getInt(key));
            }
        }

        return entityIds;
    }
    
    private static void updateSummonedEntities(Player player, List<Integer> entityIds) {
        System.out.println("Updating summoned entities for player: " + player.getName().getString());
        CompoundTag playerData = player.getPersistentData();
        CompoundTag summonData = new CompoundTag();
        for (int i = 0; i < entityIds.size(); i++) {
            summonData.putInt("Entity" + i, entityIds.get(i));
        }
        playerData.put("SummonedEntities", summonData);
    }

    private void cleanSummonedEntities(Player player) {
        System.out.println("Cleaning up dead summoned entities for player: " + player.getName().getString());
        List<Integer> summonedEntities = getSummonedEntities(player);
        List<Integer> aliveEntities = new ArrayList<>();

        for (Integer entityId : summonedEntities) {
            Entity entity = ((ServerLevel) player.level).getEntity(entityId);
            if (entity != null && entity.isAlive()) {
                aliveEntities.add(entityId);
            }
        }
        updateSummonedEntities(player, aliveEntities);
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        System.out.println("Entity died: " + event.getEntity().getName().getString());
        LivingEntity entity = event.getEntity();
        if (entity instanceof SummonedVex) {
            SummonedVex summonedVex = (SummonedVex) entity;
            Player summoner = summonedVex.getSummoner();
            if (summoner != null) {
                List<Integer> summonedEntities = getSummonedEntities(summoner);
                summonedEntities.remove((Integer) entity.getId());
                updateSummonedEntities(summoner, summonedEntities);
            }
        }
    }

}
