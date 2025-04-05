package com.xiaofanwei.xfws_someitems.registries;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.common.init.TCAttributes;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;

public class ItemRegistries {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("xfws_someitems");

    public static final DeferredItem<Item> EXAMPLE_ITEM;
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK;
    public static final DeferredItem<CurioBaseItem> NATURE_GIFT;
    public static final DeferredItem<CurioBaseItem> MANA_FLOWER;
    //magnet_flower
    public static final DeferredItem<CurioBaseItem> MAGNET_FLOWER;


    public ItemRegistries() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    static {
        EXAMPLE_ITEM = ITEMS.register("example_item",()->
                new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

        EXAMPLE_BLOCK = ITEMS.register("example_block",()-> new BlockItem(com.xiaofanwei.xfws_someitems.registries.BlockRegistries.EXAMPLE_BLOCK.get(), new Item.Properties()));

        NATURE_GIFT = ITEMS.register("nature_gift", () -> (
                new CurioBaseItem(ItemPropertiesHelper.equipment(1))
                        .withAttributes("accessory", new AttributeContainer[]{new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.05, ADD_MULTIPLIED_TOTAL)})));
        MANA_FLOWER = ITEMS.register("mana_flower", () -> (
                new CurioBaseItem(ItemPropertiesHelper.equipment(1))
                        .withAttributes("accessory", new AttributeContainer[]{new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.08, ADD_MULTIPLIED_TOTAL)})));
        MAGNET_FLOWER = ITEMS.register("magnet_flower", () -> (
                new CurioBaseItem(ItemPropertiesHelper.equipment(1))
                        .withAttributes("accessory", new AttributeContainer[]{new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.1, ADD_MULTIPLIED_TOTAL),
                                new AttributeContainer(TCAttributes.AGGRO, -300, ADD_VALUE)})));


}
    
    
//        BEZOAR = registerCurio("bezoar",builder -> builder
//                        .addAttributeModifier(Attributes.MAX_HEALTH, 10.0, ADD_VALUE)
//                        .addAttributeModifier(Attributes.MOVEMENT_SPEED, 0.1, ADD_VALUE)
//                , new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()
//        );
//    }

//    public static Supplier<CurioItem> registerCurio(String name, Consumer<CurioItem.Builder> consumer, Item.Properties properties) {
//        return ITEMS.register(name, () -> {
//            CurioItem.Builder builder = CurioItem.builder(name,properties);
//            consumer.accept(builder);
//            return builder.build();
//        });
//    }
}
