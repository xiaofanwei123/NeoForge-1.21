package com.xiaofanwei.xfws_someitems.registries;

import com.xiaofanwei.xfws_someitems.MoreAC;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class XSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUNDS;
    public static final Supplier<SoundEvent> SCISSOR_GUILLOTINE_SNAP;
    public static final Supplier<SoundEvent> CRYSTAL_IMPACT;
    public static final Supplier<SoundEvent> RAZORBLADE_TYPHOON;
    public static final Supplier<SoundEvent> JINGLE_BELLS;

    public XSoundEvents() {
    }

    private static Supplier<SoundEvent> register(String id) {
        return SOUNDS.register(id, () -> {
            return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MoreAC.MODID, id));
        });
    }

    static {
        SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MoreAC.MODID);
        SCISSOR_GUILLOTINE_SNAP = register("scissor_guillotine_snap");
        CRYSTAL_IMPACT= register("crystal_impact");
        RAZORBLADE_TYPHOON= register("razorblade_typhoon");
        JINGLE_BELLS= register("jingle_bells");
    }
}
