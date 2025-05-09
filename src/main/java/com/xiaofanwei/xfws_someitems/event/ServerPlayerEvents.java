package com.xiaofanwei.xfws_someitems.event;

import com.xiaofanwei.xfws_someitems.entity.ManaStar;
import com.xiaofanwei.xfws_someitems.items.sword.MessageSwingArm;
import com.xiaofanwei.xfws_someitems.registries.XEntityRegistry;
import com.xiaofanwei.xfws_someitems.registries.XItemRegistry;
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
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;


@EventBusSubscriber
public class ServerPlayerEvents {


    //左键事件,武器左键
    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide) {
            PacketDistributor.sendToServer(new MessageSwingArm());
        }
    }

//    @SubscribeEvent
//    public static void rightClick(PlayerInteractEvent.RightClickItem event) {
//        Player player = event.getEntity();
//        if(player instanceof AbstractClientPlayer clientPlayer){
//            System.out.println("rightClick");
//            XUtils.playAnimation(clientPlayer, "one_handed_stab");
//        }
//
//    }


//    //TODO：写合成表数据包用到，build前注释
//    @SubscribeEvent
//    public static void rightClick(PlayerInteractEvent.RightClickItem event) {
//        Player player = event.getEntity();
//        if (player instanceof ServerPlayer serverPlayer) {
//            var stack = player.getMainHandItem();
//            var holder = stack.getItemHolder();
//            var itemTags = holder.tags().toList();
//            var itemRegistry = serverPlayer.server.registryAccess().registry(Registries.ITEM).orElseThrow();
//
//            player.sendSystemMessage(copy(stack.toString(), ChatFormatting.GREEN, "Item ID"));
//            for (var tag : itemTags) {
//                var id = "'#%s'".formatted(tag.location());
//                var size = itemRegistry.getTag(tag).map(HolderSet::size).orElse(0);
//                player.sendSystemMessage(copy(id, ChatFormatting.YELLOW, "Item Tag [" + size + " items]"));
//            }
//        }
//    }
//
//    private static Component copy(String s, ChatFormatting col, String info) {
//        return copy(Component.literal(s).withStyle(col), Component.literal(info));
//    }
//
//    private static Component copy(Component c, Component info) {
//        return Component.literal("- ")
//                .withStyle(ChatFormatting.GRAY)
//                .withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, c.getString())))
//                .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, info.copy().append(" (Click to copy)"))))
//                .append(c);
//    }


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
