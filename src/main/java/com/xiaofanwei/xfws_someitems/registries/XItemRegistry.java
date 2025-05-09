package com.xiaofanwei.xfws_someitems.registries;

import com.xiaofanwei.xfws_someitems.items.EtherealLantern;
import com.xiaofanwei.xfws_someitems.items.GatewayGlass;
import com.xiaofanwei.xfws_someitems.items.TheMirrorOfDeathGaze;
import com.xiaofanwei.xfws_someitems.items.curios.CurioItem;
import com.xiaofanwei.xfws_someitems.items.sword.ArkOfTheCosmos;
import com.xiaofanwei.xfws_someitems.items.sword.Sculk_Katana;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.common.init.TCAttributes;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;

public class XItemRegistry {
    //COMMONItem为数据生成时会自动生成单一model.json
    //Item为数据生成时不会自动生成单一model.json
    //COMMONCURIOS为数据生成时会自动生成单一model.json和accessory的tag
    //CURIOS为数据生成时不会自动生成单一model.json但会生成accessory的tag
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("xfws_someitems");
    public static final DeferredRegister.Items COMMONITEMS = DeferredRegister.createItems("xfws_someitems");
    public static final DeferredRegister.Items CURIOS = DeferredRegister.createItems("xfws_someitems");
    public static final DeferredRegister.Items COMMONCURIOS = DeferredRegister.createItems("xfws_someitems");

    public static final DeferredItem<SwordItem> SCULK_KATANA;
    //public static final DeferredItem<SwordItem> BALEFUL_HARVESTER_SCYTHE;
    public static final DeferredItem<SwordItem> ARK_OF_THE_COSMOS;

    public static final DeferredItem<Item> THE_MIRROR_OF_DEATH_GAZE;
    public static final DeferredItem<Item> GATEWAY_GLASS;

    public static final Supplier<CurioItem> ETHEREAI_LANTERN;
    public static final Supplier<CurioItem> NATURE_GIFT;
    public static final Supplier<CurioItem> BAND_OF_STARPOWER;
    public static final Supplier<CurioItem> MANA_FLOWER;
    public static final Supplier<CurioItem> MAGNET_FLOWER;
    public static final Supplier<CurioItem> MANA_REGENERATION_BAND;
    public static final Supplier<CurioItem> MAGIC_CUFFS;
    public static final Supplier<CurioItem> ANCIENT_FOSSIL;
    public static final Supplier<CurioItem> SCULK_MEGAPHONE;
    public static final Supplier<CurioItem> ARCANE_FLOWER;
    public static final Supplier<CurioItem> CELESTIAL_MAGNET;
    public static final Supplier<CurioItem> CELESTIAL_CUFFS;


