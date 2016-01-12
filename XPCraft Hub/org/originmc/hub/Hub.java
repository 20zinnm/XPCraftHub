package org.originmc.hub;

import com.google.common.collect.Maps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import org.originmc.hub.commands.CmdEject;
import org.originmc.hub.commands.CmdHelp;
import org.originmc.hub.commands.CmdLinks;
import org.originmc.hub.commands.CmdPing;
import org.originmc.hub.commands.CmdReload;
import org.originmc.hub.commands.CmdSetspawn;
import org.originmc.hub.listeners.DoubleJump;
import org.originmc.hub.listeners.FloorSlam;
import org.originmc.hub.listeners.LaunchPad;
import org.originmc.hub.listeners.PlayerHider;
import org.originmc.hub.listeners.PlayerStacker;
import org.originmc.hub.listeners.ServerSelector;
import org.originmc.hub.listeners.StreamListener;
import org.originmc.hub.listeners.WorldMechanics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Hub extends JavaPlugin implements Listener {

    private final HashMap<UUID, User> users = Maps.newHashMap();

    private Settings settings;

    private final File spawnFile = new File(getDataFolder(), "spawn.yml");

    private FileConfiguration spawnConfig;

    public User getUser(UUID playerId) {
        if (users.containsKey(playerId)) {
            return users.get(playerId);
        } else {
            return null;
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public FileConfiguration getSpawnConfig() {
        return spawnConfig;
    }

    @Override
    public void onEnable() {
    	ConsoleCommandSender Logger = Bukkit.getConsoleSender();
        saveDefaultConfig();
        loadSpawnConfig();
        settings = new Settings(this);
        if (settings.isOutdated()) {
            Logger.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c**&4WARNING&c**"));
            Logger.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour configuration file is outdated."));
            Logger.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cBackup your old file and then delete it to generate a new copy."));
        }

        // Generate users for all players that are currently online.
        Bukkit.getOnlinePlayers().forEach(p -> users.put(p.getUniqueId(), new User(this)));

        // Register all listeners.
        new DoubleJump(this);
        new FloorSlam(this);
        new LaunchPad(this);
        new PlayerHider(this);
        new PlayerStacker(this);
        new ServerSelector(this);
        new StreamListener(this);
        new WorldMechanics(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        // Register all commands.
        new CmdEject(this);
        new CmdHelp(this);
        new CmdLinks(this);
        new CmdPing(this);
        new CmdReload(this);
        new CmdSetspawn(this);
    }

    public void saveSpawnConfig() {
        try {
            spawnConfig.save(spawnFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadSpawnConfig() {
        try {
            spawnConfig.load(spawnFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSpawnConfig() {
        if (!spawnFile.exists()) {
            saveResource("spawn.yml", false);
        }
        spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void addUser(PlayerJoinEvent event) {
        users.put(event.getPlayer().getUniqueId(), new User(this));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void removeUser(PlayerQuitEvent event) {
        users.remove(event.getPlayer().getUniqueId());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void itemDrops(PlayerDropItemEvent e) {
    	if (getConfig().getBoolean("disable-item-drops")) e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void eat(PlayerItemConsumeEvent e) {
    	if (getConfig().getBoolean("disable-eating")) e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void place(BlockPlaceEvent e) {
    	Player p = e.getPlayer();
    	if (getConfig().getBoolean("disable-placing") && !p.hasPermission("hub.build")) e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void place(BlockBreakEvent e) {
    	Player p = e.getPlayer();
    	if (getConfig().getBoolean("disable-breaking") && !p.hasPermission("hub.build")) e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void join(PlayerJoinEvent e) {
    	Player p = e.getPlayer();
    	FileConfiguration c = getConfig();
    	if(c.getBoolean("player-profile.default")) {	
			ItemStack skull = new ItemStack(Material.SKULL, 1, (short) 3);
			SkullMeta  meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
			meta.setOwner(p.getName());
			meta.setDisplayName(c.getString("player-profile.displayname"));
			List<String> skulllore = new ArrayList<>();
			skulllore.add(ChatColor.translateAlternateColorCodes('&', getConfig().getString("player-profile.lore")));
			meta.setLore(skulllore);
			skull.setItemMeta(meta);
			p.getInventory().setItem(4, skull);
			p.updateInventory();
		}
    }
    
}
