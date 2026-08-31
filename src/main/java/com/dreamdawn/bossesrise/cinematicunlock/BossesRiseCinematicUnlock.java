package com.dreamdawn.bossesrise.cinematicunlock;

import com.dreamdawn.bossesrise.cinematicunlock.config.CinematicUnlockConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Bosses'Rise Cinematic Unlock.
 *
 * Client-side patch: while Bosses'Rise cutscenes play, the mod normally takes
 * over the player camera (CAMERA handler) and draws letterbox bars (BARS
 * handler). This mod cancels those two handlers at their entry point
 * (BossesRiseClientCinematicCamera#startCinematicCamera) so the player keeps
 * full camera control and sees no black bars. All other cinematic effects
 * (black screen, shake, FOV, hidden GUI) remain untouched.
 */
@Mod(BossesRiseCinematicUnlock.MOD_ID)
public final class BossesRiseCinematicUnlock {

    public static final String MOD_ID = "bossesrise_cinematic_unlock";

    public BossesRiseCinematicUnlock() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CinematicUnlockConfig.SPEC);
        }
    }
}
