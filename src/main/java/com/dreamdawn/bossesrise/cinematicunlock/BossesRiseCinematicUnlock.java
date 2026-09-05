package com.dreamdawn.bossesrise.cinematicunlock;

import com.dreamdawn.bossesrise.cinematicunlock.config.CinematicUnlockConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Bosses'Rise 过场动画解锁。
 *
 * 客户端补丁：Bosses'Rise 播放过场动画时，原模组会接管玩家镜头（CAMERA 处理器）
 * 并绘制电影黑边（BARS 处理器）。本模组在入口点
 * （BossesRiseClientCinematicCamera#startCinematicCamera）取消这两个处理器，
 * 使玩家保持完整的镜头控制权，且不再看到黑边。其他过场动画效果
 * （黑屏、抖动、视野变化、隐藏GUI）保持不变。
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