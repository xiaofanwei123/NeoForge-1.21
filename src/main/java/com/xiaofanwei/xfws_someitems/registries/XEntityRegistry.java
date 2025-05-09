package com.xiaofanwei.xfws_someitems.registries;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.entity.ManaStar;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class XEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE,MoreAC.MODID );

    public static final DeferredHolder<EntityType<?>, EntityType<ManaStar>> MANA_STAR = ENTITIES.register("mana_star", () ->
            EntityType.Builder.of(ManaStar::new, MobCategory.MISC)
                    .sized(0.3F, 0.3F)
                    .fireImmune()
                    .build("mana_star")
    );

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
