package com.xiaofanwei.xfws_someitems.items.sword;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import com.xiaofanwei.xfws_someitems.util.CuriosUtils;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = MoreAC.MODID)
public class Sculk_Katana extends SwordItem {

    public Sculk_Katana(Tier tier, int rawDamage, float rawSpeed) {
        super(tier, new Item.Properties().fireResistant().durability(tier.getUses())
                .component(DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(tier,rawDamage, rawSpeed + tier.getSpeed()))
        );
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide) {
            PacketDistributor.sendToServer(new MessageSwingArm());
        }
    }

    //玩家攻击后的事件
    @SubscribeEvent
    public static void onPlayerAttack(LivingDamageEvent.Pre event){
        if (event.getSource().getEntity() instanceof Player player && player.getMainHandItem().getItem() == ItemRegistries.SCULK_KATANA.get() && player.swingTime == 0 && !event.getSource().is(DamageTypes.SONIC_BOOM)) {
            double damage=player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            onLeftClick(player,0.5*damage);
        }
    }


    public static void onLeftClick(final Player player,double damage) {
        float range = 8;
        if(CuriosUtils.isPresence(player, ItemRegistries.SCULK_MEGAPHONE.get())) {
            range = 10;
            damage*=1.2;
        }
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getForward().scale((double)range));
        AABB boundingBox = player.getBoundingBox().expandTowards(end.subtract(start));
        List<? extends Entity> entities = player.level().getEntities(player, boundingBox);
        Iterator var11 = entities.iterator();
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, player.getSoundSource(),0.5F,1F);
        while(var11.hasNext()) {
            Entity target = (Entity)var11.next();
            HitResult hit = Utils.checkEntityIntersecting(target, start, end, 0.4F);
            if (hit.getType() != HitResult.Type.MISS && !(target instanceof Player) && target instanceof LivingEntity) {
                target.invulnerableTime= 0;
                target.hurt(player.damageSources().sonicBoom(player), (float) damage);
            }
        }
        Vec3 vec3 = player.getLookAngle().normalize();
        for(int i = 2; (float)i < range; i+=1) {
            Vec3 vec32 = vec3.scale((double)i).add(player.getEyePosition());
            Level level= player.level();
            ((ServerLevel)level).sendParticles(ParticleTypes.SONIC_BOOM, vec32.x, vec32.y, vec32.z, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.sculk_katana"));
    }
}
