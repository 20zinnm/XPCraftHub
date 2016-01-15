package org.originmc.hub.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.gogobebe2.shiftspawn.GameState;
import com.gmail.gogobebe2.shiftspawn.api.GameAPI;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public final class ItemUtils {

    public static ItemStack parseItem(String string) {
        // Return air if item is null.
        ItemStack item = new ItemStack(Material.AIR);
        if (string == null) return item;

        Material type = null;
        byte data = 0;
        int amount = 0;
        String name = "";
        List<String> lore = new ArrayList<>();

        for (String arg : string.split("] ")) {
            String[] info = arg.replace("]", "").split("\\[");
            if (info.length != 2) continue;
            switch (info[0].toLowerCase()) {
                case "type":
                    type = parseMaterial(info[1]);
                    break;
                case "data":
                    data = NumUtils.parseByte(info[1]);
                    break;
                case "amount":
                    amount = NumUtils.parseInt(info[1]);
                    break;
                case "name":
                    name = translateAlternateColorCodes('&', info[1]);
                    break;
                case "lore":
                	statements(GameState.STARTED, "", "", "");
            }
        }

        // Return air if type is null.
        if (type == null) return item;

        try {
            item = new ItemStack(type, amount, data);
        } catch (Exception e) {
            e.printStackTrace();
            return item;
        }

        // Attempt to set the item meta data.
        ItemMeta meta = item.getItemMeta();
        if (!name.isEmpty()) meta.setDisplayName(name);
        if (!lore.isEmpty()) meta.setLore(lore);
        item.setItemMeta(meta);

        // Return item.
        return item;
    }

    public static Material parseMaterial(String string) {
        try {
            return Material.valueOf(string.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException ex) {
            return Material.AIR;
        }
    }
    public static void statements(GameState gameState, String message, String replaceThis, String replaceWith) {
    	List<String> lore = new ArrayList<>();
    	if(GameAPI.getGameState() == gameState) {
    		lore = Arrays.asList(translateAlternateColorCodes('&', message).replace(replaceThis, replaceWith).split("\\|"));
    	}
    }
}
