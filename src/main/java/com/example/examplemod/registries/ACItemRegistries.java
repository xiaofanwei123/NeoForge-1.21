package com.example.examplemod.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACItemRegistries {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("xfws_more_ac");

    public static final DeferredItem<Item> EXAMPLE_ITEM;
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK;

    public ACItemRegistries() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    static {
        EXAMPLE_ITEM = ITEMS.register("example_item",()-> new Item(new Item.Properties()));

        EXAMPLE_BLOCK = ITEMS.register("example_block",()-> new BlockItem(ACBlockRegistries.EXAMPLE_BLOCK.get(), new Item.Properties()));


    }

}
