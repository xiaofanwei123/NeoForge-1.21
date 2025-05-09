package com.xiaofanwei.xfws_someitems.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;

public class RayTraceResult {
    private final Entity entity;//实体
    private final BlockPos blockPos;//方块坐标
    private final Vec3 location;//命中点的坐标（实体或方块的碰撞点，或视线终点）

    private RayTraceResult(Entity entity, BlockPos blockPos, Vec3 location) {
        this.entity = entity;
        this.blockPos = blockPos;
        this.location = location;
    }

    public static RayTraceResult fromEntity(Entity entity, Vec3 hitLocation) {
        return new RayTraceResult(entity, null, hitLocation);
    }

    public static RayTraceResult fromBlock(BlockPos blockPos, Vec3 hitLocation) {
        return new RayTraceResult(null, blockPos, hitLocation);
    }

    public static RayTraceResult miss(Vec3 traceEnd) {
        return new RayTraceResult(null, null, traceEnd);
    }

    public boolean isEntityHit() {
        return entity != null;
    }

    public boolean isBlockHit() {
        return blockPos != null;
    }


    public boolean isMiss() {
        return entity == null && blockPos == null;
    }

    public Entity getEntity() {
        return entity;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Vec3 getLocation() {
        return location;
    }

    public static RayTraceResult rayTrace(Player player, double distance, boolean checkFluids) {
        HitResult hitResult = player.pick(distance, 0.0f, checkFluids);
        Vec3 eyePosition = player.getEyePosition();
        Vec3 viewVector = player.getViewVector(1.0f);
        Vec3 traceEnd = eyePosition.add(viewVector.scale(distance));
        AABB searchBox = player.getBoundingBox()
                .expandTowards(viewVector.scale(distance))
                .inflate(1.0);
        double maxDistanceSq = (hitResult.getType() != HitResult.Type.MISS)
                ? eyePosition.distanceToSqr(hitResult.getLocation())
                : distance * distance;
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                player, eyePosition, traceEnd, searchBox,
                entity -> !entity.isSpectator() && entity.isPickable(),
                maxDistanceSq
        );

        if (entityHitResult != null) {
            double entityDistSq = eyePosition.distanceToSqr(entityHitResult.getLocation());
            if (entityDistSq < maxDistanceSq || hitResult.getType() == HitResult.Type.MISS) {
                hitResult = entityHitResult;
            }
        }
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) hitResult;
            return RayTraceResult.fromEntity(entityHit.getEntity(), entityHit.getLocation());
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            return RayTraceResult.fromBlock(blockHit.getBlockPos(), blockHit.getLocation());
        } else {
            return RayTraceResult.miss(traceEnd);
        }
    }
}