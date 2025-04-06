package com.xiaofanwei.xfws_someitems.data;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MoreAC.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ItemRegistries.CURIOS.getEntries().forEach((item) -> {
            try {
                String path = item.getId().getPath().toLowerCase();
                ((ItemModelBuilder)this.withExistingParent(path, "item/generated")).texture("layer0", MoreAC.Resource("item/" + path));
            } catch (Exception var3) {
                Exception e = var3;
                MoreAC.LOGGER.error(e.getMessage());
            }

        });
    }
}
