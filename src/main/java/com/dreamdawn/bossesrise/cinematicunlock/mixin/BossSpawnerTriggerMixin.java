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
 * 移除龙刷怪笼对龙塔结构的要求。
 *
 * 原始行为：BossSpawnerBlock 仅在位于自然生成的龙塔结构内时才会生成狱炎龙。
 * 此 Mixin 替换了 tick 逻辑，使得任何非创造模式玩家站在刷怪笼方块 8 格范围内
 * 都足以召唤龙，无论方块位于何处。保留了"如果附近已有 Boss 则摧毁刷怪笼"的守卫逻辑。
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

        // 保留原始守卫逻辑：如果 16 格范围内已存在 Boss 实体，
        // 则摧毁刷怪笼方块，不再生成新的。
        if (!serverLevel.getEntitiesOfClass(AbstractBossEntity.class, AABB.ofSize(pos.getCenter(), 16.0, 16.0, 16.0)).isEmpty()) {
            serverLevel.destroyBlock(pos, false);
            ci.cancel();
            return;
        }

        // 新的触发条件：任意非创造模式玩家在 8 格范围内（距离平方 < 64）。
        for (Player player : serverLevel.players()) {
            if (player.isCreative()) {
                continue;
            }
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