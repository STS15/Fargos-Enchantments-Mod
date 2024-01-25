package dev.sts15.fargos.item.souls;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.mojang.math.Vector3f;

import dev.sts15.fargos.init.FargosConfig;

public class SoulOfTrawler extends Item implements ICurioItem {

    public SoulOfTrawler() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).defaultDurability(0));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (entity instanceof Player) {
            Player player = (Player) entity;

        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("F.I.S.H").withStyle(ChatFormatting.GRAY));
    }
}
