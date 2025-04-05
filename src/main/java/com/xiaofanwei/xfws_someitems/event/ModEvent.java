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
                        if (itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_ONE)) {
                            itemstack.shrink(1);
                            event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 1));
                            return;
                        }
                        if (itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_TWO)){
                            itemstack.shrink(1);
                            event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 2));
                            return;
                        }
                        if (itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_THREE)){
                            itemstack.shrink(1);
                            event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 3));
                            return;
                        }
                        if (itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_FOUR)){
                            itemstack.shrink(1);
                            event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 4));
                            return;
                        }
                    }
                }
            }
        }
    }
}

//                if(event.getEntity().getInventory().hasAnyMatching(stack -> stack.is(Items.POTION) && stack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_ONE))){
//
//                }

//                for (int i = 0; i < (event.getEntity()).getInventory().getContainerSize(); i++) {

//event.getEntity().getInventory().setItem(i, new ItemStack(Items.GLASS_BOTTLE));
//                for (ItemStack stack : event.getEntity().getInventory().items){
//                    if(stack.getItem() instanceof PotionItem potionItem){
//
//                    }
//                }
