package com.xiaofanwei.xfws_someitems.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = "xfws_someitems")
public class DataGenerator {
    public DataGenerator() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        boolean client = event.includeClient();
        boolean server = event.includeServer();
        CompletableFuture<TagsProvider.TagLookup<Block>> emptyBlockTags = lookup.thenApply(provider -> blockTagKey -> Optional.empty());
        generator.addProvider(server, new ModItemTagsProvider(output, lookup, emptyBlockTags, helper));
        generator.addProvider(server, new Language(output, "en_us"));
        generator.addProvider(server, new Language(output, "zh_cn"));
        generator.addProvider(client, new ModItemModelProvider(output, helper));
    }
}
