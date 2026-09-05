package com.dreamdawn.bossesrise.cinematicunlock.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 补丁的客户端开关配置。
 *
 * forceCameraMovement = 保留原版强制过场镜头。启用时，过场动画也会保留药水效果
 * （抗性/缓慢）和隐藏UI，与原模组完全一致。禁用时（默认），镜头保持自由，
 * 药水效果被取消，UI保持可见。
 *
 * blackBars = 保留过场电影黑边（默认：禁用）。
 *
 * 黑屏效果始终被取消。
 */
public final class CinematicUnlockConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    /** 保留原版强制过场镜头（及其药水效果 + 隐藏UI）。 */
    public static final ForgeConfigSpec.BooleanValue FORCE_CAMERA_MOVEMENT = BUILDER
            .comment("Keep the original forced cinematic camera. When enabled, cutscenes also keep their potion effects (Resistance/Slowness) and hidden UI, exactly like the original mod.")
            .define("forceCameraMovement", false);

    /** 保留过场电影黑边。 */
    public static final ForgeConfigSpec.BooleanValue BLACK_BARS = BUILDER
            .comment("Keep the cinematic letterbox bars.")
            .define("blackBars", false);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private CinematicUnlockConfig() {
    }
}