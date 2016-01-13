package org.originmc.hub;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static org.bukkit.ChatColor.translateAlternateColorCodes;
import static org.originmc.hub.util.ItemUtils.parseItem;
import static org.originmc.hub.util.NumUtils.parseInt;

public final class Settings {

    private final Hub plugin;

    private Location spawn;

    private ItemStack playerHiderItemEnabled;

    private ItemStack playerHiderItemDisabled;

    private ItemStack playerStackerItemEnabled;

    private ItemStack playerStackerItemDisabled;

    private ItemStack serverSelectorItem;

    private Inventory serverSelectorInventory;

    private HashMap<Integer, ItemStack> defaultItems;

    Settings(Hub plugin) {
        this.plugin = plugin;
        load();
    }

    public int getConfigVersion() {
        return plugin.getConfig().getInt("config-version", 0);
    }

    public int getLatestConfigVersion() {
        return plugin.getConfig().getDefaults().getInt("config-version", 0);
    }

    public boolean isOutdated() {
        return getConfigVersion() < getLatestConfigVersion();
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location loc) {
        plugin.getSpawnConfig().set("world", loc.getWorld().getName());
        plugin.getSpawnConfig().set("x", loc.getX());
        plugin.getSpawnConfig().set("y", loc.getY());
        plugin.getSpawnConfig().set("z", loc.getZ());
        plugin.getSpawnConfig().set("yaw", loc.getYaw());
        plugin.getSpawnConfig().set("pitch", loc.getPitch());
        plugin.saveSpawnConfig();
        spawn = loc;
    }

    public boolean disableBurn() {
        return plugin.getConfig().getBoolean("disable-burn");
    }

    public boolean disableDecay() {
        return plugin.getConfig().getBoolean("disable-decay");
    }

    public boolean disableExplosions() {
        return plugin.getConfig().getBoolean("disable-explosions");
    }

    public boolean disableFoodLevelChange() {
        return plugin.getConfig().getBoolean("disable-food-level-change");
    }

    public boolean disableInventoryEditing() {
        return plugin.getConfig().getBoolean("disable-inventory-editing");
    }

    public boolean disablePlayerDamage() {
        return plugin.getConfig().getBoolean("disable-player-damage");
    }

    public boolean disableVoidFalling() {
        return plugin.getConfig().getBoolean("disable-void-falling");
    }

    public boolean disableWeather() {
        return plugin.getConfig().getBoolean("disable-weather");
    }
    

    public String getNewbieMessage() {
        return translateAlternateColorCodes('&', plugin.getConfig().getString("newbie-message"));
    }

    public String getJoinMessage(Player player) {
        String joinMessage = "";
        for (String key : plugin.getConfig().getConfigurationSection("join-messages").getKeys(false)) {
            if (!player.hasPermission("hub.joinmessages." + key)) {
                continue;
            }
            joinMessage = plugin.getConfig().getString("join-messages." + key);
        }
        return joinMessage == null ? "" : translateAlternateColorCodes('&', joinMessage);
    }

    public String getQuitMessage(Player player) {
        String quitMessage = "";
        for (String key : plugin.getConfig().getConfigurationSection("quit-messages").getKeys(false)) {
            if (!player.hasPermission("hub.quitmessages." + key)) {
                continue;
            }
            quitMessage = plugin.getConfig().getString("quit-messages." + key);
        }
        return quitMessage == null ? "" : translateAlternateColorCodes('&', quitMessage);
    }

    public String getLinksMessage() {
        return translateAlternateColorCodes('&', plugin.getConfig().getString("links-message"));
    }

    public boolean cleanInventoryOnJoin() {
        return plugin.getConfig().getBoolean("clean-inventory-on-join");
    }

    public HashMap<Integer, ItemStack> getDefaultItems() {
        return defaultItems;
    }

    public boolean isPlayerHiderDefault() {
        return plugin.getConfig().getBoolean("player-hider.default");
    }

    public int getPlayerHiderSlot() {
        return plugin.getConfig().getInt("player-hider.slot");
    }

    public ItemStack getPlayerHiderItemEnabled() {
        return playerHiderItemEnabled.clone();
    }

    public ItemStack getPlayerHiderItemDisabled() {
        return playerHiderItemDisabled.clone();
    }

    public boolean isPlayerStackerDefault() {
        return plugin.getConfig().getBoolean("player-stacker.default");
    }

