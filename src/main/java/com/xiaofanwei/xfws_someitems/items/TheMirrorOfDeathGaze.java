package com.xiaofanwei.xfws_someitems.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

public class TheMirrorOfDeathGaze extends Item {
    public TheMirrorOfDeathGaze() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 30;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity living) {
        if (living instanceof ServerPlayer serverPlayer && serverPlayer.getLastDeathLocation().isEmpty()){
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("message.xfws_someitems.the_mirror_of_death_gaze.-1").withStyle(ChatFormatting.DARK_RED)));
            return itemStack;
        }
        if (level.isClientSide) {
            Minecraft.getInstance().gameRenderer.displayItemActivation(itemStack);
        }
        if (living instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.getVehicle() != null) {
                serverPlayer.removeVehicle();
            }
            serverPlayer.getCooldowns().addCooldown(this, serverPlayer.isCreative() ? 0 : 20*15);
            ResourceKey<Level> dimension = serverPlayer.getLastDeathLocation().get().dimension();
            ServerLevel destinationLevel = serverPlayer.server.getLevel(dimension);
            BlockPos blockPos = serverPlayer.getLastDeathLocation().get().pos();
            if (destinationLevel != null) {
                serverPlayer.teleportTo( destinationLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.ZOMBIE_INFECT, serverPlayer.getSoundSource(),1F,1F);
                int randomInt = new Random().nextInt(5);
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("message.xfws_someitems.the_mirror_of_death_gaze"+"."+randomInt).withStyle(ChatFormatting.DARK_PURPLE)));
            }
        }
        return itemStack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.the_mirror_of_death_gaze").withStyle(ChatFormatting.DARK_PURPLE));
    }

}
