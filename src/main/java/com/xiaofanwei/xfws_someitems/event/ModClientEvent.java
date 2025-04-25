package com.xiaofanwei.xfws_someitems.event;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.EntityRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import com.xiaofanwei.xfws_someitems.entity.ManaStarRenderer;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = MoreAC.MODID, value = Dist.CLIENT)
public final class ModClientEvent {
    // 注册实体渲染器
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.MANA_STAR.get(), ManaStarRenderer::new);
    }


}
