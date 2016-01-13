package org.originmc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;
import org.originmc.hub.User;

public final class FloorSlam implements Listener {

    private static final int SLAM_RADIUS = 2;

    private final Hub plugin;

    public FloorSlam(Hub plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void startFloorSlam(PlayerToggleSneakEvent event) {
        // Do nothing if player does not have permission.
        Player player = event.getPlayer();
        if (!Perm.FLOORSLAM.has(player)) return;

        // Do nothing if user is null.
        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;

        // Do nothing if user is not performing a double jump.
        if (!user.isJumping()) return;

        // Do nothing if user is already performing a slam.
        if (user.isSlamming()) return;

        // Shoot the player downwards and begin the floor slam.
        player.setVelocity(player.getVelocity().setY(-0.5));
        player.playEffect(player.getLocation(), Effect.GHAST_SHOOT, null);
        user.setSlamming(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void finishFloorSlam(EntityDamageEvent event) {
        // Do nothing if entity is not a player.
        if (!(event.getEntity() instanceof Player)) return;

        // Do nothing if player does not have permission.
        Player player = (Player) event.getEntity();
        if (!Perm.FLOORSLAM.has(player)) return;

        // Do nothing if user is not slamming.
        User user = plugin.getUser(player.getUniqueId());
        if (user == null || !user.isSlamming()) return;

        // Do nothing if damage was not caused by falling.
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        // Stop the player from sneaking.
        player.setSneaking(false);
        user.setSlamming(false);

        // Play floor slam effect for all blocks surrounding player.
        Location loc = player.getLocation();
        Location block;
        for (int x = -SLAM_RADIUS; x <= SLAM_RADIUS; x++) {
            for (int y = -SLAM_RADIUS; y <= SLAM_RADIUS; y++) {
                for (int z = -SLAM_RADIUS; z <= SLAM_RADIUS; z++) {
                    block = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
                    player.playEffect(block, Effect.STEP_SOUND, block.getBlock().getType());
                }
            }
        }
    }
}
