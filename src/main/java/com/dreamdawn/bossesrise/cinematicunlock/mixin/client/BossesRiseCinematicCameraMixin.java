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
 * Blocks unwanted cinematic handlers at the entry point:
 * <ul>
 *   <li>CAMERA - forced camera takeover (unless config.forceCameraMovement)</li>
 *   <li>BARS - letterbox black bars (unless config.blackBars)</li>
 *   <li>BLACK - full-screen black overlay (always cancelled)</li>
 *   <li>HIDE_GUI - GUI hiding (unless config.forceCameraMovement)</li>
 * </ul>
 * Injection happens at HEAD of the enum-based startCinematicCamera overload,
 * which is also what the String-based overload delegates to, so every call
 * path is covered.
 *
 * remap = false: the target is a third-party mod class that is never
 * SRG-remapped, so the Mixin annotation processor must not look up an
 * obfuscation mapping for it.
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