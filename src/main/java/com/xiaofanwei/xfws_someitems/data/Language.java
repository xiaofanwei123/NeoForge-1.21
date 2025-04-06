package com.xiaofanwei.xfws_someitems.data;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;


public class Language extends LanguageProvider {
    private final String locale;
    private final PackOutput output;
    private final Map<String, String> enData = new TreeMap<>();
    private final Map<String, String> zhData = new TreeMap<>();
//    public Language(PackOutput output) {
//        super(output, MoreAC.MODID, "en_us");
//    }

    ;

    public Language(PackOutput output, String locale) {
        super(output, MoreAC.MODID, locale);
        this.output = output;
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        addTooltips();
    }

    private void addTooltips() {
        add("item.xfws_someitems.nature_gift", "Nature Gift");
        add("item.xfws_someitems.mana_flower", "Mana Flower");
        add("item.xfws_someitems.magnet_flower", "Magnet Flower");}

}
