package net.wobble.killsound.command;

import net.wobble.killsound.WobbleKillSound;
import net.wobble.killsound.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class KillSoundCommand implements CommandExecutor {

    private final WobbleKillSound plugin;

    public KillSoundCommand(WobbleKillSound plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("wobblekillsound.reload")) {
                sender.sendMessage(ChatUtil.message(plugin, "no-permission"));
                return true;
            }

            plugin.reloadPlugin();
            sender.sendMessage(ChatUtil.message(plugin, "reload-success"));
            return true;
        }

        sender.sendMessage(ChatUtil.message(plugin, "usage"));
        return true;
    }
}
