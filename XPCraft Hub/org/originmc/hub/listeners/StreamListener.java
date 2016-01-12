package org.originmc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

public final class StreamListener implements Listener {

    private final Hub plugin;

    public StreamListener(Hub plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void newbieMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            Bukkit.broadcastMessage(plugin.getSettings().getNewbieMessage().replace("{PLAYER}", player.getName()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void setJoinMessage(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
        if (plugin.getSettings().getJoinMessage(p).isEmpty()) {
            event.setJoinMessage(null);
        } else {
            event.setJoinMessage(plugin.getSettings().getJoinMessage(p).replace("{PLAYER}", p.getName()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void shootFirework(PlayerJoinEvent event) {
        // Do nothing if player does not have permission.
        Player player = event.getPlayer();
        if (!Perm.FIREWORKS.has(player)) return;

        // Shoot a new firework.
        Firework fire = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta meta = fire.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.AQUA).withFade(Color.NAVY).with(FireworkEffect.Type.BALL_LARGE).trail(true).build();
        meta.addEffect(effect);
        fire.setFireworkMeta(meta);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void teleportToSpawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(plugin.getSettings().getSpawn());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void teleportToSpawn(PlayerJoinEvent event) {
        event.getPlayer().teleport(plugin.getSettings().getSpawn());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void manageInventory(PlayerJoinEvent event) {
        // Clear inventory if configuration file states so.
        Player player = event.getPlayer();
        if (plugin.getSettings().cleanInventoryOnJoin()) {
            player.getInventory().clear();
        }

        // Loop through all default items and place them in the players inventory.
        plugin.getSettings().getDefaultItems().forEach((slot, item) -> player.getInventory().setItem(slot, item));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void setQuitMessage(PlayerQuitEvent event) {
    	Player p = event.getPlayer();
        if (plugin.getSettings().getQuitMessage(p).isEmpty()) {
            event.setQuitMessage(null);
        } else {
            event.setQuitMessage(plugin.getSettings().getJoinMessage(p).replace("{PLAYER}", p.getName()));
        }
    }
}
