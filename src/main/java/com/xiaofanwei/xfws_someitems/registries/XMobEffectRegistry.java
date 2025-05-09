package com.xiaofanwei.xfws_someitems.registries;

import com.xiaofanwei.xfws_someitems.MoreAC;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class XMobEffectRegistry {


    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, MoreAC.MODID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }

    public static final DeferredHolder<MobEffect, MobEffect> MANA_SiCKNESS = MOB_EFFECT_DEFERRED_REGISTER.register(
            "mana_sickness", () -> new MagicMobEffect(MobEffectCategory.HARMFUL, 9109643).addAttributeModifier(AttributeRegistry.SPELL_POWER, MoreAC.Resource("mana_sickness"), -0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

}