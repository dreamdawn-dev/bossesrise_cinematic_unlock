package com.dreamdawn.bossesrise.cinematicunlock.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.unusual.block_factorys_bosses.event.ClientEvents;
import net.unusual.block_factorys_bosses.init.BossesRiseItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 移除雪怪暴怒转换过场动画期间的"冻结"效果。
 *
 * 原始 ClientEvents#updateMovementInput 在附近雪怪的 DATA_GROUNDSMASH_ANIMTIME
 * 值介于 11 到 109 之间时，会将本地玩家的移动输入归零。雪怪的 ENRAGED 状态
 * 每 tick 都会递增该计数器，因此输入锁定也会在暴怒过场动画期间触发，
 * 将玩家冻结在原地。真正的砸地攻击实际上不会驱动该计数器，
 * 因此移除锁定仅影响过场动画。冰拳套的移动加速效果被保留。
 */
@Mixin(ClientEvents.class)
public abstract class YetiInputLockMixin {

    @Inject(method = "updateMovementInput", remap = false, at = @At("HEAD"), cancellable = true)
    private static void bossesrisecinematicunlock$removeYetiInputLock(MovementInputUpdateEvent event, CallbackInfo ci) {
        if (event.getEntity() instanceof LocalPlayer player) {
            if (player.isUsingItem() && player.getUseItem().getItem() == BossesRiseItems.ICE_GAUNTLET.get()) {
                event.getInput().forwardImpulse *= 2.0F;
                event.getInput().leftImpulse *= 2.0F;
            }
        }
        ci.cancel();
    }
}