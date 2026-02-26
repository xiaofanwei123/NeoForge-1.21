package com.xiaofanwei.xfws_someitems.entity;

import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.comet.Comet;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class RComet extends Comet {
    public RComet(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RComet(Level pLevel, LivingEntity pShooter) {
        super(pLevel, pShooter);
    }


    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide) {
            this.impactParticles(this.xOld, this.yOld, this.zOld);
            this.getImpactSound().ifPresent(this::doImpactSound);
            float explosionRadius = this.getExplosionRadius();
            List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().inflate((double)explosionRadius));

            for (Entity entity : entities) {
                if(entity instanceof LivingEntity livingEntity && !(entity instanceof Player)){
                    double distance = livingEntity.distanceToSqr(hitResult.getLocation());
                    if (distance < (double) (explosionRadius * explosionRadius) && this.canHitEntity(livingEntity)) {
                        if(this.getOwner()!=null){
                            boolean flag = XUtils.modifyDamage(this, (Player) this.getOwner(), livingEntity, 1);
                            if(flag){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 500,0,false,false));
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 60) {
            this.discard();
        }
    }



}
