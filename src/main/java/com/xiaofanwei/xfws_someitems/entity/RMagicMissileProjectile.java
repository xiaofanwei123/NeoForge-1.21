package com.xiaofanwei.xfws_someitems.entity;

import com.xiaofanwei.xfws_someitems.registries.XSoundEvents;
import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RMagicMissileProjectile extends MagicMissileProjectile {
    private static final double BASE_SPEED = 0.60;
    @Nullable
    private LivingEntity target;
    private Vec3 centerPos;

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public void setTargetPos(Vec3 centerPos) {
        this.centerPos = centerPos;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        entityHitResult.getEntity().invulnerableTime=0;
        entityHitResult.getEntity().hurt(this.getOwner().damageSources().mobProjectile( this,(LivingEntity)this.getOwner()), this.getDamage());
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), XSoundEvents.CRYSTAL_IMPACT.get(), SoundSource.NEUTRAL, 1F, 1F);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult){
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), XSoundEvents.CRYSTAL_IMPACT.get(), SoundSource.NEUTRAL, 1F, 1F);
        super.onHitBlock(blockHitResult);
    }

    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        if (!this.level().isClientSide) {
            this.impactParticles(this.getX(), this.getY(), this.getZ());
        }
    }

    public RMagicMissileProjectile(Level level, LivingEntity owner,Vec3 centerPos) {
        super(EntityRegistry.MAGIC_MISSILE_PROJECTILE.get(), level);
        this.setOwner(owner);
        this.setTargetPos(centerPos);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 100) {
            this.discard();
        }
        if (!this.level().isClientSide) {
            if(target!=null && target.isAlive() && !target.getType().getCategory().isFriendly()){
                Vec3 targetPos = target.position();
                Vec3 toTarget = targetPos.subtract(this.position());
                if (toTarget.length() > 0) {
                    Vec3 motion = toTarget.normalize().scale(BASE_SPEED);
                    this.setDeltaMovement(motion);
                }
            }
            if(target == null || target.isDeadOrDying() || target.getType().getCategory().isFriendly()){
                LivingEntity target= (LivingEntity) XUtils.findNearestProjectile(this, 2);
                if(target!=null) {
                    this.setTarget(target);
                }
                Vec3 direction = this.position().subtract(centerPos).normalize();
                Vec3 motion = direction.scale(BASE_SPEED);
                this.setDeltaMovement(motion);
            }
        }
    }
}
