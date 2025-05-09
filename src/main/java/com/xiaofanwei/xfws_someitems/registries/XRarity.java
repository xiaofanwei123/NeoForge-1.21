package com.xiaofanwei.xfws_someitems.registries;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.awt.Color;
import java.util.function.UnaryOperator;

public class XRarity {
    public static final EnumProxy<Rarity> RAINBOW = new EnumProxy<>(Rarity.class,-1, "xfws_someitems:rainbow", (UnaryOperator<Style>) ((style) -> style.withColor(Color.HSBtoRGB((System.currentTimeMillis() % 10000) / 10000F, 1f, 1F))));

}
