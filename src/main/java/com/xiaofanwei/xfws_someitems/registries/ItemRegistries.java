package com.xiaofanwei.xfws_someitems.registries;

import com.xiaofanwei.xfws_someitems.items.curios.CurioItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
    public static final Supplier<CurioItem> BAND_OF_STARPOWER;
    public static final Supplier<CurioItem> MANA_FLOWER;
    public static final Supplier<CurioItem> MAGNET_FLOWER;
    public static final Supplier<CurioItem> MANA_REGENERATION_BAND;
    public static final Supplier<CurioItem> MAGIC_CUFFS;
    public static final Supplier<CurioItem> ANCIENT_FOSSIL;

    public ItemRegistries() {
    }

    public static void register(IEventBus eventBus) {
        CURIOS.register(eventBus);
    }

    static {
        ANCIENT_FOSSIL=registerCurio("ancient_fossil",builder -> builder
                .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, 0.15, ADD_MULTIPLIED_TOTAL));

        NATURE_GIFT = registerCurio("nature_gift",builder -> builder
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.05, ADD_MULTIPLIED_TOTAL));

        BAND_OF_STARPOWER= registerCurio("band_of_starpower",builder -> builder.jeiTooltip()
                .addAttributeModifier(AttributeRegistry.MAX_MANA, 50, ADD_VALUE));

        MANA_FLOWER= registerCurio("mana_flower",builder -> builder
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.08, ADD_MULTIPLIED_TOTAL)
                .addTooltip(),
                new Item.Properties().rarity(Rarity.RARE));

        MAGNET_FLOWER= registerCurio("magnet_flower",builder -> builder
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.1, ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(TCAttributes.AGGRO, -300, ADD_VALUE)
                .addTooltip(),
                new Item.Properties().rarity(Rarity.EPIC));

        MANA_REGENERATION_BAND =registerCurio("mana_regeneration_band", builder -> builder
                .addAttributeModifier(AttributeRegistry.MAX_MANA, 50, ADD_VALUE)
                .addAttributeModifier(AttributeRegistry.MANA_REGEN, 0.1, ADD_MULTIPLIED_TOTAL),
                new Item.Properties().rarity(Rarity.RARE));

        MAGIC_CUFFS = registerCurio("magic_cuffs", builder -> builder
                .addAttributeModifier(AttributeRegistry.MAX_MANA, 100, ADD_VALUE)
                .addAttributeModifier(AttributeRegistry.MANA_REGEN, 0.15, ADD_MULTIPLIED_TOTAL)
                .addTooltip(),
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
