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
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.setup.IronsAdjustmentModifier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static io.redspace.ironsspellbooks.config.ClientConfigs.SHOW_FIRST_PERSON_ARMS;
import static io.redspace.ironsspellbooks.config.ClientConfigs.SHOW_FIRST_PERSON_ITEMS;

@OnlyIn(Dist.CLIENT)
public class PlayAnimationTrigger {
    /**
     * 播放动画
     * */
    public static void playAnimation(AbstractClientPlayer clientPlayer, String animationFileName) {

        var rawanimation = PlayerAnimationRegistry.getAnimation(MoreAC.Resource(animationFileName));
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
