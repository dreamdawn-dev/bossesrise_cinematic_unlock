# Bosses'Rise Cinematic Unlock

Forge 1.20.1 patch for [Bosses'Rise](https://www.curseforge.com/minecraft/mc-mods/bossesrise) (`block_factorys_bosses` 2.1.2).

Changes how Bosses'Rise cutscenes behave:

- **Free camera** - the forced cinematic camera takeover is cancelled; you keep full look/move control.
- **No black screen** - the full-screen black overlay is always cancelled.
- **No letterbox bars** - the black bars are cancelled (configurable).
- **No hidden UI** - the GUI is no longer hidden during cutscenes.
- **No potion effects** - the Resistance/Slowness effects the mod applies during transitions are cancelled (Dragon 255/IV, Kraken 255, Yeti 200).
- **No yeti freeze** - the yeti's enrage cutscene no longer locks the player's movement input.
- **Music bug fix** - clients were never notified that boss music should stop (the mod sent a stale record in `UpdateBossBarTypeMessage`), so the boss music kept playing/looping through the dragon death cinematic. The packet now carries the current music flag.
- **Dragon spawner without structure check** - the BossSpawnerBlock now summons the Infernal Dragon whenever a player is within 8 blocks, no matter where the block is (the Dragon Tower structure requirement is removed).

## Configuration

Client config file: `config/bossesrise_cinematic_unlock-client.toml`

| Option | Default | Description |
| --- | --- | --- |
| `forceCameraMovement` | `false` | Keep the original forced cinematic camera. When enabled, cutscenes also keep their potion effects and hidden UI, exactly like the original mod. |
| `blackBars` | `false` | Keep the cinematic letterbox bars. |

The black-screen effect is always cancelled (not configurable).

## Mixins

| Mixin | Target | Purpose |
| --- | --- | --- |
| `BossesRiseCinematicCameraMixin` (client) | `BossesRiseClientCinematicCamera#startCinematicCamera` | Blocks CAMERA / BARS / BLACK / HIDE_GUI handlers per config |
| `BossesRiseEffectMixin` | `LivingEntity#addEffect` | Cancels Resistance 255/200 and Slowness 4/200 cinematic effects |
| `YetiInputLockMixin` (client) | `ClientEvents#updateMovementInput` | Removes the yeti enrage-cutscene movement input lock |
| `BossMusicSyncMixin` | `AbstractBossEntity#setPlayingMusic` (lambda) | Sends the current music flag to clients |
| `StateBossMusicSyncMixin` | `AbstractStateBossEntity#setPlayingMusic` (lambda) | Same fix for state-machine bosses (Kraken) |
| `BossSpawnerTriggerMixin` | `BossSpawnerBlockEntity#tick` | Removes the Dragon Tower structure check; player within 8 blocks summons the dragon |

## Build

```
gradlew build
```

Output: `build/libs/bossesrise_cinematic_unlock-1.0.1.jar`

The original mod jar and GeckoLib in `libs/` are only used for compile-time linking (`fg.deobf`) and are NOT bundled into the output.

## Install

Drop the built jar into the `mods` folder of a Forge 1.20.1 instance that also has Bosses'Rise 2.1.2 and GeckoLib installed (both client and server, since the music fix is server-side).