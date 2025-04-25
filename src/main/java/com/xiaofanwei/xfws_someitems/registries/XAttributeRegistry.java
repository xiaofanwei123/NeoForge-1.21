package com.xiaofanwei.xfws_someitems.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


@EventBusSubscriber(modid = "xfws_someitems", bus = EventBusSubscriber.Bus.MOD)
public class XAttributeRegistry {
    private static final DeferredRegister<Attribute> ATTRIBUTES=DeferredRegister.create(Registries.ATTRIBUTE, "xfws_someitems");
    public static final DeferredHolder<Attribute, Attribute> MANASTAR_DISTANCE;

    static {
        MANASTAR_DISTANCE = ATTRIBUTES.register("manastar_distance", () -> (new RangedAttribute("attribute.xfws_someitems.manastar_distance", 5, 0,25 )).setSyncable(true));
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach((entity) -> {
            ATTRIBUTES.getEntries().forEach((attribute) -> {
                e.add(entity, attribute);
            });
        });
    }
}
