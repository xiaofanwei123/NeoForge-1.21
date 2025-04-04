package com.example.examplemod.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACCreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "xfws_more_ac");

    public ACCreativeTabRegistry() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }


    static {
        CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.examplemod"))
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> ACItemRegistries.EXAMPLE_ITEM.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    output.accept(ACItemRegistries.EXAMPLE_ITEM.get());
                    output.accept(ACItemRegistries.EXAMPLE_BLOCK.get());
                }).build());

    }

}
