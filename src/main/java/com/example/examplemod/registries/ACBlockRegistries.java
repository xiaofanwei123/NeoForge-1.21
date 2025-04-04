package com.example.examplemod.registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
public class ACBlockRegistries {
    private static final DeferredRegister.Blocks BLOCKS =DeferredRegister.createBlocks("xfws_more_ac");
    public static final DeferredBlock<Block> EXAMPLE_BLOCK;

    public ACBlockRegistries() {
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    static {
        EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    }
}
