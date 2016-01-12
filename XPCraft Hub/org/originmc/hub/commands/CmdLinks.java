package org.originmc.hub.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

public final class CmdLinks implements CommandExecutor {

    private final Hub plugin;

    public CmdLinks(Hub plugin) {
        this.plugin = plugin;
        plugin.getCommand("links").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        // Do nothing if sender does not have permission.
        if (!Perm.LINKS.has(sender)) return true;

        // Send the sender the links from configuration.
        sender.sendMessage(plugin.getSettings().getLinksMessage());
        return true;
    }
}

