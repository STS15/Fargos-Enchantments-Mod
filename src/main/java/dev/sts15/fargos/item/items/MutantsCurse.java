package dev.sts15.fargos.item.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import dev.sts15.fargos.entity.custom.MutantEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;

public class MutantsCurse extends Item {
    private final RegistryObject<EntityType<MutantEntity>> entityType;

    public MutantsCurse(RegistryObject<EntityType<MutantEntity>> mutant, Properties properties) {
        super(properties);
        this.entityType = mutant;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }

        if (!world.isClientSide) {
            EntityType<?> type = entityType.get();
            Mob entity = (Mob) type.create(world);
            if (entity != null) {
                entity.moveTo(context.getClickedPos().getX() + 0.5, 
                              context.getClickedPos().getY() + 1, 
                              context.getClickedPos().getZ() + 0.5, 
                              0.0F, 
                              0.0F);
                world.addFreshEntity(entity);
            }
        }

        context.getItemInHand().shrink(1); // Decrease the item count by 1
        return InteractionResult.CONSUME;
    }
}