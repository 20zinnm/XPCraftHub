package org.originmc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.originmc.hub.Hub;
import org.originmc.hub.Perm;
import org.originmc.hub.User;

public final class DoubleJump implements Listener {

    private final Hub plugin;

    public DoubleJump(Hub plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void startDoubleJump(PlayerMoveEvent event) {
        // Do nothing if player is in creative mode.
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;

        // Do nothing if player does not have permission.
        if (!Perm.DOUBLEJUMP.has(player)) return;

        // Deny flight by default.
        player.setAllowFlight(false);

        // Do nothing if user is null.
        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;

        // Revoke the players jumping status if they are on the ground.
        if (player.isOnGround()) {
            if (user.isJumping()) user.setJumping(false);
            return;
        }

        // Send a smoke effect to the player if performing a double jump.
        if (user.isJumping()) {
            player.playEffect(player.getLocation(), Effect.SMOKE, BlockFace.SELF);
            return;
        }

        // Let the player fly if they have left the ground.
        player.setAllowFlight(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void finishDoubleJump(PlayerToggleFlightEvent event) {
        // Do nothing if player is in creative mode.
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;

        // Do nothing if player does not have permission.
        if (!Perm.DOUBLEJUMP.has(player)) return;

        // Do nothing if user is null.
        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;

        // Do nothing if player is already performing a double jump.
        if (user.isJumping()) return;

        // Player is now performing a double jump.
        user.setJumping(true);

        // Increase the players velocity to shoot them up.
        player.setVelocity(player.getLocation().getDirection().multiply(1.6D).setY(1.0D));

        // Send a flames effect to the player.
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 2F, 1F);
        player.playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, null);

        // Take player off fly mode.
        player.setAllowFlight(false);
    }
}
