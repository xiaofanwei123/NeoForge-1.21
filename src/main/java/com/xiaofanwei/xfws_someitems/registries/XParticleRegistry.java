package com.xiaofanwei.xfws_someitems.registries;

import com.xiaofanwei.xfws_someitems.MoreAC;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class XParticleRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLES=DeferredRegister.create(Registries.PARTICLE_TYPE, MoreAC.MODID);

    public static final Supplier<SimpleParticleType> PARRY = PARTICLES.register("parry",() -> new SimpleParticleType(false));

    public static void register(IEventBus modBus) {
        PARTICLES.register(modBus);
    }

}
