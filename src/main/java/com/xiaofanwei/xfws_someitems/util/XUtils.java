package com.xiaofanwei.xfws_someitems.util;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class XUtils {

    public static MagicData getMagicData(LivingEntity living){
        return MagicData.getPlayerMagicData(living);
    }

    public static float getPresentMana(LivingEntity living){
        return getMagicData(living).getMana();
    }

    public static double getMaxMana(LivingEntity living){
        return living.getAttributeValue(AttributeRegistry.MAX_MANA);
    }

    public static void addMana(LivingEntity living, float mana){
        getMagicData(living).addMana(mana);
        getMagicData(living).addMana(-2);
    }

    /**
     * 传送玩家(若维度相同进行相同维度传送)
     * */
    public static void teleportTo(Player player, ResourceLocation Dime, BlockPos pos) {
        if(player.level() instanceof ServerLevel serverLevel) {
            if (player.getVehicle() != null) {
                player.removeVehicle();
            }
            if (!player.level().dimension().location().equals(Dime)) {
                ServerLevel destination = serverLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Dime));
                if (destination != null && canTeleport(destination, pos)) {
                    teleportToDimension(player, destination, pos);
                }
            } else if (canTeleport(serverLevel, pos))
                    teleportPlayer(pos, player);
        }
    }

    private static void teleportToDimension(Player player, ServerLevel serverLevel, BlockPos pos) {
        player.changeDimension(new DimensionTransition(serverLevel, pos.above().getBottomCenter(), Vec3.ZERO,
                player.getYRot(), player.getXRot(), DimensionTransition.DO_NOTHING));
    }

    private static boolean canTeleport(Level level, BlockPos pos) {
        BlockPos above = pos.above();
        return level.isEmptyBlock(above) && level.isEmptyBlock(above.above());
    }

    private static void teleportPlayer(BlockPos pos, Player player) {
        player.teleportTo(pos.getX(), pos.getY()+0.5, pos.getZ());
    }


    /**
     * 寻找最近的实体
     * */
    public static Entity findNearestProjectile(Entity entity, double dist) {
        LivingEntity closestValid = null;
        Level level = entity.level();
        Vec3 entityEyes = entity.getEyePosition(1.0F);
        HitResult hitresult = level.clip(new ClipContext(entityEyes, entityEyes.add(entity.getLookAngle().scale(dist)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity));
        Vec3 at = hitresult.getLocation();
        AABB around = new AABB(at.add(-0.5F, -0.5F, -0.5F), at.add(0.5F, 0.5F, 0.5F)).inflate(15);
        for (LivingEntity entity1 : level.getEntitiesOfClass(LivingEntity.class, around.inflate(dist))) {
            if ( !entity1.equals(entity) && !entity.isAlliedTo(entity1) && !entity1.isAlliedTo(entity) && entity1 instanceof Mob) {
                if (closestValid == null || entity1.distanceToSqr(at) < closestValid.distanceToSqr(at)) {
                    closestValid = entity1;
                }
            }
        }
        return closestValid ;
    }


    //攻击速度和冷却的关系
    public static int getAttackSpeed(LivingEntity living){
        AttributeInstance attributeInstance = living.getAttribute(Attributes.ATTACK_SPEED);
        if (attributeInstance != null){
            double speed = attributeInstance.getValue();
            int time = (int) (20 / speed) - 1;
            return Math.max(0, time);
        }
        return 0;
    }

    //弹射物伤害规则
    public static boolean modifyDamage(Projectile projectile, Player player, LivingEntity target, float damageMultiplier){
        ItemStack weaponItem = player.getWeaponItem();
        Level level = player.level();
        DamageSource damageSource = projectile.damageSources().playerAttack(player);
        float damage = player.getAttribute(Attributes.ATTACK_DAMAGE) != null ? (float) (player.getAttributeValue(Attributes.ATTACK_DAMAGE)) : 1;
        damage = EnchantmentHelper.modifyDamage((ServerLevel) level, weaponItem, target, damageSource, damage);

        if (target.hurt(damageSource, damageMultiplier * damage)) {
            EnchantmentHelper.doPostAttackEffectsWithItemSource((ServerLevel) level, target, damageSource, weaponItem);
            return true;
        }
        return false;
    }
}
