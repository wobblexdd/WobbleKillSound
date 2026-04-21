package net.klouse.killsound.util;

import net.klouse.killsound.KillSound;
import org.bukkit.configuration.file.FileConfiguration;

public final class ChatUtil {

    private ChatUtil() {
    }

    public static String color(String text) {
        return text == null ? "" : text.replace("&", "§");
    }

    public static String message(KillSound plugin, String path) {
        return message(plugin, path, new String[0]);
    }

    public static String message(KillSound plugin, String path, String... replacements) {
        FileConfiguration messages = plugin.getMessagesConfig();
        String prefix = color(messages.getString("prefix", "&8[&dKillSound&8] &r"));
        String raw = color(messages.getString(path, "&cMissing message: " + path));

        for (int i = 0; i + 1 < replacements.length; i += 2) {
            raw = raw.replace(replacements[i], replacements[i + 1]);
        }

        return prefix + raw;
    }
}
