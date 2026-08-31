package com.dreamdawn.bossesrise.cinematicunlock.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.unusual.block_factorys_bosses.block.entity.BossSpawnerBlockEntity;
import net.unusual.block_factorys_bosses.entity.boss.AbstractBossEntity;
import net.unusual.block_factorys_bosses.init.BossesRiseEntities;

/**
 * Removes the Dragon Tower structure requirement from the dragon spawner.
 *
 * Original behavior: the BossSpawnerBlock only spawns the Infernal Dragon when
 * it is located inside a naturally generated Dragon Tower structure. This
 * mixin replaces the tick logic so that any player standing within 8 blocks of
 * the spawner block is enough to summon the dragon, no matter where the block
 * is. The "destroy the spawner if a boss is already nearby" guard is kept.
 */
@Mixin(BossSpawnerBlockEntity.class)
public abstract class BossSpawnerTriggerMixin {


    @Inject(method = "tick", remap = false, at = @At("HEAD"), cancellable = true)
    private void bossesrisecinematicunlock$spawnWithoutStructureCheck(Level level, CallbackInfo ci) {
        if (!(level instanceof ServerLevel serverLevel)) {
            ci.cancel();
            return;
        }

        BlockPos pos = ((BlockEntity) (Object) this).getBlockPos();

        // Keep the original guard: if a boss entity is already within 16 blocks,
        // destroy the spawner block instead of spawning another one.
        if (!serverLevel.getEntitiesOfClass(AbstractBossEntity.class, AABB.ofSize(pos.getCenter(), 16.0, 16.0, 16.0)).isEmpty()) {
            serverLevel.destroyBlock(pos, false);
            ci.cancel();
            return;
        }

        // New trigger: any player within 8 blocks (squared distance < 64).
        for (Player player : serverLevel.players()) {
            if (player.distanceToSqr(pos.getCenter()) < 64.0) {
                Entity dragon = BossesRiseEntities.INFERNAL_DRAGON.get().create(serverLevel);
                if (dragon != null) {
                    dragon.moveTo(pos.getX(), pos.getY(), pos.getZ());
                    serverLevel.addFreshEntity(dragon);
                    if (dragon instanceof AbstractBossEntity bossEntity) {
                        bossEntity.onBossSpawnerSpawn((BossSpawnerBlockEntity) (Object) this);
                    }
                    serverLevel.destroyBlock(pos, false);
                }
                break;
            }
        }

        ci.cancel();
    }
}