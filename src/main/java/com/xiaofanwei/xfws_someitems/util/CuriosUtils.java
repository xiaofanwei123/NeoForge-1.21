package com.xiaofanwei.xfws_someitems.util;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosUtils {
    public static boolean isPresence(LivingEntity living, Item Item){
        return CuriosApi.getCuriosHelper().findEquippedCurio(Item, living).isPresent();
    }

    public static MagicData getMagicData(LivingEntity living){
        return MagicData.getPlayerMagicData((ServerPlayer)living);
    }
}
