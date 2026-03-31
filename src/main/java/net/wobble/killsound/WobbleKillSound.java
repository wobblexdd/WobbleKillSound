package net.wobble.killsound;

import net.wobble.killsound.command.KillSoundCommand;
import net.wobble.killsound.util.ChatUtil;
import net.wobble.killsound.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public final class WobbleKillSound extends JavaPlugin implements Listener {

    private FileConfiguration messagesConfig;
    private File messagesFile;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResourceIfNotExists("messages.yml");
        loadMessages();

        getServer().getPluginManager().registerEvents(this, this);

        if (getCommand("killsound") != null) {
            getCommand("killsound").setExecutor(new KillSoundCommand(this));
        }

        getLogger().info("WobbleKillSound enabled.");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKill(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) {
            return;
        }

        if (!getConfig().getBoolean("sound.enabled", true)) {
            return;
        }

        if (!isKillSoundEnabledFor(killer.getUniqueId())) {
            return;
        }

        String soundName = getConfig().getString("sound.name", "ENTITY_WITHER_SPAWN");
        float volume = (float) getConfig().getDouble("sound.volume", 1.0);
        float pitch = (float) getConfig().getDouble("sound.pitch", 1.0);
        boolean playToAll = getConfig().getBoolean("sound.play-to-all", false);
        double radius = getConfig().getDouble("sound.radius", 20.0);

        if (playToAll) {
            for (Player nearby : killer.getWorld().getPlayers()) {
                if (nearby.getLocation().distanceSquared(killer.getLocation()) <= radius * radius) {
                    if (isKillSoundEnabledFor(nearby.getUniqueId())) {
                        SoundUtil.play(nearby, soundName, volume, pitch);
                    }
                }
            }
        } else {
            SoundUtil.play(killer, soundName, volume, pitch);
        }

        if (getConfig().getBoolean("messages.enabled", false)) {
            killer.sendMessage(ChatUtil.message(this, "kill-sound-played",
                    "{sound}", soundName));
        }
    }

    public void reloadPlugin() {
        reloadConfig();
        loadMessages();
    }

    private boolean isKillSoundEnabledFor(UUID uuid) {
        Plugin settingsPlugin = Bukkit.getPluginManager().getPlugin("WobbleSettings");
        if (settingsPlugin == null || !settingsPlugin.isEnabled()) {
            return true;
        }

        try {
            Method getSettingsManager = settingsPlugin.getClass().getMethod("getSettingsManager");
            Object settingsManager = getSettingsManager.invoke(settingsPlugin);
            if (settingsManager == null) {
                return true;
            }

            Method isKillSoundsEnabled = settingsManager.getClass().getMethod("isKillSoundsEnabled", UUID.class);
            Object result = isKillSoundsEnabled.invoke(settingsManager, uuid);

            if (result instanceof Boolean enabled) {
                return enabled;
            }
        } catch (Exception exception) {
            getLogger().warning("Failed to hook into WobbleSettings: " + exception.getMessage());
        }

        return true;
    }

    private void saveResourceIfNotExists(String resourcePath) {
        File file = new File(getDataFolder(), resourcePath);
        if (!file.exists()) {
            saveResource(resourcePath, false);
        }
    }

    private void loadMessages() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().warning("Could not create plugin data folder.");
        }

        this.messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResourceIfNotExists("messages.yml");
        }

        this.messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getMessagesConfig() {
        return Objects.requireNonNull(messagesConfig, "messagesConfig");
    }

    public void saveMessages() {
        if (messagesConfig == null || messagesFile == null) {
            return;
        }

        try {
            messagesConfig.save(messagesFile);
        } catch (IOException exception) {
            getLogger().log(Level.SEVERE, "Could not save messages.yml", exception);
        }
    }
}
