package com.xiaofanwei.xfws_someitems.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "xfws_someitems");

    public CreativeTabRegistry() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }


    static {
        CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.examplemod"))
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> com.xiaofanwei.xfws_someitems.registries.ItemRegistries.NATURE_GIFT.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    output.accept(ItemRegistries.NATURE_GIFT.get());
                    output.accept(ItemRegistries.MANA_FLOWER.get());
                    output.accept(ItemRegistries.MAGNET_FLOWER.get());
                }).build());

    }

}
