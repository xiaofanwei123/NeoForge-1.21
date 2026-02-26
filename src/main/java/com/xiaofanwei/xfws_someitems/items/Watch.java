package com.xiaofanwei.xfws_someitems.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class Watch extends Item {
    public Watch() {
        super(new Properties().stacksTo(1));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(!level.isClientSide&&player instanceof ServerPlayer serverPlayer){
            serverPlayer.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.watch.0").withStyle(ChatFormatting.LIGHT_PURPLE));
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.watch.1").withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
