package org.originmc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

import static org.bukkit.Material.*;

public final class LaunchPad implements Listener {

    public LaunchPad(Hub plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void launchPlayer(PlayerMoveEvent event) {
        // Do nothing if player does not have permission.
        Player player = event.getPlayer();
        if (!Perm.LAUNCHPAD.has(player)) return;

        // Do nothing if block player is in is not a stone pressure plate.
        Block block = player.getLocation().getBlock();
        if (block.getType() != STONE_PLATE) return;

        // Do nothing if block below the player is not obsidian.
        if (block.getRelative(BlockFace.DOWN).getType() != OBSIDIAN) return;

        // Shoot the player upwards.
        player.setVelocity(player.getLocation().getDirection().multiply(3.6D).setY(1.4D));
        player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 2.0F, 1.0F);
    }
}
