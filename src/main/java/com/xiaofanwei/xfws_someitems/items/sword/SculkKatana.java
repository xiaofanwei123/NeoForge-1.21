package com.xiaofanwei.xfws_someitems.items.sword;

import com.xiaofanwei.xfws_someitems.registries.XItemRegistry;
import com.xiaofanwei.xfws_someitems.util.CuriosUtils;
import com.xiaofanwei.xfws_someitems.util.RayTraceResult;
import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.List;

public class SculkKatana extends SwordItem implements OnLeftClick {

    public SculkKatana(Tier tier, int rawDamage, float rawSpeed) {
        super(tier, new Item.Properties().fireResistant().durability(tier.getUses())
                .component(DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(tier,rawDamage, rawSpeed + tier.getSpeed()))
        );
    }

    //玩家攻击后的事件
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.getCooldowns().isOnCooldown(XItemRegistry.SCULK_KATANA.get())) {
            onLeftClick(player);
        }
    }

    public void onLeftClick(Player player) {
        if(player.getCooldowns().isOnCooldown(XItemRegistry.SCULK_KATANA.get())){
            return;
        }
        double damageAttr = 0.5*player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean wearingSculk = CuriosUtils.isPresence(player, XItemRegistry.SCULK_MEGAPHONE.get());
        float range = wearingSculk ? 8 : 10;
        double damage = wearingSculk ? 1.3 * damageAttr : damageAttr;
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getForward().scale(range));
        AABB boundingBox = player.getBoundingBox().expandTowards(end.subtract(start));
        List<? extends Entity> entities = player.level().getEntities(player, boundingBox);
        Iterator<? extends Entity> var11 = entities.iterator();
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, player.getSoundSource(),0.5F,1F);
        while(var11.hasNext()) {
            Entity target = var11.next();
            HitResult hit = Utils.checkEntityIntersecting(target, start, end, 0.4F);
            if (hit.getType() != HitResult.Type.MISS && !(target instanceof Player) && target instanceof LivingEntity) {
                target.hurt(player.damageSources().sonicBoom(player), (float) damage);
            }
        }
        Vec3 vec3 = player.getLookAngle().normalize();
        for(int i = 2; (float)i < range; i+=1) {
            Vec3 vec32 = vec3.scale(i).add(player.getEyePosition());
            Level level= player.level();
            ((ServerLevel)level).sendParticles(ParticleTypes.SONIC_BOOM, vec32.x, vec32.y, vec32.z, 1, 0, 0, 0, 0);
        }
        player.getCooldowns().addCooldown(XItemRegistry.SCULK_KATANA.get(), XUtils.getAttackSpeed(player));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(level.isClientSide()){
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));
        }
        if (player.getMainHandItem().getItem() == XItemRegistry.SCULK_KATANA.get()) {
            RayTraceResult rayTraceResult = RayTraceResult.rayTrace(player, 16, false);
            if(rayTraceResult.isEntityHit() && rayTraceResult.getEntity() instanceof LivingEntity target&& target.isAlive()){
                double distance = player.distanceTo(target);
                double damage= player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                double health = target.getHealth();
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(),1F,1F);
                target.hurt(player.damageSources().sonicBoom(player), 1.5F * (float) damage);
                if(damage<health) {
                    player.getCooldowns().addCooldown(XItemRegistry.SCULK_KATANA.get(), 20 * 5);
                }
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20*3, 2, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20*3, 1, false, false));
                for(float i = 0; i < distance; i+=0.2f) {
                    Vec3 vec3 = player.getLookAngle().normalize().scale(i).add(player.getEyePosition());
                    ((ServerLevel)level).sendParticles(ParticleTypes.SCULK_SOUL.getType(), vec3.x, vec3.y, vec3.z, 1, 0, 0, 0, 0.2);
                }
                XUtils.teleportTo(player, player.level().dimension().location(), target.blockPosition());
                player.swingingArm = player.getUsedItemHand();
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.sculk_katana.0").withStyle(ChatFormatting.AQUA));
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.sculk_katana.1").withStyle(ChatFormatting.AQUA));
    }
}
