package com.xiaofanwei.xfws_someitems;

import net.neoforged.neoforge.common.ModConfigSpec;

public class XServerConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    //新增的数值配置
    public static final ModConfigSpec.IntValue MAXIMUM_DISTANCE;
    public static final ModConfigSpec.IntValue BRIGHTNESS;
    //public static final ModConfigSpec.BooleanValue ENABLE_FEATURE;

    public static int maximumDistance;
    public static int brightness;

    public static void onConfigReload() {
        MoreAC.LOGGER.debug("ServerConfigs load item blacklists:");

        //缓存数值配置
        maximumDistance = MAXIMUM_DISTANCE.get();
        brightness = BRIGHTNESS.get();
        //enableFeature = ENABLE_FEATURE.get();

        MoreAC.LOGGER.debug("maximumDistance: {}", maximumDistance);
        MoreAC.LOGGER.debug("brightness: {}", brightness);
    }

    static {
        //武器配置
        BUILDER.push("武器,Weapon");

        BUILDER.pop();

        //饰品配置
        BUILDER.push("飘渺游灯Ethereal Lantern");
        MAXIMUM_DISTANCE = BUILDER
                .comment("飘渺游灯的最大放置距离(整数):默认:8, 最小:1, 最大: 50")
                .comment("Maximum placement distance of Ethereal Lantern(integer):Default:8, Minimum: 1, Maximum: 50")
                .defineInRange("Maximum Distance", 8, 1, 50);

        BRIGHTNESS = BUILDER
                .comment("飘渺游灯放置火把所需的光照强度(整数):默认:4, 最小:0, 最大:15")
                .comment("The light intensity required for placing a torch in Ethereal Lantern(integer):Default: 4, Minimum: 0, Maximum: 15")
                .defineInRange("Brightness", 4, 0, 15);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}