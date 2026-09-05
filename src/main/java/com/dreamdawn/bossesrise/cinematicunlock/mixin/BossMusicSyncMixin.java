package com.dreamdawn.bossesrise.cinematicunlock.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.unusual.block_factorys_bosses.entity.boss.AbstractBossEntity;
import net.unusual.block_factorys_bosses.forge.BossesRiseForgeNetwork;
import net.unusual.block_factorys_bosses.network.UpdateBossBarTypeMessage;
import net.unusual.block_factorys_bosses.util.BossHandling;
import net.unusual.block_factorys_bosses.util.BossHandling.TrackedBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 修复 Bosses'Rise 的一个Bug：AbstractBossEntity#setPlayingMusic 更新了
 * 服务端 TrackedBoss 记录，但随后向客户端发送的是旧记录，导致客户端
 * 无法得知 Boss 音乐应该停止（例如龙播放死亡过场动画时）。
 * 此 Redirect 将发出的数据包替换为当前映射值，该值在数据包发送前已被更新。
 */
@Mixin(AbstractBossEntity.class)
public abstract class BossMusicSyncMixin {

    @Redirect(
            method = "lambda$setPlayingMusic$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/unusual/block_factorys_bosses/forge/BossesRiseForgeNetwork;sendToPlayer(Lnet/minecraft/server/level/ServerPlayer;Ljava/lang/Object;)V"
            ),
            remap = false
    )
    private void bossesrisecinematicunlock$sendCurrentMusicFlag(ServerPlayer player, Object message) {
        if (message instanceof UpdateBossBarTypeMessage update) {
            TrackedBoss current = BossHandling.TRACKED_BOSSES.get(update.id());
            if (current != null) {
                BossesRiseForgeNetwork.sendToPlayer(player, new UpdateBossBarTypeMessage(update.id(), current));
            } else {
                BossesRiseForgeNetwork.sendToPlayer(player, message);
            }
        } else {
            BossesRiseForgeNetwork.sendToPlayer(player, message);
        }
    }
}