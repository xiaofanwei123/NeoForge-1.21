package com.xiaofanwei.xfws_someitems.util;

import com.xiaofanwei.xfws_someitems.MoreAC;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;

import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.setup.IronsAdjustmentModifier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.*;

import static io.redspace.ironsspellbooks.config.ClientConfigs.SHOW_FIRST_PERSON_ARMS;
import static io.redspace.ironsspellbooks.config.ClientConfigs.SHOW_FIRST_PERSON_ITEMS;

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
            if ( !entity1.equals(entity) && !entity.isAlliedTo(entity1) && !entity1.isAlliedTo(entity) && entity1 instanceof Mob /*&& entity.hasLineOfSight(entity1)*/) {
                if (closestValid == null || entity1.distanceToSqr(at) < closestValid.distanceToSqr(at)) {
                    closestValid = entity1;
                }
            }
        }
        return closestValid ;
    }

    /**
     * 播放动画
     * */
    public static void playAnimation(AbstractClientPlayer clientPlayer, String animationFileName) {
        var rawanimation = PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(MoreAC.MODID, animationFileName));
        if (rawanimation instanceof KeyframeAnimation keyframeAnimation) {
            var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(SpellAnimations.ANIMATION_RESOURCE);
            if (playerAnimationData != null) {
                var animation = new KeyframeAnimationPlayer(keyframeAnimation) {
                    @Override
                    public void tick() {
                        if (getCurrentTick() == getStopTick() - 2) {
                            IronsAdjustmentModifier.INSTANCE.fadeOut(3);
                        }
                        super.tick();
                    }
                };
                var armsFlag = SHOW_FIRST_PERSON_ARMS.get();
                var itemsFlag = SHOW_FIRST_PERSON_ITEMS.get();
                if (armsFlag || itemsFlag) {
                    animation.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                    animation.setFirstPersonConfiguration(new FirstPersonConfiguration(armsFlag, armsFlag, itemsFlag, itemsFlag));
                } else {
                    animation.setFirstPersonMode(FirstPersonMode.DISABLED);
                }
                playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(2, Ease.INOUTSINE), animation, true);
            }
        }
    }
}
