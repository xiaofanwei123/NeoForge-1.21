package com.xiaofanwei.xfws_someitems.util;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;

import net.minecraft.world.entity.LivingEntity;

import java.util.Calendar;

public class XUtils {

    public static MagicData getMagicData(LivingEntity living){
        return MagicData.getPlayerMagicData(living);
    }

    public static float getPresentMana(LivingEntity living){
        return getMagicData(living).getMana();
    }

    public static double getMaxMana(LivingEntity living){
        return living.getAttributeValue(AttributeRegistry.MAX_MANA);
    }

    public static void addMana(LivingEntity living, float mana){
        getMagicData(living).addMana(mana);
        getMagicData(living).addMana(-2);
    }


}
