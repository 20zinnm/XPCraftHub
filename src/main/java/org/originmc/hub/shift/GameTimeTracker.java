package org.originmc.hub.shift;

import net.md_5.bungee.api.ChatColor;
import org.originmc.hub.listeners.GameTimeClock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by meyerzinn on 1/21/16.
 */
public class GameTimeTracker {

    private static GameTimeTracker instance;
    private  Map<String, GameTimeClock> currentGameTimes;

    private GameTimeTracker() {
        currentGameTimes = new HashMap<String, GameTimeClock>();
    }

    public void setGameTime(String serverId, GameTimeClock clock) {
        currentGameTimes.put(serverId, clock);
    }

    public String getTimeLeft(String serverId) {
        if (currentGameTimes.containsKey(serverId)) {
            return (currentGameTimes.get(serverId)).getTimeRemaining();
        } else {
            return ChatColor.RED + "Server not found!";
        }
    }

    public void removeServer(String serverId) {
        currentGameTimes.remove(serverId);
    }

    public Set<String> getServers() {
        return currentGameTimes.keySet();
    }

    public static GameTimeTracker getInstance() {
        if (instance != null) return instance;
        instance = new GameTimeTracker();
        return instance;
    }

}
