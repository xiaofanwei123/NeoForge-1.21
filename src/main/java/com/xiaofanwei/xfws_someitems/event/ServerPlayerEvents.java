package com.xiaofanwei.xfws_someitems.event;


import com.xiaofanwei.xfws_someitems.entity.ManaStar;
import com.xiaofanwei.xfws_someitems.registries.EntityRegistry;
import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import com.xiaofanwei.xfws_someitems.registries.XAttributeRegistry;
import com.xiaofanwei.xfws_someitems.util.CuriosUtils;
import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber
public class ServerPlayerEvents {

//    @SubscribeEvent
//    public static void rightClick(PlayerInteractEvent.RightClickItem event) {
//        Player player = event.getEntity();
//        if (player instanceof ServerPlayer) {
//            System.out.println(event.getEntity().getMainHandItem().getDescriptionId());
//        }
//    }


    @SubscribeEvent
    public static void MagicCuffsRegenMana(LivingDamageEvent.Post event) {
        var livingEntity = event.getEntity();
        if(livingEntity.level().isClientSide()) return;
        if(livingEntity instanceof ServerPlayer player && CuriosUtils.isPresence(player, ItemRegistries.MAGIC_CUFFS.get(), ItemRegistries.CELESTIAL_CUFFS.get())){
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
                var star=new ManaStar(EntityRegistry.MANA_STAR.get(), level);
                star.setPos(entity.position().add(0, entity.getBbHeight() / 2F, 0));
                star.setMana(25.0F);
                star.setMaxDistance(player.getAttributeValue(XAttributeRegistry.MANASTAR_DISTANCE));
                level.addFreshEntity(star);
            }
        }
    }
}
