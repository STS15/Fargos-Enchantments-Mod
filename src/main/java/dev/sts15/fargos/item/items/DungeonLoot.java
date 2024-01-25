package dev.sts15.fargos.item.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.List;

public class DungeonLoot extends Item {

	public DungeonLoot() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    }

	@Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Looted from a Fargos Dungeon").withStyle(ChatFormatting.GRAY));
    }
}

