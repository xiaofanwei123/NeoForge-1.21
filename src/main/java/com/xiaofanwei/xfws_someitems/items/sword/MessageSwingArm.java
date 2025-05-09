package com.xiaofanwei.xfws_someitems.items.sword;


import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.XItemRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;


//武器挥动需要客户端向服务端发送
public record MessageSwingArm() implements CustomPacketPayload {

    public static final Type<MessageSwingArm> TYPE = new Type<>(MoreAC.Resource("message_swing_arm"));
    public static final StreamCodec<ByteBuf, MessageSwingArm> STREAM_CODEC = CustomPacketPayload.codec(
            MessageSwingArm::encode,
            MessageSwingArm::decode
    );

    private void encode(ByteBuf byteBuf) {}

    private static MessageSwingArm decode(ByteBuf byteBuf) {
        return new MessageSwingArm();
    }

    @Override
    public Type<MessageSwingArm> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer){
                if ( serverPlayer.getMainHandItem().getItem() == XItemRegistry.SCULK_KATANA.get() && serverPlayer.swingTime == 0) {
                    Sculk_Katana.onLeftClick(context.player(), 0.3*context.player().getAttributeValue(Attributes.ATTACK_DAMAGE));
                }
                if( serverPlayer.getMainHandItem().getItem() == XItemRegistry.ARK_OF_THE_COSMOS.get() && serverPlayer.swingTime == 0) {
                    ArkOfTheCosmos.onLeftClick(context.player());
                }
        }}).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
