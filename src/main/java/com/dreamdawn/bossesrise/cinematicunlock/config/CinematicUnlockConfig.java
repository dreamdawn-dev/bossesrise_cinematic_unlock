package com.dreamdawn.bossesrise.cinematicunlock.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Client-side toggles for the patch.
 *
 * forceCameraMovement = keep the original forced cinematic camera. When
 * enabled, cutscenes also keep their potion effects (Resistance/Slowness) and
 * hidden UI, exactly like the original mod. When disabled (default), the
 * camera stays free, the potion effects are cancelled and the UI stays
 * visible.
 *
 * blackBars = keep the cinematic letterbox bars (default: disabled).
 *
 * The black-screen effect is always cancelled.
 */
public final class CinematicUnlockConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    /** Keep the original forced cinematic camera (and its potion effects + hidden UI). */
    public static final ForgeConfigSpec.BooleanValue FORCE_CAMERA_MOVEMENT = BUILDER
            .comment("Keep the original forced cinematic camera. When enabled, cutscenes also keep their potion effects (Resistance/Slowness) and hidden UI, exactly like the original mod.")
            .define("forceCameraMovement", false);

    /** Keep the cinematic letterbox bars. */
    public static final ForgeConfigSpec.BooleanValue BLACK_BARS = BUILDER
            .comment("Keep the cinematic letterbox bars.")
            .define("blackBars", false);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private CinematicUnlockConfig() {
    }
}