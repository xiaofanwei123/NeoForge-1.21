package com.xiaofanwei.xfws_someitems.event;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.items.EtherealLantern;
import com.xiaofanwei.xfws_someitems.registries.EntityRegistry;
import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import com.xiaofanwei.xfws_someitems.entity.ManaStarRenderer;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = MoreAC.MODID, value = Dist.CLIENT)
public final class ModClientEvent {
    // 注册实体渲染器
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.MANA_STAR.get(), ManaStarRenderer::new);
    }


    @SubscribeEvent //物品渲染
    public static void propertyOverride(FMLClientSetupEvent event)
    {
        ItemProperties.register(
                ItemRegistries.ETHEREAI_LANTERN.get(),
                ResourceLocation.fromNamespaceAndPath(MoreAC.MODID, "damage"),
                (stack, level, entity, seed) -> {
                    if((float) stack.getDamageValue() / stack.getMaxDamage()>0.8) {
                        return 1.0f;
                    } else if((float) stack.getDamageValue() / stack.getMaxDamage()<=0.8
                            && (float) stack.getDamageValue() / stack.getMaxDamage()>0.3){
                        return 0.5f;
                    } else {
                        return 0.0f;
                    }
                }
        );
    }
}
