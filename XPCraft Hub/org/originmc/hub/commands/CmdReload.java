package org.originmc.hub.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

import static org.bukkit.ChatColor.GREEN;

public final class CmdReload implements CommandExecutor {

    private final Hub plugin;

    public CmdReload(Hub plugin) {
        this.plugin = plugin;
        plugin.getCommand("hubreload").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        // Do nothing if sender does not have permission.
        if (!Perm.RELOAD.has(sender)) return true;

        // Reload the configuration.
        plugin.reloadConfig();
        plugin.reloadSpawnConfig();
        plugin.getSettings().load();
        sender.sendMessage(GREEN + plugin.getName() + " config reloaded.");
        return true;
    }
}
