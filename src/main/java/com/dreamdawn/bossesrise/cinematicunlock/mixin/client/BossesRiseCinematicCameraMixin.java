package com.dreamdawn.bossesrise.cinematicunlock.mixin.client;

import com.dreamdawn.bossesrise.cinematicunlock.config.CinematicUnlockConfig;
import net.unusual.block_factorys_bosses.client.CinematicEntity;
import net.unusual.block_factorys_bosses.client.camera.BossesRiseClientCinematicCamera;
import net.unusual.block_factorys_bosses.client.camera.CinematicCameraTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 在入口点拦截不需要的过场动画处理器：
 * <ul>
 *   <li>CAMERA - 强制镜头接管（除非 config.forceCameraMovement 启用）</li>
 *   <li>BARS - 电影黑边（除非 config.blackBars 启用）</li>
 *   <li>BLACK - 全屏黑屏覆盖（始终取消）</li>
 *   <li>HIDE_GUI - GUI隐藏（除非 config.forceCameraMovement 启用）</li>
 * </ul>
 * 注入点在基于枚举的 startCinematicCamera 重载方法的 HEAD 处，
 * 基于字符串的重载方法也会委托到此方法，因此覆盖了所有调用路径。
 *
 * remap = false：目标是第三方模组类，不会进行 SRG 重映射，
 * 因此 Mixin 注解处理器不能为其查找混淆映射。
 */
@Mixin(BossesRiseClientCinematicCamera.class)
public abstract class BossesRiseCinematicCameraMixin {

    @Inject(
            method = "startCinematicCamera(Lnet/unusual/block_factorys_bosses/client/camera/CinematicCameraTypes;Lnet/unusual/block_factorys_bosses/client/CinematicEntity;Ljava/lang/String;)V",
            remap = false,
            at = @At("HEAD"),
            cancellable = true
    )
    private static void bossesrisecinematicunlock$blockForcedEffects(
            CinematicCameraTypes handler,
            CinematicEntity trackedObject,
            String cameraBoneName,
            CallbackInfo ci
    ) {
        boolean forceCamera = CinematicUnlockConfig.FORCE_CAMERA_MOVEMENT.get();
        if (handler == CinematicCameraTypes.CAMERA && !forceCamera) {
            ci.cancel();
        } else if (handler == CinematicCameraTypes.BARS && !CinematicUnlockConfig.BLACK_BARS.get()) {
            ci.cancel();
        } else if (handler == CinematicCameraTypes.BLACK) {
            ci.cancel();
        } else if (handler == CinematicCameraTypes.HIDE_GUI && !forceCamera) {
            ci.cancel();
        }
    }
}