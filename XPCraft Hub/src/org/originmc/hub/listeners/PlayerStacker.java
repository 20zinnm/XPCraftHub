package org.originmc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;
import org.originmc.hub.User;

import static org.bukkit.event.block.Action.*;

public final class PlayerStacker implements Listener {

    private final Hub plugin;

    public PlayerStacker(Hub plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void togglePlayerStacker(PlayerInteractEvent event) {
        // Do nothing if player is not right clicking.
        Action action = event.getAction();
        if (action != RIGHT_CLICK_AIR && action != RIGHT_CLICK_BLOCK) return;

        // Do nothing if player does not have permission.
        Player player = event.getPlayer();
        if (!Perm.PLAYERSTACKER.has(player)) return;

        // Do nothing if player is not clicking the player hider item.
        if (!player.getItemInHand().equals(plugin.getSettings().getPlayerStackerItemDisabled()) &&
                !player.getItemInHand().equals(plugin.getSettings().getPlayerStackerItemEnabled())) return;

        // Do nothing if user is null.
        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;

        // Toggle users stacking option.
        user.setStacking(!user.isStacking());

        // Update players inventory with the newer player hider item.
        if (plugin.getSettings().getPlayerStackerSlot() >= 0) {
            ItemStack reverse = player.getItemInHand().equals(plugin.getSettings().getPlayerStackerItemEnabled()) ?
                    plugin.getSettings().getPlayerStackerItemDisabled() : plugin.getSettings().getPlayerStackerItemEnabled();
            player.getInventory().setItem(plugin.getSettings().getPlayerStackerSlot(), reverse);
            player.updateInventory();
        }

        // Play a clicking sound.
        player.playSound(player.getLocation(), Sound.CLICK, 2.0F, 1.0F);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void stackPlayer(PlayerInteractEntityEvent event) {
        // Do nothing if clicked entity is not a player.
        if (!(event.getRightClicked() instanceof Player)) return;

        // Do nothing if player does not have permission.
        Player player = event.getPlayer();
        if (!Perm.PLAYERSTACKER.has(player)) return;

        // Do nothing if clicking player is not stacking.
        User user = plugin.getUser(player.getUniqueId());
        if (user != null && !user.isStacking()) return;

        // Do nothing if clicked player is not stacking.
        Player clicked = (Player) event.getRightClicked();
        user = plugin.getUser(clicked.getUniqueId());
        if (user != null && !user.isStacking()) return;

        // Do nothing if player or clicked player are inside a vehicle.
        if (clicked.isInsideVehicle() || player.isInsideVehicle()) return;

        // Stack the player.
        Entity topPassenger = getTopPassenger(player);
        topPassenger.setPassenger(clicked);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void throwPlayer(PlayerInteractEvent event) {
        // Do nothing if player did not left click.
        Action action = event.getAction();
        if (action != LEFT_CLICK_AIR && action != LEFT_CLICK_BLOCK) return;

        // Do nothing if player does not have permission.
        Player player = event.getPlayer();
        if (!Perm.PLAYERSTACKER.has(player)) return;

        // Do nothing if player is not stacking.
        User user = plugin.getUser(player.getUniqueId());
        if (user != null && !user.isStacking()) return;

        // Do nothing if top passenger is the clicking player.
        Entity topPassenger = getTopPassenger(player);
        if (topPassenger == player) return;

        // Throw the top passenger.
        topPassenger.leaveVehicle();
        topPassenger.setVelocity(player.getLocation().getDirection().multiply(1.6D).setY(1.0D));
    }

    private Entity getTopPassenger(Entity entity) {
        if (entity.getPassenger() != null) {
            return getTopPassenger(entity.getPassenger());
        } else {
            return entity;
        }
    }
}