    public XItemRegistry() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        COMMONITEMS.register(eventBus);
        CURIOS.register(eventBus);
        COMMONCURIOS.register(eventBus);
    }

    static{
        SCULK_KATANA = ITEMS.register("sculk_katana", ()-> new Sculk_Katana(
                new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2000, -4f, -1f, 10, () -> Ingredient.of(Items.ECHO_SHARD)), 10,1.6f));

        ARK_OF_THE_COSMOS = ITEMS.register("ark_of_the_cosmos", ()-> new ArkOfTheCosmos(new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 7, -4f, -1f, 30, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get())), 10,1.6f));

        //BALEFUL_HARVESTER_SCYTHE = ITEMS.register("baleful_harvester_scythe", ()-> new BalefulHarvesterScythe());

        THE_MIRROR_OF_DEATH_GAZE = COMMONITEMS.register("the_mirror_of_death_gaze", TheMirrorOfDeathGaze::new);

        GATEWAY_GLASS = COMMONITEMS.register("gateway_glass", GatewayGlass::new);
    }

    static {
        //因为他的modeljson是特殊的，所以不用CURIOS
        ETHEREAI_LANTERN = CURIOS.register("ethereal_lantern", EtherealLantern::new);

        SCULK_MEGAPHONE= registerCurio("sculk_megaphone", CurioItem.Builder::addTooltip,
                new Item.Properties().rarity(Rarity.EPIC));

        ANCIENT_FOSSIL=registerCurio("ancient_fossil",builder -> builder
                .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, 0.15, ADD_MULTIPLIED_TOTAL));

        NATURE_GIFT = registerCurio("nature_gift",builder -> builder
                .addJeiTooltip()
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.05, ADD_MULTIPLIED_TOTAL));

        BAND_OF_STARPOWER= registerCurio("band_of_starpower",builder -> builder
                .addJeiTooltip()
                .addAttributeModifier(AttributeRegistry.MAX_MANA, 50, ADD_VALUE));

        MANA_FLOWER= registerCurio("mana_flower",builder -> builder
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.08, ADD_MULTIPLIED_TOTAL)
                .addTooltip(),
                new Item.Properties().rarity(Rarity.RARE));

        ARCANE_FLOWER= registerCurio("arcane_flower",builder -> builder
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.1, ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(TCAttributes.AGGRO, -300, ADD_VALUE)
                .addTooltip(),
                new Item.Properties().rarity(Rarity.EPIC));

        MAGNET_FLOWER= registerCurio("magnet_flower",builder -> builder
                .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, 0.1, ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(XAttributeRegistry.MANASTAR_DISTANCE, 20, ADD_VALUE)
                .addTooltips(2),
                new Item.Properties().rarity(Rarity.EPIC));

        MANA_REGENERATION_BAND =registerCurio("mana_regeneration_band", builder -> builder
                .addAttributeModifier(AttributeRegistry.MAX_MANA, 50, ADD_VALUE)
                .addAttributeModifier(AttributeRegistry.MANA_REGEN, 0.05, ADD_MULTIPLIED_TOTAL),
                new Item.Properties().rarity(Rarity.RARE));

        MAGIC_CUFFS = registerCurio("magic_cuffs", builder -> builder
                .addAttributeModifier(AttributeRegistry.MAX_MANA, 100, ADD_VALUE)
                .addAttributeModifier(AttributeRegistry.MANA_REGEN, 0.10, ADD_MULTIPLIED_TOTAL)
                .addTooltip(),
                new Item.Properties().rarity(Rarity.EPIC));

        CELESTIAL_MAGNET = registerCurio("celestial_magnet", builder -> builder
                .addTooltip()
                .addJeiTooltip()
                .addAttributeModifier(XAttributeRegistry.MANASTAR_DISTANCE, 15, ADD_VALUE)
                , new Item.Properties().rarity(Rarity.UNCOMMON));

        CELESTIAL_CUFFS = registerCurio("celestial_cuffs", builder -> builder
                .addAttributeModifier(AttributeRegistry.MAX_MANA, 125, ADD_VALUE)
                .addAttributeModifier(AttributeRegistry.MANA_REGEN, 0.15, ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(XAttributeRegistry.MANASTAR_DISTANCE, 20, ADD_VALUE)
                .addTooltip()
                , new Item.Properties().rarity(Rarity.EPIC));


    }

    public static Supplier<CurioItem> registerCurio(String name, Consumer<CurioItem.Builder> consumer, Item.Properties properties) {
        return COMMONCURIOS.register(name, () -> {
            CurioItem.Builder builder = CurioItem.builder(name,properties);
            consumer.accept(builder);
            return builder.build();
        });
    }

    public static Supplier<CurioItem> registerCurio(String name, Consumer<CurioItem.Builder> consumer) {
        return COMMONCURIOS.register(name, () -> {
            CurioItem.Builder builder = CurioItem.builder(name);
            consumer.accept(builder);
            return builder.build();
        });
    }

    public static Supplier<CurioItem> registerCurio(String name, Supplier<CurioItem> supplier) {
        return COMMONCURIOS.register(name, supplier);
    }

}
