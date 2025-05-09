package com.xiaofanwei.xfws_someitems.items;

import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.util.IMinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

    private GlobalPos LastDeathLocation(Player player){
        if(player.getLastDeathLocation().isPresent()){
            return player.getLastDeathLocation().get();
        }
        return null;
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
        if (living instanceof ServerPlayer serverPlayer && LastDeathLocation(serverPlayer) == null) {
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("message.xfws_someitems.the_mirror_of_death_gaze.-1").withStyle(ChatFormatting.DARK_RED)));
            return itemStack;
        }

        if (living instanceof Player player && LastDeathLocation(player) != null) {
            player.getCooldowns().addCooldown(this, player.isCreative() ? 0 : 20*15);
            GlobalPos globalPos = LastDeathLocation(player);
            XUtils.teleportTo(player, globalPos.dimension().location(), globalPos.pos());
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_INFECT, player.getSoundSource(),1F,1F);
            int randomInt = new Random().nextInt(5);
            if(player instanceof ServerPlayer serverPlayer){
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("message.xfws_someitems.the_mirror_of_death_gaze"+"."+randomInt).withStyle(ChatFormatting.DARK_PURPLE)));
            }
            if (level.isClientSide) {
                Minecraft.getInstance().gameRenderer.displayItemActivation(itemStack);
            }
        }
        return itemStack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.the_mirror_of_death_gaze").withStyle(ChatFormatting.DARK_PURPLE));
        MinecraftInstanceHelper.ifPlayerPresent(player -> {
            if(LastDeathLocation(player) != null){
            tooltipComponents.addAll( List.of(
                Component.translatable("tooltip.item.xfws_someitems.d").withStyle(ChatFormatting.DARK_PURPLE).append(LastDeathLocation(player).dimension().location().toString()).withStyle(ChatFormatting.LIGHT_PURPLE),
                Component.translatable("tooltip.item.xfws_someitems.p").withStyle(ChatFormatting.DARK_PURPLE).append(LastDeathLocation(player).pos().toShortString()).withStyle(ChatFormatting.LIGHT_PURPLE)
                    ));
            }
        });
    }

}
