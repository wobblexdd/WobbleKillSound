# 🔊 KillSound

Production-ready Minecraft plugin that plays a configurable sound when a player kills another player.

---

## ⚙️ Overview

KillSound enhances PvP feedback by playing a sound on kill events.  
It supports full configuration, radius-based playback, and optional integration with other plugins.

---

## 🚀 Features

- 🔊 Custom kill sound
- 🎚 Configurable volume & pitch
- 🌍 Radius-based sound playback
- 🎯 Option to play only for killer or nearby players
- ⚙️ Fully configurable via `config.yml`
- 💬 Optional messages
- 🔁 Reload command
- 🔗 Integration with `WobbleSettings` (toggle per-player)

---

## 🧠 How it works

When a player kills another player:
- Plugin checks if sound is enabled
- Reads config values (sound, volume, pitch)
- Plays sound:
  - Only to killer OR
  - To nearby players in radius
- Optional message is sent

Kill detection is handled via `PlayerDeathEvent` :contentReference[oaicite:0]{index=0}

---

## ⚙️ Configuration

### `config.yml`

```yml
sound:
  enabled: true
  name: "ENTITY_WITHER_SPAWN"
  volume: 1.0
  pitch: 1.0
  play-to-all: false
  radius: 20.0

messages:
  enabled: false
````

### Parameters

* `enabled` — global toggle
* `name` — Bukkit sound name
* `volume` — sound volume
* `pitch` — sound pitch
* `play-to-all`:

  * `false` → only killer hears sound
  * `true` → all players in radius hear sound
* `radius` — distance for sound (if enabled)

---

## 💬 Messages

### `messages.yml`

```yml
prefix: "&8[&dKillSound&8] &r"

reload-success: "&aKillSound configuration reloaded."
no-permission: "&cYou do not have permission."
usage: "&eUsage: /killsound reload"
kill-sound-played: "&7Played sound: &f{sound}"
```

---

## 🔧 Commands

```
/killsound reload
```

Reloads plugin configuration.

---

## 🔐 Permissions

* `wobblekillsound.reload` — allows reloading plugin

---

## 🔗 Integration

### WobbleSettings (optional)

If installed:

* Players can toggle kill sounds individually
* Plugin uses reflection to call:

```java
isKillSoundsEnabled(UUID)
```

If not installed:

* Kill sounds are always enabled



## 🛠 Technical Details

* Java 21
* Paper 1.21+
* Uses Bukkit `Sound` enum
* Safe sound parsing (no crashes on invalid sound)
* Async-safe event handling


## 📌 Notes

* Invalid sound names are ignored safely
* No sound is played if killer is null
* Works only for PvP kills (player → player)
