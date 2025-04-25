package com.xiaofanwei.xfws_someitems.entity;

import com.xiaofanwei.xfws_someitems.util.XUtils;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ManaStar extends Entity {
    private static final EntityDataAccessor<Float> MANA = SynchedEntityData.defineId(ManaStar.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MAX_DISTANCE = SynchedEntityData.defineId(ManaStar.class, EntityDataSerializers.FLOAT);

    public ManaStar(EntityType<? extends ManaStar> type, Level level) {
        super(type, level);
    }




    @Override
    public void tick() {
        super.tick();

        //下坠
        if (!this.isNoGravity())
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));

        //检测当前实体的碰撞箱（Bounding Box）是否与游戏世界中任何方块或其他实体的碰撞箱发生碰撞
        if (!this.level().noCollision(this.getBoundingBox()))
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());

        double maxDistance = getMaxDistance();

        Player player = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), maxDistance, entity -> {
            Player player1 = (Player) entity;

            return !player1.isSpectator() && XUtils.getPresentMana(player1) < XUtils.getMaxMana(player1);
        });

        //玩家吸收恢复法力
        if (player != null) {
            this.setDeltaMovement(this.getDeltaMovement().add(player.position().add(0F, player.getBbHeight() / 2F, 0F).subtract(this.position()).normalize().scale((maxDistance - this.position().distanceTo(player.position())) / (maxDistance * 8))));
            if (this.position().distanceTo(player.position()) <= player.getBbWidth() && ( XUtils.getPresentMana(player) < XUtils.getMaxMana(player))) {
                XUtils.addMana(player, this.getMana());
                this.discard();
                this.level().playSound(null, this.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.MASTER, 1F, 1.25F);
            }
        }

        //球移动
        this.move(MoverType.SELF, this.getDeltaMovement());

        float friction = 0.98F;
        if (this.onGround()) {
            BlockPos pos = getBlockPosBelowThatAffectsMyMovement();

            friction = this.level().getBlockState(pos).getBlock().getFriction() * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(friction, 0.98D, friction));

        if (this.onGround())
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));

        if (this.tickCount >= 60 * 20)
            this.discard();
    }

    public float getMaxMana() {
        return (float) 100.0;
    }

    public float getMana() {
        return this.getEntityData().get(MANA);
    }

    public void setMana(float mana) {
        this.getEntityData().set(MANA, mana);
    }

    public float getMaxDistance() {
        return this.getEntityData().get(MAX_DISTANCE);
    }

    public void setMaxDistance(double maxDistance){
        this.getEntityData().set(MAX_DISTANCE,  (float)maxDistance);
    }

//    public int getStage() {
//        int stages = 5;
//        return Mth.clamp(getMana() <= 1 ? 1 : (int) Math.ceil(Math.min((getMana() / (getMaxMana() / Math.max(1, stages - 1))) + 1, stages)), 1, stages);
//    }


    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setMana(tag.getFloat("mana"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("mana", getMana());
        tag.putFloat("maxDistance", getMaxDistance());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(MANA, 1F);
        builder.define(MAX_DISTANCE, 5F);
    }

    @Override
    public BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999F);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (MANA.equals(pKey))
            this.refreshDimensions();
        else if (MAX_DISTANCE.equals(pKey))
            this.refreshDimensions();

        super.onSyncedDataUpdated(pKey);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        float scale = 0.5F;

        return EntityDimensions.scalable(scale, scale);
    }
}