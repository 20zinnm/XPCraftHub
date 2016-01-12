package org.originmc.hub.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;

public final class CmdSetspawn implements CommandExecutor {

    private static final String SETSPAWN_MESSAGE = YELLOW + "World spawn has been set. " + GRAY + " (World: {WORLD} X: {X} Y: {Y} Z: {Z})";

    private final Hub plugin;

    public CmdSetspawn(Hub plugin) {
        this.plugin = plugin;
        plugin.getCommand("setspawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        // Do nothing if sender does not have permission.
        if (!Perm.SETSPAWN.has(sender)) return true;

        // Do nothing if sender is not a player.
        if (!(sender instanceof Player)) return true;

        // Get the players location.
        Location location = ((Player) sender).getLocation();

        // Set worlds spawn at the players current location.
        plugin.getSettings().setSpawn(location);
        location.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        // Inform the player.
        sender.sendMessage(SETSPAWN_MESSAGE
                .replace("{WORLD}", location.getWorld().getName())
                .replace("{X}", String.valueOf(location.getBlockX()))
                .replace("{Y}", String.valueOf(location.getBlockY()))
                .replace("{Z}", String.valueOf(location.getBlockZ())));
        return true;
    }
}
