package com.dreamdawn.bossesrise.cinematicunlock.mixin;

import com.dreamdawn.bossesrise.cinematicunlock.config.CinematicUnlockConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 取消 Bosses'Rise 在过场动画期间施加给附近实体的药水效果
 * （当 config.forceCameraMovement 为 off 时）：
 * <ul>
 *   <li>伤害抗性 255级 - 克拉肯登场/阶段转换、龙阶段转换和死亡</li>
 *   <li>缓慢 4级 - 龙阶段转换和死亡</li>
 *   <li>伤害抗性/缓慢 200级 - 雪怪暴怒转换</li>
 * </ul>
 * 这些精确的等级仅由过场动画代码使用，因此过滤器不会影响正常游戏中的效果。
 */
@Mixin(LivingEntity.class)
public abstract class BossesRiseEffectMixin {

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z", at = @At("HEAD"), cancellable = true)
    private void bossesrisecinematicunlock$cancelCinematicEffects(MobEffectInstance instance, CallbackInfoReturnable<Boolean> cir) {
        if (CinematicUnlockConfig.FORCE_CAMERA_MOVEMENT.get()) {
            return;
        }
        MobEffect effect = instance.getEffect();
        if ((effect == MobEffects.DAMAGE_RESISTANCE && (instance.getAmplifier() == 255 || instance.getAmplifier() == 200))
                || (effect == MobEffects.MOVEMENT_SLOWDOWN && (instance.getAmplifier() == 4 || instance.getAmplifier() == 200))) {
            cir.setReturnValue(false);
        }
    }
}