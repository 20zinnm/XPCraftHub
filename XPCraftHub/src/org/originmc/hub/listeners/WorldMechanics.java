package org.originmc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

public final class WorldMechanics implements Listener {

    private final Hub plugin;

    public WorldMechanics(Hub plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableBurn(BlockBurnEvent event) {
        if (plugin.getSettings().disableBurn()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableDecay(BlockFadeEvent event) {
        if (plugin.getSettings().disableDecay()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableExplosions(EntityExplodeEvent event) {
        if (plugin.getSettings().disableExplosions()) {
            event.blockList().clear();
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableFoodLevelChange(FoodLevelChangeEvent event) {
        if (plugin.getSettings().disableFoodLevelChange()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableInventoryEditing(InventoryClickEvent event) {
        // Do nothing if player has permission.
        if (Perm.INVENTORY.has(event.getWhoClicked())) return;

        if (plugin.getSettings().disableInventoryEditing()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void disableInventoryEditing(InventoryInteractEvent event) {
        // Do nothing if player has permission.
        if (Perm.INVENTORY.has(event.getWhoClicked())) return;

        if (plugin.getSettings().disableInventoryEditing()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void disableInventoryEditing(InventoryDragEvent event) {
        // Do nothing if player has permission.
        if (Perm.INVENTORY.has(event.getWhoClicked())) return;

        if (plugin.getSettings().disableInventoryEditing()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableInventoryEditing(PlayerDropItemEvent event) {
        // Do nothing if player has permission.
        if (Perm.INVENTORY.has(event.getPlayer())) return;

        if (plugin.getSettings().disableInventoryEditing()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableInventoryEditing(PlayerPickupItemEvent event) {
        // Do nothing if player has permission.
        if (Perm.INVENTORY.has(event.getPlayer())) return;

        if (plugin.getSettings().disableInventoryEditing()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disablePlayerDamage(EntityDamageEvent event) {
        // Do nothing if entity damaged is not a player.
        if (!(event.getEntity() instanceof Player)) return;

        if (plugin.getSettings().disablePlayerDamage()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void disableVoidFalling(PlayerMoveEvent event) {
        // Do nothing if movement is above a Y value of zero.
        if (event.getTo().getY() >= 0) return;

        if (plugin.getSettings().disableVoidFalling()) {
            event.getPlayer().teleport(event.getTo().getWorld().getSpawnLocation());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableWeather(WeatherChangeEvent event) {
        if (plugin.getSettings().disableWeather()) {
            event.setCancelled(true);
        }
    }
}
