package org.originmc.hub.shift;

import org.originmc.hub.shift.GameTimeClock;
import org.originmc.hub.shift.GameTimeTracker;
import org.originmc.hub.shift.GameState;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by meyerzinn on 1/21/16.
 */
public class GameStateChangeListener extends JedisPubSub {

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        if (channel.split(":")[1] == "shift") {
            String serverId = channel.split(":")[2];
            GameState gameState = GameState.getByCode(Integer.valueOf(message));
            if (gameState == null /* Should be the result of a DEL operation. */) {
                GameTimeTracker.getInstance().removeServer(serverId);
            } else {
                GameTimeTracker.getInstance().setGameTime(serverId, new GameTimeClock(gameState));
            }
        }
    }

}
