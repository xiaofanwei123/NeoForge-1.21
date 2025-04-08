package com.xiaofanwei.xfws_someitems.data;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.items.curios.CurioItem;
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
        addItemAndJeiInfo(ItemRegistries.NATURE_GIFT, "Nature Gift","大自然的礼物","Destruction of cave vines has a chance of obtaining","破坏洞穴藤蔓有概率获得");
        addItem(ItemRegistries.MANA_REGENERATION_BAND, "Mana Regeneration Band","魔力再生手环");
        addItem(ItemRegistries.ANCIENT_FOSSIL, "Ancient Fossil","远古化石");
        addItemAndJeiInfo(ItemRegistries.BAND_OF_STARPOWER, "Band of Starpower","星力手环", "You can find it in the Magician's Cabin","你可以在魔法师小屋找到");
        addItemAndTooltip(ItemRegistries.MANA_FLOWER, "Mana Flower","魔力花","When you need mana, automatically use the mana potion in your backpack","当你需要法力时，自动使用你背包的法力药水");
        addItemAndTooltip(ItemRegistries.MAGNET_FLOWER, "Magnet Flower","奥术花","When you need mana, automatically use the mana potion in your backpack","当你需要法力时，自动使用你背包的法力药水");
        addItemAndTooltip(ItemRegistries.MAGIC_CUFFS, "Magic Cuffs","魔法手铐","Restore 10 times the mana value of the damage received after injury","受伤后恢复受到伤害10倍的法力值");
        addItemAndTooltip(ItemRegistries.SCULK_KATANA, "§3Sculk Katana","§3幽匿太刀","Swing this sword and launch a sonic boom","空挥发射一道音爆");
        addItemAndTooltip(ItemRegistries.SCULK_MEGAPHONE, "Sculk Megaphone","幽匿扩音器","Increases the range and damage of the Sonic Boom of the Sculk Katana","增加幽匿太刀音爆的距离和伤害");
    }

    private void addItemAndJeiInfo(Supplier<? extends Item> item, String enName, String zhName, String enJeiInfo, String zhJeiInfo) {
        String key = ((Item)item.get()).getDescriptionId();
        this.addItem(item, enName, zhName);
        this.addJeiInfos(key, enJeiInfo, zhJeiInfo);
    }

    private void addJeiInfos(String key, String enJeiInfo, String zhJeiInfo) {
        String jeiInfo = "jei.tooltip." + key;
        this.add(jeiInfo, enJeiInfo, zhJeiInfo);
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
