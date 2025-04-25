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
        add("effect.xfws_someitems.mana_sickness", "Mana Sickness","魔力病");
        add("xfws_someitems.creativemodetab","xfw's some items","小烦维的物品");
        add("attribute.xfws_someitems.manastar_distance","Mana Star Pick Up Distance","魔力星星拾取距离");
        //ai生成，请勿较真，如有错误，及时提出
        add("message.xfws_someitems.the_mirror_of_death_gaze.-1","You have never died","汝未尝一死");
        add("message.xfws_someitems.the_mirror_of_death_gaze.0","After experiencing death and life, you return to the human world. What will you do next?","君历死生，得返人间，将继之乎？");
        add("message.xfws_someitems.the_mirror_of_death_gaze.1","Yin and Yang are orderly, and you are strong in the way of defying heaven.","阴阳有序，汝强逆天道，纵得偿所愿，亦难逃业火焚身！");
        add("message.xfws_someitems.the_mirror_of_death_gaze.2","The resentment has been repaid, and the soul returns to Jiuquan. Why suffer from the calamity of dust again?","怨毒已偿，魂归九泉，何苦再涉尘劫？");
        add("message.xfws_someitems.the_mirror_of_death_gaze.3","How can the flag be lowered if the blood debt is not paid off? I have a secret method to help you eliminate all enemies!","血债未偿，岂可偃旗？吾有秘法，助汝戮尽仇雠!");
        add("message.xfws_someitems.the_mirror_of_death_gaze.4","Do you seek rebirth for revenge?","汝求重生，为复仇耶？");
    }

    private void addItem() {
        addItemAndJeiInfo(ItemRegistries.NATURE_GIFT, "Nature Gift","大自然的礼物","Destruction of cave vines has a chance of obtaining","破坏洞穴藤蔓有概率获得");
        addItem(ItemRegistries.MANA_REGENERATION_BAND, "Mana Regeneration Band","魔力再生手环");
        addItem(ItemRegistries.ANCIENT_FOSSIL, "Ancient Fossil","远古化石");
        addItemAndJeiInfo(ItemRegistries.BAND_OF_STARPOWER, "Band of Starpower","星力手环", "You can find it in the Magician's Cabin","你可以在魔法师小屋找到");
        addItemAndTooltip(ItemRegistries.MANA_FLOWER, "Mana Flower","魔力花","When you need mana, automatically use the mana potion in your backpack","当你需要法力时，自动使用你背包的法力药水");
        addItemAndTooltip(ItemRegistries.MAGNET_FLOWER, "Magnet Flower","奥术花","When you need mana, automatically use the mana potion in your backpack.Greatly increasing the pickup range for Stars","当你需要法力时，自动使用你背包的法力药水。大大增加星星的拾取范围");
        addItemAndTooltip(ItemRegistries.MAGIC_CUFFS, "Magic Cuffs","魔法手铐","Restore 10 times the mana value of the damage received after injury","受伤后恢复受到伤害10倍的法力值");
        addItemAndTooltip(ItemRegistries.SCULK_KATANA, "§3Sculk Katana","§3幽匿太刀","Swing this sword and launch a sonic boom","挥动这把剑来发射一道音爆");
        addItemAndTooltip(ItemRegistries.SCULK_MEGAPHONE, "Sculk Megaphone","幽匿扩音器","Increases the range and damage of the Sonic Boom of the Sculk Katana","增加幽匿太刀音爆的距离和伤害");
        addItemAndTooltip(ItemRegistries.ARCANE_FLOWER, "Arcane Flower","奥术花","When you need mana, automatically use the mana potion in your backpack","当你需要法力时，自动使用你背包的法力药水");
        addItemAndTooltip(ItemRegistries.CELESTIAL_CUFFS, "Celestial Cuffs","天界手铐","Restore 10 times the mana value of the damage received after injury","受伤后恢复受到伤害10倍的法力值,增大魔力星星的拾取距离");
        addItemAndTooltipAndJeiInfo(ItemRegistries.CELESTIAL_MAGNET, "Celestial Magnet","天界磁石","Increase the picking distance of mana stars","增大魔力星星的拾取距离", "You can find it in the Magician's Cabin","你可以在魔法师小屋找到");
        addItemAndTooltip(ItemRegistries.THE_MIRROR_OF_DEATH_GAZE, "The Mirror Of Death Gaze","死亡凝视之镜","Staring into this mirror to return to the point of death","凝视这面镜子以返回死亡点");
        addItemAndTooltips(ItemRegistries.ETHEREAI_LANTERN, "Ethereal Lantern","飘渺游灯",
                new String[]{"Give it fuel, and it will light up the darkness for you with a torch when you put it on",
                        "Left click on the fuel button to consume it all, right click to consume it one by one"}
                ,new String[]{"给予其燃料，它便会在你穿上他的时候用火把为你点亮黑暗",
                       "用燃料左键以全部消耗，右键则逐个消耗"});
    }

    private void addItemAndTooltipAndJeiInfo(Supplier<? extends Item> item, String enName, String zhName,String TooltipenName, String TooltipzhName,String JeiInfoenName, String JeiInfozhName) {
        String key = item.get().getDescriptionId();
        this.addJeiInfos(key, JeiInfoenName, JeiInfozhName);
        this.addItemAndTooltip(item,enName, zhName, TooltipenName, TooltipzhName);
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

    private void addItemAndTooltips(Supplier<? extends Item> item, String enName, String zhName, String[] enTooltips,  String[] zhTooltips) {
        addItem(item, enName, zhName);
        String itemKey = item.get().getDescriptionId();
        addTooltips(itemKey, enTooltips, zhTooltips);
    }

    private void addTooltips(String itemKey, String[] enTooltips, String[] zhTooltips) {
        for (int i = 0; i < enTooltips.length; i++) {
            String tooltipKey = "tooltip." + itemKey + "." + i;
            add(tooltipKey, enTooltips[i], zhTooltips[i]);
        }
    }

    private void addItem(Supplier<? extends Item> item, String enName, String zhName) {
        String key = item.get().getDescriptionId();
        add(key, enName, zhName);
    }

    private void add(String key, String en, String zh) {
        if (this.locale.equals("en_us")) {
            add(key, en);
        }
        else if (this.locale.equals("zh_cn")) {
            add(key, zh);
        }
    }
}
