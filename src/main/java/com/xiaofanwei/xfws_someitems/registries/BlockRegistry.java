package com.xiaofanwei.xfws_someitems.registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
public class BlockRegistry {
    private static final DeferredRegister.Blocks BLOCKS =DeferredRegister.createBlocks("xfws_someitems");
    public static final DeferredBlock<Block> EXAMPLE_BLOCK;

    public BlockRegistry() {
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    static {
        EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    }
}
