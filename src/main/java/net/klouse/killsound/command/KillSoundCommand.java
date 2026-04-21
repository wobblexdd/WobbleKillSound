package net.klouse.killsound.command;

import net.klouse.killsound.KillSound;
import net.klouse.killsound.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class KillSoundCommand implements CommandExecutor {

    private final KillSound plugin;

    public KillSoundCommand(KillSound plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("killsound.reload")) {
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
