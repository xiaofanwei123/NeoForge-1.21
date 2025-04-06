package com.xiaofanwei.xfws_someitems.event;


import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import com.xiaofanwei.xfws_someitems.util.CuriosUtils;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber
public class ServerPlayerEvents {

    @SubscribeEvent
    public static void MagicCuffsRegenMana(LivingDamageEvent.Post event) {
        var livingEntity = event.getEntity();
        if(livingEntity instanceof ServerPlayer player && CuriosUtils.isPresence(player, ItemRegistries.MAGIC_CUFFS.get())){
            float amount = event.getNewDamage();
            CuriosUtils.getMagicData(player).addMana(10*amount);
        }
    }
}
