package org.originmc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public final class ServerSelector implements Listener {

    private final Hub plugin;

    public ServerSelector(Hub plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void selectServer(InventoryClickEvent event) {
        // Do nothing if player who clicked is not a player.
        if (!(event.getWhoClicked() instanceof Player)) return;

        // Do nothing if player does not have permission.
        Player player = (Player) event.getWhoClicked();
        if (!Perm.SERVERSELECTOR.has(player)) return;

        // Do nothing if inventory was not the server selector.
        if (event.getInventory() != plugin.getSettings().getServerSelectorInventory()) return;

        // Do nothing if item does not have any meta data.
        ItemStack item = event.getCurrentItem();
        if (!item.hasItemMeta()) return;

        // Do nothing if there is no such server specified.
        String server = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        if (!plugin.getConfig().getStringList("servers").contains(server)) return;

        // Attempt to teleport the player to the new server.
        player.chat(plugin.getSettings().getServerSelectorCommand().replace("{SERVER}", server));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void openCompass(PlayerInteractEvent event) {
        // Do nothing if player is not right clicking.
        Action action = event.getAction();
        if (action != RIGHT_CLICK_AIR && action != RIGHT_CLICK_BLOCK) return;

        // Do nothing if player does not have permission.
        Player player = event.getPlayer();
        if (!Perm.SERVERSELECTOR.has(player)) return;

        // Do nothing if player is not clicking the server selector item.
        if (!player.getItemInHand().equals(plugin.getSettings().getServerSelectorItem())) return;

        // Open the compass inventory for the player.
        player.openInventory(plugin.getSettings().getServerSelectorInventory());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void stopInventoryChange(InventoryClickEvent event) {
        // Do nothing if inventory changed is not the compass inventory.
        if (!event.getInventory().equals(plugin.getSettings().getServerSelectorInventory())) return;

        // Cancel inventory change.
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void stopInventoryChange(InventoryInteractEvent event) {
        // Do nothing if inventory changed is not the compass inventory.
        if (!event.getInventory().equals(plugin.getSettings().getServerSelectorInventory())) return;

        // Cancel inventory change.
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void stopInventoryChange(InventoryDragEvent event) {
        // Do nothing if inventory changed is not the compass inventory.
        if (!event.getInventory().equals(plugin.getSettings().getServerSelectorInventory())) return;

        // Cancel inventory change.
        event.setCancelled(true);
    }
}
