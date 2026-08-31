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
 * Fixes a Bosses'Rise bug: AbstractBossEntity#setPlayingMusic updates the
 * server-side TrackedBoss record but then sends the OLD record to clients, so
 * clients never learn that the boss music should stop (e.g. while the dragon
 * plays its death cinematic). This redirect replaces the outgoing message with
 * the current map value, which has already been updated before the packet is
 * sent.
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