package com.xiaofanwei.xfws_someitems.data;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.List;
import java.util.function.Supplier;


public class Language extends LanguageProvider {
    private final String locale;

    public Language(PackOutput output, String locale) {
        super(output, MoreAC.MODID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        addItem();
    }

    private void addItem() {
        addItem(ItemRegistries.NATURE_GIFT, "Nature Gift","大自然的礼物");
        addItemAndTooltip(ItemRegistries.MANA_FLOWER, "Mana Flower","魔力花","When you need mana, automatically use the mana potion in your backpack","当你需要法力时，自动使用你背包的法力药水");
        addItemAndTooltip(ItemRegistries.MAGNET_FLOWER, "Magnet Flower","奥术花","When you need mana, automatically use the mana potion in your backpack","当你需要法力时，自动使用你背包的法力药水");
    }

    private void addItemAndTooltip(Supplier<? extends Item> item,String enName, String zhName,String enTooltip, String zhTooltip) {
        String key = "tooltip." + item.get().getDescriptionId();
        this.add(key,enTooltip, zhTooltip);
        this.addItem(item, enName, zhName);
    }

    private void addItemAndTooltips(Supplier<? extends Item> item, String enName, String zhName, List<String> enTooltips,  List<String> zhTooltips) {
        addItem(item, enName, zhName);
        String itemKey = item.get().getDescriptionId();
        addTooltips(itemKey, enTooltips, zhTooltips);
    }

    private void addTooltips(String itemKey, List<String> enTooltips, List<String> zhTooltips) {
        for (int i = 0; i < enTooltips.size(); i++) {
            String tooltipKey = "tooltip." + itemKey + i;
            add(tooltipKey, enTooltips.get(i), zhTooltips.get(i));
        }
    }

    private void addItem(Supplier<? extends Item> item, String enName, String zhName) {
        String key = item.get().getDescriptionId();
        add(key, enName, zhName);
    }

    private void add(String item, String en, String zh) {
        if (this.locale.equals("en_us")) {
            add(item, en);
        }
        else if (this.locale.equals("zh_cn")) {
            add(item, zh);
        }
    }
}
