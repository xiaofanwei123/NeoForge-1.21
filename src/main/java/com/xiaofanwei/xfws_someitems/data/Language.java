package com.xiaofanwei.xfws_someitems.data;

import com.xiaofanwei.xfws_someitems.MoreAC;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class Language extends LanguageProvider {

    public Language(PackOutput output, String modid, String locale) {
        super(output, MoreAC.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addTooltips();
    }

    private void addTooltips() {
        add("item.xfws_someitems.nature_gift.tooltip", "Nature Gift");
        add("item.xfws_someitems.mana_flower.tooltip", "Mana Flower");
        add("item.xfws_someitems.magnet_flower.tooltip", "Magnet Flower");
    }
}
