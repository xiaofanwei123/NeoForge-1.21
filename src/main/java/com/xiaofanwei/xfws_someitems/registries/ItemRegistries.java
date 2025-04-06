package com.xiaofanwei.xfws_someitems.registries;

import com.xiaofanwei.xfws_someitems.items.curios.CurioItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.common.init.TCAttributes;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;

public class ItemRegistries {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("xfws_someitems");
    public static final DeferredRegister.Items CURIOS = DeferredRegister.createItems("xfws_someitems");

    public static final Supplier<CurioItem> NATURE_GIFT;
    public static final Supplier<CurioItem> MANA_FLOWER;
    public static final Supplier<CurioItem> MAGNET_FLOWER;

    public ItemRegistries() {
    }

    public static void register(IEventBus eventBus) {
        CURIOS.register(eventBus);
    }

    static {
        NATURE_GIFT = registerCurio("nature_gift",builder -> builder
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.05, ADD_MULTIPLIED_TOTAL));

        MANA_FLOWER= registerCurio("mana_flower",builder -> builder
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.08, ADD_MULTIPLIED_TOTAL)
                .addTooltip(Component.translatable("tooltip.item.xfws_someitems.mana_flower")),
                new Item.Properties().rarity(Rarity.RARE));

        MAGNET_FLOWER= registerCurio("magnet_flower",builder -> builder
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.1, ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(TCAttributes.AGGRO, -300, ADD_VALUE)
                .addTooltip(Component.translatable("tooltip.item.xfws_someitems.magnet_flower")),
                new Item.Properties().rarity(Rarity.EPIC));
}

    public static Supplier<CurioItem> registerCurio(String name, Consumer<CurioItem.Builder> consumer, Item.Properties properties) {
        return CURIOS.register(name, () -> {
            CurioItem.Builder builder = CurioItem.builder(name,properties);
            consumer.accept(builder);
            return builder.build();
        });
    }

    public static Supplier<CurioItem> registerCurio(String name, Consumer<CurioItem.Builder> consumer) {
        return CURIOS.register(name, () -> {
            CurioItem.Builder builder = CurioItem.builder(name);
            consumer.accept(builder);
            return builder.build();
        });
    }
}
