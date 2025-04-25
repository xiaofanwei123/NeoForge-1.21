package com.xiaofanwei.xfws_someitems.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.List;


public class CuriosUtils {
    public static boolean isPresence(LivingEntity entity, Item... items) {
        List<Item> targetItems = Arrays.stream(items).toList();
        return CuriosApi.getCuriosInventory(entity)
                .map(handler ->
                        handler.isEquipped(stack -> targetItems.contains(stack.getItem()))
                ).orElse(false);
    }

    public static boolean isPresence(LivingEntity living, Item Item){
        return CuriosApi.getCuriosHelper().findEquippedCurio(Item, living).isPresent();
    }

//    public static boolean hasAccessoriesType(LivingEntity living, ValueType<Unit, UnitValue> type) {
//        return living.getData(TCAttachments.ACCESSORIES).contains(type);
//    }

}
