package org.originmc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;
import org.originmc.hub.User;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public final class PlayerHider implements Listener {

    private final Hub plugin;

    public PlayerHider(Hub plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void toggleHidePlayers(PlayerInteractEvent event) {
        // Do nothing if player is not right clicking.
        Action action = event.getAction();
        if (action != RIGHT_CLICK_AIR && action != RIGHT_CLICK_BLOCK) return;

        // Do nothing if player does not have permission.
        Player player = event.getPlayer();
        if (!Perm.PLAYERHIDER.has(player)) return;

        // Do nothing if player is not clicking the player hider item.
        if (!player.getItemInHand().equals(plugin.getSettings().getPlayerHiderItemDisabled()) &&
                !player.getItemInHand().equals(plugin.getSettings().getPlayerHiderItemEnabled())) return;

        // Do nothing if user is null.
        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;

        // Toggle users hiding players option.
        user.setHidePlayers(!user.isHidePlayers());

        // Hide all players for the player.
        if (user.isHidePlayers()) {
            Bukkit.getOnlinePlayers().forEach(player::hidePlayer);
        } else {
            Bukkit.getOnlinePlayers().forEach(player::showPlayer);
        }

        // Update players inventory with the newer player hider item.
        if (plugin.getSettings().getPlayerHiderSlot() >= 0) {
            ItemStack reverse = player.getItemInHand().equals(plugin.getSettings().getPlayerHiderItemEnabled()) ?
                    plugin.getSettings().getPlayerHiderItemDisabled() : plugin.getSettings().getPlayerHiderItemEnabled();
            player.getInventory().setItem(plugin.getSettings().getPlayerHiderSlot(), reverse);
            player.updateInventory();
        }

        // Play a clicking sound.
        player.playSound(player.getLocation(), Sound.CLICK, 2.0F, 1.0F);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void hidePlayers(PlayerJoinEvent event) {
        Bukkit.getOnlinePlayers().stream().filter(p -> {
            User user = plugin.getUser(p.getUniqueId());
            return user != null && user.isHidePlayers();
        }).forEach(p -> p.hidePlayer(event.getPlayer()));
    }
}
