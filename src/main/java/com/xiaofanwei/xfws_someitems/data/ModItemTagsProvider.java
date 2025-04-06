package com.xiaofanwei.xfws_someitems.data;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_curio.common.init.TCTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> b, @Nullable ExistingFileHelper helper) {
        super(output, provider, b, MoreAC.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> accessory = this.tag(TCTags.ACCESSORY);
        ItemRegistries.CURIOS.getEntries().forEach((item) -> {
            accessory.add((Item)item.get());
        });
    }
}
