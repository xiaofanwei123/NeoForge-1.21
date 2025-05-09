package com.xiaofanwei.xfws_someitems.items;

import com.xiaofanwei.xfws_someitems.registries.DataComponents;
import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class GatewayGlass extends Item {

    public GatewayGlass() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC));
    }

    private void setDimAndPos(ItemStack stack, BlockPos pos, ResourceLocation dimension) {
        stack.set(DataComponents.DIMENSION, dimension);
        stack.set(DataComponents.POSITION, pos);
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
    public int getUseDuration(ItemStack itemStack, LivingEntity living) {
        return 30;
    }


    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity living) {
        if (living instanceof Player player){
            if(player.isShiftKeyDown()){
                BlockPos pos = player.blockPosition();
                setDimAndPos(itemStack, pos, level.dimension().location());
                if(player instanceof ServerPlayer serverPlayer){
                    serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("message.xfws_someitems.gate_glass.0").withStyle(ChatFormatting.AQUA)));
                }
                return itemStack;
            }
            else {
                if(itemStack.get(DataComponents.DIMENSION) == null || itemStack.get(DataComponents.POSITION) == null){
                    if(living instanceof ServerPlayer serverPlayer){
                        serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("message.xfws_someitems.gate_glass.-1").withStyle(ChatFormatting.RED)));
                    }
                    return itemStack;
                }
                ResourceLocation dimension = itemStack.get(DataComponents.DIMENSION);
                BlockPos pos = itemStack.get(DataComponents.POSITION);
                XUtils.teleportTo(player, dimension, pos);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, player.getSoundSource(),1F,1F);
                if(player instanceof ServerPlayer serverPlayer){
                   serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("message.xfws_someitems.gate_glass.1").withStyle(ChatFormatting.AQUA)));
                }
                if (level.isClientSide) {
                    Minecraft.getInstance().gameRenderer.displayItemActivation(itemStack);
                }
                return itemStack;
            }
        }
        return itemStack;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.gateway_glass").withStyle(ChatFormatting.AQUA));
        if(itemStack.get(DataComponents.DIMENSION) != null && itemStack.get(DataComponents.POSITION) != null) {
            MinecraftInstanceHelper.ifPlayerPresent(player -> {
                tooltipComponents.addAll(List.of(
                        Component.translatable("tooltip.item.xfws_someitems.d").append(itemStack.get(DataComponents.DIMENSION).toString()).withStyle(ChatFormatting.WHITE),
                        Component.translatable("tooltip.item.xfws_someitems.p").append(itemStack.get(DataComponents.POSITION).toShortString()).withStyle(ChatFormatting.WHITE)
                ));
            });
        }
    }
}
