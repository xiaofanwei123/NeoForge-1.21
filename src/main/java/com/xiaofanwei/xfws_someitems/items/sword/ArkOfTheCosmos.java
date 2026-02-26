package com.xiaofanwei.xfws_someitems.items.sword;

import com.xiaofanwei.xfws_someitems.entity.RMagicMissileProjectile;
import com.xiaofanwei.xfws_someitems.registries.*;
import com.xiaofanwei.xfws_someitems.util.PlayAnimationTrigger;
import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ArkOfTheCosmos extends SwordItem implements OnLeftClick {
    public ArkOfTheCosmos(Tier tier, int rawDamage, float rawSpeed) {
        super(tier, new Item.Properties().rarity(XRarity.RAINBOW.getValue()).fireResistant()
                .component(net.minecraft.core.component.DataComponents.UNBREAKABLE,new Unbreakable(false))//不可破坏
                .component(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(tier,rawDamage, rawSpeed + tier.getSpeed()))
                .component(DataComponents.COUNT, 0)
                .component(DataComponents.COOLDOWN, 0F)
        );
    }

    //彩虹耐久条
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        //每20tick更新
        if(!level.isClientSide() && entity instanceof Player player && player.tickCount % 20 == 0){
            if(getCooldown(stack) > 0){
                stack.set(DataComponents.COOLDOWN, getCooldown(stack) - 1F);
                if(getCooldown(stack) == 0){
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), XSoundEvents.JINGLE_BELLS.get(), player.getSoundSource(), 2F, 1F);
                }
            }
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getCooldown(stack)<=7 && getCooldown(stack) != 0.0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) (13 - 13 * getCooldown(stack) / 7);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Color.HSBtoRGB((System.currentTimeMillis() % 5000) / 5000F, 1f, 1F);
    }

    private static int getPower(ItemStack stack) {
        return stack.getOrDefault(DataComponents.COUNT,0);//getOrDefault
    }

    private static void setPower(ItemStack stack, int count) {
        stack.set(DataComponents.COUNT, count);
    }

    private static float getCooldown(ItemStack stack) {
            return stack.getOrDefault(DataComponents.COOLDOWN,0F);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(getCooldown(player.getMainHandItem()) == 0){//冷却为0
            player.getMainHandItem().set(DataComponents.COOLDOWN, player.isCreative() ? 0F : 7F);
            if(player.level().isClientSide && player instanceof AbstractClientPlayer clientPlayer){
                PlayAnimationTrigger.playAnimation(clientPlayer, "parry");
            }
            if(!level.isClientSide()){
                Vec3 normalize = player.getLookAngle().normalize();
                Vec3 start = normalize.scale(-0.8).add(player.getEyePosition());
                Vec3 end = start.add(player.getForward().scale(2.5));
                AABB boundingBox = player.getBoundingBox().expandTowards(end.subtract(start));
                List<Entity> entities = new ArrayList<>();
                entities.addAll(level.getEntitiesOfClass(
                        LivingEntity.class,
                        boundingBox,
                        l -> l != player && l.isAlive()//格挡宠物？
                ));
                entities.addAll(level.getEntitiesOfClass(
                        Projectile.class,
                        boundingBox,
                        p -> p.getOwner() == null || !(p.getOwner() instanceof Player)
                ));
                Iterator<? extends Entity> var11 = entities.iterator();
                if (var11.hasNext()){//有实体
                    for (Entity target : entities) {
                        HitResult hit = Utils.checkEntityIntersecting(target, start, end, 1F);
                        if (hit.getType() != HitResult.Type.MISS && !(target instanceof Player)) {
                            if(target instanceof Projectile projectile && !(projectile.getOwner() instanceof Player)){
                                projectile.discard();//格挡弹射物
                            }
                            else if(target instanceof LivingEntity){
                                target.hurt(player.damageSources().playerAttack(player), (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE));
                            }
                            if(player instanceof ServerPlayer serverPlayer){
                                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("tooltip.item.xfws_someitems.ark_of_the_cosmos.3").withStyle(style  -> style.withColor(Color.HSBtoRGB(System.currentTimeMillis() % 5000F / 5000F,1f, 1F)))));
                            }
                        }
                    }
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50, 4, false, false));
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), XSoundEvents.SCISSOR_GUILLOTINE_SNAP.get(), player.getSoundSource(), 1.7F, 1F);
                    Vec3 vec3 = player.getLookAngle().normalize().scale(0.5).add(player.getEyePosition());
                    ((ServerLevel)player.level()).sendParticles(XParticleRegistry.PARRY.get(), vec3.x, vec3.y, vec3.z, 1, 0, 0, 0, 0);
                    setPower(player.getMainHandItem(), 15);
                }
                else {//没有实体
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), XSoundEvents.RAZORBLADE_TYPHOON.get(), player.getSoundSource(), 1F, 1F);
                }
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    //玩家攻击后的事件
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof ServerPlayer player
            && !player.getCooldowns().isOnCooldown(XItemRegistry.ARK_OF_THE_COSMOS.get())){
            addComet(player);
        }
    }

    public void onLeftClick(final Player player) {
        if(player.getCooldowns().isOnCooldown(XItemRegistry.ARK_OF_THE_COSMOS.get())) return;
        addComet(player);
    }

    private static void addComet(Player player){
        for (int i = 0; i < 3; i++) {
            Vec3 position = player.position().add(0,player.getBbHeight()/2,0);
            RMagicMissileProjectile comet = new RMagicMissileProjectile(player.level(), player, position);
            comet.setDamage((float)player.getAttributeValue(Attributes.ATTACK_DAMAGE));
            comet.setGlowingTag(true);
            Vec3 spawnPos = position.add(
                    2*(Math.random() - 0.5),
                    2*(Math.random()),
                    2*(Math.random() - 0.5));
            comet.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            player.level().addFreshEntity(comet);
            Vec3 direction = comet.position().subtract(position).normalize();
            Vec3 motion = direction.scale(comet.getBaseSpeed());
            comet.setGlowingTag(true);
            comet.setDeltaMovement(motion);
        }
        player.getCooldowns().addCooldown(XItemRegistry.ARK_OF_THE_COSMOS.get(), XUtils.getAttackSpeed(player));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.ark_of_the_cosmos.0").withStyle(style  -> style.withColor(Color.HSBtoRGB((System.currentTimeMillis() % 10000) / 10000F, 1f, 1F))));
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.ark_of_the_cosmos.1").withStyle(style  -> style.withColor(Color.HSBtoRGB((System.currentTimeMillis() % 10000) / 10000F, 1f, 1F))));
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.ark_of_the_cosmos.2").withStyle(style  -> style.withColor(Color.HSBtoRGB((System.currentTimeMillis() % 10000) / 10000F, 1f, 1F))));
    }
}
