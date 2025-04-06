package com.xiaofanwei.xfws_someitems.event;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import com.xiaofanwei.xfws_someitems.registries.MobEffectRegistry;
import com.xiaofanwei.xfws_someitems.util.CuriosUtils;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = MoreAC.MODID)
public class GameEvent {

    @SubscribeEvent
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
            CostPotion(itemstack, player, 100,maxmana);
            return true;
        }
        if(itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_THREE)){
            CostPotion(itemstack, player, 75,maxmana);
            return true;
        }
        if(itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_TWO)){
            CostPotion(itemstack, player, 50,maxmana);
            return true;
        }
        if(itemstack.get(DataComponents.POTION_CONTENTS).is(PotionRegistry.INSTANT_MANA_ONE)){
            CostPotion(itemstack, player, 25,maxmana);
            return true;
        }
        return false;
    }

    private static void CostPotion(ItemStack itemstack, Player player, int parameter,double maxmana) {
        itemstack.shrink(1);
        player.addItem(new ItemStack(Items.GLASS_BOTTLE));
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, player.getSoundSource(), 2.0F, 1.0F);
        CuriosUtils.getMagicData(player).addMana((float) (parameter+maxmana*parameter/500));
        player.addEffect(new MobEffectInstance(MobEffectRegistry.MANA_SiCKNESS, 8*20, (parameter/25)-1, true, true));
    }



}

