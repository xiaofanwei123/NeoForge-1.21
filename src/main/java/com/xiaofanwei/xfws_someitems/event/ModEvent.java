package com.xiaofanwei.xfws_someitems.event;

import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import com.xiaofanwei.xfws_someitems.util.CuriosUtils;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.neoforged.neoforge.common.NeoForge;


public class ModEvent {
    public static void register() {
        NeoForge.EVENT_BUS.addListener(ModEvent::magicflower);
    }

    private static void magicflower(SpellSelectionManager.SpellSelectionEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(CuriosUtils.isPresence(event.getEntity(), ItemRegistries.MANA_FLOWER.get())
        || CuriosUtils.isPresence(event.getEntity(), ItemRegistries.MAGNET_FLOWER.get()))) return;
        if (event.getEntity() instanceof Player && event.getManager().getSelection() != null) {
            int spelllevel = event.getManager().getSelection().spellData.getLevel();
            int costmana = event.getManager().getSelection().spellData.getSpell().getManaCost(spelllevel);
            double maxmana = event.getEntity().getAttributeValue(AttributeRegistry.MAX_MANA);
            if (costmana > maxmana) return;
            double leftmana = CuriosUtils.getMagicData(event.getEntity()).getMana();
            if (costmana > leftmana && leftmana != 0) {
                for (ItemStack itemstack : event.getEntity().getInventory().items) {
                    if (itemstack.getItem() instanceof PotionItem) {
                        if(JudgePotion(itemstack, event.getEntity(), maxmana)) return;
                    }
                }
            }
        }
    }

    private static boolean JudgePotion(ItemStack itemstack, Player player,double maxmana) {
        if(itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_FOUR)){
            CostPotion(itemstack, player, 100,0.2,maxmana);
            return true;
        }
        if(itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_THREE)){
            CostPotion(itemstack, player, 75,0.15,maxmana);
            return true;
        }
        if(itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_TWO)){
            CostPotion(itemstack, player, 50,0.1,maxmana);
            return true;
        }
        if(itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_ONE)){
            CostPotion(itemstack, player, 25,0.05,maxmana);
            return true;
        }
        return false;
    }

    private static void CostPotion(ItemStack itemstack, Player player, int parameter1,double parameter2,double maxmana) {
        itemstack.shrink(1);
        player.addItem(new ItemStack(Items.GLASS_BOTTLE));
        CuriosUtils.getMagicData(player).addMana((float) (parameter1+maxmana*parameter2));
    }
}

//                        if (itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_FOUR)){
//                            itemstack.shrink(1);
//                            event.getEntity().addItem(new ItemStack(Items.GLASS_BOTTLE));
//                            CuriosUtils.getMagicData(event.getEntity()).addMana((float) (100+maxmana*0.2));
//                            //event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 4));
//                            return;
//                        }
//                        if (itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_THREE)){
//                            itemstack.shrink(1);
//                            event.getEntity().addItem(new ItemStack(Items.GLASS_BOTTLE));
//                            CuriosUtils.getMagicData(event.getEntity()).addMana((float) (75+maxmana*0.15));
//                            //event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 3));
//                            return;
//                        }
//                        if (itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_TWO)){
//                            itemstack.shrink(1);
//                            event.getEntity().addItem(new ItemStack(Items.GLASS_BOTTLE));
//                            CuriosUtils.getMagicData(event.getEntity()).addMana((float) (50+maxmana*0.1));
//                            // event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 2));
//                            return;
//                        }
//                        if (itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_ONE)) {
//                            itemstack.shrink(1);
//                            event.getEntity().addItem(new ItemStack(Items.GLASS_BOTTLE));
//                            CuriosUtils.getMagicData(event.getEntity()).addMana((float) (25+maxmana*0.05));
//                            //event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 1));
//                            return;
//                        }
