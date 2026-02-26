package com.xiaofanwei.xfws_someitems;

import com.xiaofanwei.xfws_someitems.registries.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

//这里的值应该与 META-INF/neoforge.mods.toml 文件中的条目匹配
@Mod(MoreAC.MODID)
public class MoreAC
{
    public static final String MODID = "xfws_someitems";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MoreAC(IEventBus modEventBus, ModContainer modContainer)
    {
        XParticleRegistry.register(modEventBus);
        XItemRegistry.register(modEventBus);
        XCreativeTabRegistry.register(modEventBus);
        XMobEffectRegistry.register(modEventBus);
        XAttributeRegistry.register(modEventBus);
        XEntityRegistry.register(modEventBus);
        DataComponents.register(modEventBus);
        XSoundEvents.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.SERVER, XServerConfigs.SPEC, String.format("%s-server.toml", MODID));

    }

    public static ResourceLocation Resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
