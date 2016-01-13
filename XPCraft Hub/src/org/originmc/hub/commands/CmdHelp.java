package org.originmc.hub.commands;

import com.google.common.collect.ImmutableSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

import static org.bukkit.ChatColor.*;

public final class CmdHelp implements CommandExecutor {

    private static final ImmutableSet<String> HELP_MESSAGE = ImmutableSet.of(
            GOLD + "_____________.[" + DARK_GREEN + " Help for \"Hub Plugin\" " + GOLD + "]._____________",
            AQUA + "/eject " + YELLOW + " Ejects the player riding you",
            AQUA + "/links " + YELLOW + " View all server links",
            AQUA + "/msg " + DARK_AQUA + "<player> <message>" + YELLOW + " Message a player",
            AQUA + "/reply " + DARK_AQUA + "<message>" + YELLOW + " Reply to message partner"
    );

    public CmdHelp(Hub plugin) {
        plugin.getCommand("help").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        // Do nothing if sender does not have permission.
        if (!Perm.HELP.has(sender)) return true;

        // Send all help messages.
        HELP_MESSAGE.forEach(sender::sendMessage);
        return true;
    }
}
