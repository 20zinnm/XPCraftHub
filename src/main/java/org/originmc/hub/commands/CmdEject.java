package org.originmc.hub.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

import static org.bukkit.ChatColor.YELLOW;

public final class CmdEject implements CommandExecutor {

    public CmdEject(Hub plugin) {
        plugin.getCommand("eject").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        // Do nothing if sender does not have permission.
        if (!Perm.EJECT.has(sender)) return true;

        // Do nothing if sender is not a player.
        if (!(sender instanceof Player)) return true;

        // Attempt to eject players off the player.
        Player player = (Player) sender;
        if (player.eject()) {
            // A passengers has been ejected, inform player.
            player.sendMessage(YELLOW + "All passengers ejected!");
            return true;
        }

        // No passengers ejected, inform player.
        player.sendMessage(YELLOW + "No passengers remaining to eject!");
        return true;
    }
}
