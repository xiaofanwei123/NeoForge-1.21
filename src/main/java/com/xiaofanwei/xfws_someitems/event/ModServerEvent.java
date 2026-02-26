package com.xiaofanwei.xfws_someitems.event;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.XServerConfigs;
import com.xiaofanwei.xfws_someitems.entity.ManaStar;
import com.xiaofanwei.xfws_someitems.items.sword.MessageSwingArm;
import com.xiaofanwei.xfws_someitems.registries.XAttributeRegistry;
import com.xiaofanwei.xfws_someitems.registries.XEntityRegistry;
import com.xiaofanwei.xfws_someitems.registries.XItemRegistry;
import com.xiaofanwei.xfws_someitems.registries.XMobEffectRegistry;
import com.xiaofanwei.xfws_someitems.util.CuriosUtils;
import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MoreAC.MODID)
public class ModServerEvent {

    @SubscribeEvent
    private static void magicflower(SpellSelectionManager.SpellSelectionEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!CuriosUtils.isPresence(event.getEntity(),
                XItemRegistry.MAGNET_FLOWER.get(),
                XItemRegistry.ARCANE_FLOWER.get(),
                XItemRegistry.MANA_FLOWER.get()
        )) return;

        if (event.getEntity() instanceof Player && event.getManager().getSelection() != null) {
            int spelllevel = event.getManager().getSelection().spellData.getLevel();
            int costmana = event.getManager().getSelection().spellData.getSpell().getManaCost(spelllevel);
            double maxmana = event.getEntity().getAttributeValue(AttributeRegistry.MAX_MANA);
            if (costmana > maxmana) return;
            double leftmana = XUtils.getPresentMana(event.getEntity());
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
        PotionContents potionContents = itemstack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if(potionContents.is(PotionRegistry.INSTANT_MANA_FOUR)){
            CostPotion(itemstack, player, 100,maxmana);
            return true;
        }
        if(potionContents.is(PotionRegistry.INSTANT_MANA_THREE)){
            CostPotion(itemstack, player, 75,maxmana);
            return true;
        }
        if(potionContents.is(PotionRegistry.INSTANT_MANA_TWO)){
            CostPotion(itemstack, player, 50,maxmana);
            return true;
        }
        if(potionContents.is(PotionRegistry.INSTANT_MANA_ONE)){
            CostPotion(itemstack, player, 25,maxmana);
            return true;
        }
        return false;
    }

    private static void CostPotion(ItemStack itemstack, Player player, int parameter,double maxmana) {
        itemstack.shrink(1);
        player.addItem(new ItemStack(Items.GLASS_BOTTLE));
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, player.getSoundSource(), 2.0F, 1.0F);
        XUtils.addMana(player,(float) (parameter+maxmana*parameter/500));
        player.addEffect(new MobEffectInstance(XMobEffectRegistry.MANA_SiCKNESS, 8*20, (parameter/25)-1, true, true));
    }


    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(MessageSwingArm.TYPE, MessageSwingArm.STREAM_CODEC, MessageSwingArm::handle);
    }

    @SubscribeEvent
    public static void onModConfigLoadingEvent(ModConfigEvent.Loading event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER && MoreAC.MODID.equals(event.getConfig().getModId())) {
            XServerConfigs.onConfigReload();
        }
    }

    //左键事件,武器左键
    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide) {
            PacketDistributor.sendToServer(new MessageSwingArm());
        }
    }

    @SubscribeEvent
    public static void MagicCuffsRegenMana(LivingDamageEvent.Post event) {
        var livingEntity = event.getEntity();
        if(livingEntity.level().isClientSide()) return;
        if(livingEntity instanceof ServerPlayer player && CuriosUtils.isPresence(player, XItemRegistry.MAGIC_CUFFS.get(), XItemRegistry.CELESTIAL_CUFFS.get())){
            float amount = event.getNewDamage();
            XUtils.addMana(livingEntity,10*amount);
        }
    }

    //法术击杀实体掉落星星
    @SubscribeEvent
    public static void ManaStar(LivingDeathEvent event) {
        var entity = event.getEntity();
        if(entity.level().isClientSide()) return;
        if(event.getSource() instanceof SpellDamageSource && event.getSource().getEntity() instanceof ServerPlayer player){
            Level level= entity.level();
            if(Math.random()>0.5F){
                var star=new ManaStar(XEntityRegistry.MANA_STAR.get(), level);
                star.setPos(entity.position().add(0, entity.getBbHeight() / 2F, 0));
                star.setMana(25.0F);
                star.setMaxDistance(player.getAttributeValue(XAttributeRegistry.MANASTAR_DISTANCE));
                level.addFreshEntity(star);
            }
        }
    }
}

