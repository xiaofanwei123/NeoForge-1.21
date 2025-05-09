package com.xiaofanwei.xfws_someitems.data;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.items.curios.CurioItem;
import com.xiaofanwei.xfws_someitems.registries.XItemRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import org.jetbrains.annotations.NotNull;

@JeiPlugin
public final class ModJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = MoreAC.Resource("jei_plugin");

    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        XItemRegistry.COMMONCURIOS.getEntries().forEach(entry -> {
            Item item = entry.get();
            if (item instanceof CurioItem curioItem && curioItem.enableJeiTooltip()) {
                String key = "jei.tooltip." + curioItem.getDescriptionId();
                registration.addItemStackInfo(item.getDefaultInstance(), Component.translatable(key)
                );
            }
        });
    }
}


