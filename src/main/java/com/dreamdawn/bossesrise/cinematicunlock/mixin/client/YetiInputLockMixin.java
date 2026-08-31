package com.dreamdawn.bossesrise.cinematicunlock.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.unusual.block_factorys_bosses.event.ClientEvents;
import net.unusual.block_factorys_bosses.init.BossesRiseItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Removes the yeti's "freeze" during its enrage transition cutscene.
 *
 * The original ClientEvents#updateMovementInput zeroes the local player's
 * movement input whenever a nearby yeti's DATA_GROUNDSMASH_ANIMTIME is between
 * 11 and 109. The yeti's ENRAGED state increments that counter every tick, so
 * the input lock also fires during the enrage cutscene, freezing the player in
 * place. The real ground-smash attack never actually drives that counter, so
 * dropping the lock only affects the cutscene. The ice gauntlet movement boost
 * is kept.
 */
@Mixin(ClientEvents.class)
public abstract class YetiInputLockMixin {

    @Inject(method = "updateMovementInput", remap = false, at = @At("HEAD"), cancellable = true)
    private static void bossesrisecinematicunlock$removeYetiInputLock(MovementInputUpdateEvent event, CallbackInfo ci) {
        if (event.getEntity() instanceof LocalPlayer player) {
            if (player.isUsingItem() && player.getUseItem().getItem() == BossesRiseItems.ICE_GAUNTLET.get()) {
                event.getInput().forwardImpulse *= 2.0F;
                event.getInput().leftImpulse *= 2.0F;
            }
        }
        ci.cancel();
    }
}