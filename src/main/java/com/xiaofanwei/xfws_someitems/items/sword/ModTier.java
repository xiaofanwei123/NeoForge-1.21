package com.xiaofanwei.xfws_someitems.items.sword;


import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.SimpleTier;

import java.util.function.Supplier;

//TODO: 可能需要
public class ModTier extends SimpleTier {

    public ModTier(TagKey<Block> incorrectBlocksForDrops, int uses, float speed, float attackDamageBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        super(incorrectBlocksForDrops, uses, speed, attackDamageBonus, enchantmentValue, repairIngredient);
    }
    //public static final ModTier SCULK_KATANA = new ModTier(BlockTags.MINEABLE_WITH_PICKAXE, 200, -4f, -1f, 10, () -> Ingredient.of(Items.ECHO_SHARD));
    //public static final ModTier SCULK_KATANA = new SimpleTier(BlockTags.MINEABLE_WITH_PICKAXE, 200, -4f, -1f, 10, () -> Ingredient.of(Items.ECHO_SHARD));

}