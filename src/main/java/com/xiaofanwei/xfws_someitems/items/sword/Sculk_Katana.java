package com.xiaofanwei.xfws_someitems.items.sword;

import com.xiaofanwei.xfws_someitems.MoreAC;
import com.xiaofanwei.xfws_someitems.registries.ItemRegistries;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = MoreAC.MODID)
public class Sculk_Katana extends SwordItem {

    public Sculk_Katana(Tier tier, int rawDamage, float rawSpeed) {
        super(tier, new Item.Properties().durability(tier.getUses())
                .component(DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(tier,rawDamage, rawSpeed + tier.getSpeed()))
        );
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide) {
            PacketDistributor.sendToServer(new MessageSwingArm());
        }
    }

    public static void onLeftClick(final Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
                float range = 10;
                Vec3 start = serverPlayer.getEyePosition();
                Vec3 end = start.add(serverPlayer.getForward().scale((double)range));
                AABB boundingBox = serverPlayer.getBoundingBox().expandTowards(end.subtract(start));
                List<? extends Entity> entities = serverPlayer.level().getEntities(serverPlayer, boundingBox);
                Iterator var11 = entities.iterator();
                serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.WARDEN_SONIC_BOOM, serverPlayer.getSoundSource(),1,1);
                while(var11.hasNext()) {
                    Entity target = (Entity)var11.next();
                    HitResult hit = Utils.checkEntityIntersecting(target, start, end, 0.4F);
                    if (hit.getType() != HitResult.Type.MISS&&!(target instanceof Player) && target instanceof LivingEntity) {
                        target.hurt(serverPlayer.damageSources().sonicBoom(serverPlayer), 10);
                    }
                }
                Vec3 vec3 = serverPlayer.getLookAngle().normalize();
                for(int i = 2; (float)i < range; i+=2) {
                    Vec3 vec32 = vec3.scale((double)i).add(serverPlayer.getEyePosition());
                    Level level= serverPlayer.level();
                    ((ServerLevel)level).sendParticles(ParticleTypes.SONIC_BOOM, vec32.x, vec32.y, vec32.z, 1, 0, 0, 0, 0);
                }
            }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.sculk_katana"));
    }
}
