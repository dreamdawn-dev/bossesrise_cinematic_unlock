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
 * Cancels the potion effects Bosses'Rise applies to nearby entities while its
 * cinematics play (when config.forceCameraMovement is off):
 * <ul>
 *   <li>DAMAGE_RESISTANCE amplifier 255 - Kraken intro/phase-transition, Dragon phase-transition and death</li>
 *   <li>MOVEMENT_SLOWDOWN amplifier 4 - Dragon phase-transition and death</li>
 *   <li>DAMAGE_RESISTANCE / MOVEMENT_SLOWDOWN amplifier 200 - Yeti enrage transition</li>
 * </ul>
 * These exact amplifiers are only ever used by the cinematic code, so the
 * filter does not touch normal gameplay effects.
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