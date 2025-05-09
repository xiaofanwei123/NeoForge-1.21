package com.xiaofanwei.xfws_someitems.event;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.XEntityRegistry;
import com.xiaofanwei.xfws_someitems.registries.XItemRegistry;
import com.xiaofanwei.xfws_someitems.registries.XParticleRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import com.xiaofanwei.xfws_someitems.particle.ParryParticle.ParticleFactory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import com.xiaofanwei.xfws_someitems.entity.ManaStarRenderer;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = MoreAC.MODID, value = Dist.CLIENT)
public final class ModClientEvent {
    // 注册实体渲染器
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(XEntityRegistry.MANA_STAR.get(), ManaStarRenderer::new);
    }

    // 注册粒子效果
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(XParticleRegistry.PARRY.get(), ParticleFactory::new);
    }


    @SubscribeEvent //物品渲染
    public static void propertyOverride(FMLClientSetupEvent event)
    {

        ItemProperties.register(
                XItemRegistry.ETHEREAI_LANTERN.get(),
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
