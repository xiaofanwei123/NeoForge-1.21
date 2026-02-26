package com.xiaofanwei.xfws_someitems.entity;

import com.xiaofanwei.xfws_someitems.registries.XSoundEvents;
import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RMagicMissileProjectile extends MagicMissileProjectile {
    private static final double BASE_SPEED = 0.40;
    @Nullable
    private LivingEntity target;
    private Vec3 centerPos;

    public double getBaseSpeed() {
        if(target == null || target.isDeadOrDying() || this.tickCount < 10){
            return BASE_SPEED;
        }
        else {
            return BASE_SPEED * 2;
        }
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public void setTargetPos(Vec3 centerPos) {
        this.centerPos = centerPos;
    }

    protected void onHit(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)hitResult);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if(this.getOwner()!=null){
            Entity entity =entityHitResult.getEntity();
            if (entity instanceof Player player && player.isCreative()) return;
            if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                XUtils.modifyDamage(this, (Player) this.getOwner(),livingEntity, 1);
            }
        }
        this.beforeDiscard();
    }

    public void beforeDiscard() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), XSoundEvents.CRYSTAL_IMPACT.get(), SoundSource.NEUTRAL, 1F, 1F);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult){
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), XSoundEvents.CRYSTAL_IMPACT.get(), SoundSource.NEUTRAL, 1F, 1F);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
    }

    @Override
    public void checkDespawn() {
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
            this.beforeDiscard();
        }
        if (!this.level().isClientSide) {
            if(target!=null && !target.getType().getCategory().isFriendly()){
                if(target.isAlive()){
                    Vec3 targetPos = target.position();
                    Vec3 toTarget = targetPos.subtract(this.position());
                    if (toTarget.length() > 0) {
                        Vec3 motion = toTarget.normalize().scale(getBaseSpeed());
                        this.setDeltaMovement(motion);
                    }
                }
                if (target.isDeadOrDying()) {
                    this.deltaMovementOld = this.getDeltaMovement();
                    travel();
                }
            }
            if(target == null || target.isDeadOrDying() || target.getType().getCategory().isFriendly()){
                LivingEntity target= (LivingEntity) XUtils.findNearestProjectile(this, 2);
                if(target!=null) {
                    this.setTarget(target);
                }
            }
        }
    }
}
