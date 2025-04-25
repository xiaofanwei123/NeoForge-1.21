package com.xiaofanwei.xfws_someitems.items.curios;


import com.xiaofanwei.xfws_someitems.MoreAC;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import com.xiaofanwei.xfws_someitems.util.CuriosUtils;
import org.jetbrains.annotations.ApiStatus;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.ArrayList;
import java.util.List;

public class CurioItem extends Item implements ICurioItem {
    private final List<Component> tooltipComponents;
    private final boolean enableJeiTooltip;

    protected static final ImmutableMultimap<Holder<Attribute>, AttributeModifier> EMPTY_ATTRIBUTE = ImmutableMultimap.of();
    protected Builder builder;

    public CurioItem(Builder builder) {
        super(builder.initialize().properties);
        this.builder = builder;
        this.tooltipComponents = builder.tooltipComponents;
        this.enableJeiTooltip = builder.enableJeiTooltip;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return this.canEquip(slotContext, stack);
    }

    //无法穿戴相同
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
//        LivingEntity entity= slotContext.entity();
//        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(entity);
//        if (curiosInventory.isEmpty()) {
//            return true;
//        }
//        for (ICurioStacksHandler curioStacksHandler : curiosInventory.get().getCurios().values()) {
//            IDynamicStackHandler stackHandler = curioStacksHandler.getStacks();
//            for (int i = 0; i < stackHandler.getSlots(); i++) {
//                ItemStack itemstack = stackHandler.getStackInSlot(i);
//                if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem()) {
//                    return false;
//                }
//            }
//        }
//        return true;
        return !CuriosUtils.isPresence(slotContext.entity(),stack.getItem());
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return builder == null ? EMPTY_ATTRIBUTE : builder.attributes;
    }

    public boolean enableJeiTooltip() {
        return enableJeiTooltip;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static Builder builder(String name,Properties properties) {
        return new Builder(name, properties);
    }

    public static class Builder {
        private List<Component> tooltipComponents = new ArrayList<>();
        private boolean enableJeiTooltip = false;
        private final String itemName;
        private Item.Properties properties = new Item.Properties();
        private transient ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> attributesBuilder = ImmutableMultimap.builder();
        private ImmutableMultimap<Holder<Attribute>, AttributeModifier> attributes;

        public Builder(String itemName) {
            this.itemName = itemName;
        }

        public Builder(String itemName,Properties properties) {
            this.itemName = itemName;
            this.properties=properties;
        }


        public Builder addTooltip() {
            String key = "tooltip.item." + MoreAC.MODID + "." + itemName;
            this.tooltipComponents.add(Component.translatable(key));
            return this;
        }

        public Builder jeiTooltip() {
            this.enableJeiTooltip = true;
            return this;
        }

        @ApiStatus.Internal
        public Builder initialize() {
            properties.stacksTo(1)/*.component(TCDataComponentTypes.MOD_RARITY, rarity)*/;
            this.attributes = attributesBuilder.build();
            this.attributesBuilder = null;
            return this;
        }

        public Builder addAttributeModifier(Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation) {
            attributesBuilder.put(attribute,new AttributeModifier(MoreAC.Resource(itemName), amount, operation));
            return this;
        }


        public CurioItem build() {
            return new CurioItem(this);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.addAll(tooltipComponents);
    }

}
