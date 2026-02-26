package com.xiaofanwei.xfws_someitems.items.sword;

import com.xiaofanwei.xfws_someitems.entity.RComet;
import com.xiaofanwei.xfws_someitems.registries.XItemRegistry;
import com.xiaofanwei.xfws_someitems.util.RayTraceResult;
import com.xiaofanwei.xfws_someitems.util.XUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class StarWrath extends SwordItem implements OnLeftClick {
    public StarWrath(Tier tier, int rawDamage, float rawSpeed) {
        super(tier, new Item.Properties().rarity(Rarity.EPIC).fireResistant()
                .component(net.minecraft.core.component.DataComponents.UNBREAKABLE,new Unbreakable(false))//不可破坏
                .component(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(tier,rawDamage, rawSpeed + tier.getSpeed()))
        );
    }

    public void onLeftClick(final Player player) {
        if(player.getCooldowns().isOnCooldown(XItemRegistry.STAR_WRATH.get())) return;
        RayTraceResult rayTraceResult = RayTraceResult.rayTrace(player, 32, true);
        if(rayTraceResult != null){
            Vec3 spawnPos;
            if(rayTraceResult.isEntityHit()){
                spawnPos = rayTraceResult.getEntity().position();
            }else if (rayTraceResult.isBlockHit()){
                spawnPos = rayTraceResult.getBlockPos().getCenter();
            }
            else {
                spawnPos = rayTraceResult.getLocation();
            }
            if(player.getCooldowns().isOnCooldown(XItemRegistry.STAR_WRATH.get())) return;
            addComet(player,spawnPos,3,1);
            player.getCooldowns().addCooldown(XItemRegistry.STAR_WRATH.get(), XUtils.getAttackSpeed(player));
        }
    }

    //玩家攻击后的事件
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.getCooldowns().isOnCooldown(XItemRegistry.STAR_WRATH.get())) {
            Vec3 spawnPos = target.position();
            addComet(player,spawnPos,1,2);
        }
    }

    private static void addComet(Player player, Vec3 spawnPos,int count,float ratio){
        for (int i = 0; i < count; i++) {
            RComet comet = new RComet(player.level(), player);
            float power =(float)player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            comet.setDamage(ratio*power/2f);
            comet.setGlowingTag(true);
            comet.setOwner(player);
            comet.setExplosionRadius(Math.max(power/5f,3f));
            comet.shoot(new Vec3((player.getRandom().nextFloat()-0.5)/5, -1+(player.getRandom().nextFloat()-0.5)/5, (player.getRandom().nextFloat()-0.5)/5),0.05F);
            comet.setPos(spawnPos.x + player.getRandom().nextFloat()-0.5, spawnPos.y + 7 + 3*(player.getRandom().nextFloat()-0.5), spawnPos.z + player.getRandom().nextFloat()-0.5);
            player.level().addFreshEntity(comet);
        }
        player.getCooldowns().addCooldown(XItemRegistry.STAR_WRATH.get(), XUtils.getAttackSpeed(player));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.item.xfws_someitems.star_wrath").withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