    public int getPlayerStackerSlot() {
        return plugin.getConfig().getInt("player-stacker.slot");
    }

    public ItemStack getPlayerStackerItemEnabled() {
        return playerStackerItemEnabled.clone();
    }

    public ItemStack getPlayerStackerItemDisabled() {
        return playerStackerItemDisabled.clone();
    }

    public int getServerSelectorSlot() {
        return plugin.getConfig().getInt("server-selector.slot");
    }

    public String getServerSelectorCommand() {
        return plugin.getConfig().getString("server-selector.command");
    }

    public ItemStack getServerSelectorItem() {
        return serverSelectorItem.clone();
    }

    public Inventory getServerSelectorInventory() {
        return serverSelectorInventory;
    }

    public void load() {
        updateServerSelectorInventory();
        serverSelectorItem = parseItem(plugin.getConfig().getString("server-selector.clickable-item"));
        playerHiderItemEnabled = parseItem(plugin.getConfig().getString("player-hider.item-enabled"));
        playerHiderItemDisabled = parseItem(plugin.getConfig().getString("player-hider.item-disabled"));
        playerStackerItemEnabled = parseItem(plugin.getConfig().getString("player-stacker.item-enabled"));
        playerStackerItemDisabled = parseItem(plugin.getConfig().getString("player-stacker.item-disabled"));
        updateDefaultItems();
        updateSpawn();
    }

    private void updateServerSelectorInventory() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("server-selector");
        serverSelectorInventory = Bukkit.createInventory(null, section.getInt("inventory-rows") * 9, section.getString("inventory-name"));

        // Clone item into all inventory slots.
        ItemStack defaultItem = parseItem(section.getString("default-item"));
        for (int i = 0; i < serverSelectorInventory.getSize(); i++) {
            serverSelectorInventory.setItem(i, defaultItem.clone());
        }

        // Setup inventory with all items specified within the "servers" configuration section.
        section = section.getConfigurationSection("servers");
        for (String server : section.getKeys(false)) {
            serverSelectorInventory.setItem(section.getInt(server + ".slot"), parseItem(section.getString(server + ".item")));
        }
    }

    private void updateDefaultItems() {
        // Set default items to a new HashMap.
        defaultItems = Maps.newHashMap();

        // Load all default items list.
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("default-items");
        for (String index : section.getKeys(false)) {
            defaultItems.put(Math.abs(parseInt(index)), parseItem(section.getString(index)));
        }

        // Add default player hider item.
        if (getPlayerHiderSlot() >= 0) {
            if (isPlayerHiderDefault()) {
                defaultItems.put(getPlayerHiderSlot(), getPlayerHiderItemEnabled());
            } else {
                defaultItems.put(getPlayerHiderSlot(), getPlayerHiderItemDisabled());
            }
        }

        // Add default player stacker item.
        if (getPlayerStackerSlot() >= 0) {
            if (isPlayerStackerDefault()) {
                defaultItems.put(getPlayerStackerSlot(), getPlayerStackerItemEnabled());
            } else {
                defaultItems.put(getPlayerStackerSlot(), getPlayerStackerItemDisabled());
            }
        }

        // Add server selector item.
        if (getServerSelectorSlot() >= 0) {
            defaultItems.put(getServerSelectorSlot(), getServerSelectorItem());
        }
    }

    private void updateSpawn() {
        // Do nothing if specified world is not loaded into the server.
        String worldName = plugin.getSpawnConfig().getString("world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            plugin.getLogger().info("Failed to load spawn for invalid world: " + worldName);

            try {
                spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
            } catch (IndexOutOfBoundsException e) {
                plugin.getLogger().info("Unable to load any spawns due to no worlds being loaded!");
                return;
            }

            plugin.getLogger().warning("Resorted to using " + spawn.getWorld().getName() + "'s default spawn.");
            return;
        }

        // Set the spawn.
        double x = plugin.getSpawnConfig().getDouble("x");
        double y = plugin.getSpawnConfig().getDouble("y");
        double z = plugin.getSpawnConfig().getDouble("z");
        float yaw = (float) plugin.getSpawnConfig().getDouble("yaw");
        float pitch = (float) plugin.getSpawnConfig().getDouble("pitch");
        spawn = new Location(world, x, y, z, yaw, pitch);

        // Set Bukkit's spawn to this location also.
        world.setSpawnLocation((int) x, (int) y, (int) z);
    }
}
