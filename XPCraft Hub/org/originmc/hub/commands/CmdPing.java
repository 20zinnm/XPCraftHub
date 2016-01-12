package org.originmc.hub.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.bukkit.ChatColor.*;

public final class CmdPing implements CommandExecutor {

    private static final String API_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    private static final String PING_MESSAGE = YELLOW + "Player " + LIGHT_PURPLE + "{PLAYER} " + YELLOW + "has " + LIGHT_PURPLE + "{PING} " + YELLOW + "ping.";

    private final Field ping;

    public CmdPing(Hub plugin) {
        try {
            Class<?> nmsPlayerListClass = Class.forName("net.minecraft.server." + API_VERSION + ".EntityPlayer");
            ping = nmsPlayerListClass.getDeclaredField("ping");
            ping.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        plugin.getCommand("ping").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        // Do nothing if sender does not have permission.
        if (!Perm.PING.has(sender)) return true;

        // Attempt to find a suitable player.
        Player player = null;
        if (args.length > 0) {
            List<Player> matches = Bukkit.matchPlayer(args[0]);
            if (!matches.isEmpty()) player = matches.get(0);
        } else if (sender instanceof Player) {
            player = (Player) sender;
        }

        // If no player is found, give an error.
        if (player == null) {
            sender.sendMessage(RED + "No such player online!");
            return true;
        }

        // Attempt to get the players ping.
        int ping;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            ping = (int) this.ping.get(entityPlayer);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Send the sender what the players ping is.
        sender.sendMessage(PING_MESSAGE
                .replace("{PLAYER}", player.getName())
                .replace("{PING}", String.valueOf(ping)));
        return true;
    }
}
