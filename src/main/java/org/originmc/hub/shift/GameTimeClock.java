package org.originmc.hub.shift;

import org.originmc.hub.shift.GameState;

import java.util.concurrent.TimeUnit;

/**
 *
 * Created by meyerzinn on 1/21/16.
 */
public class GameTimeClock {

    private GameState state;
    private Long startTime;
public GameTimeClock(GameState state) {
    this.state = state;
    startTime = System.currentTimeMillis();
}

    public String getTimeRemaining() {
        if (state != GameState.STARTED) return state.toString();
            /*
             * Ugh math.
             */
        Long timeLeft = (System.currentTimeMillis() - startTime < TimeUnit.MINUTES.toMillis(5)) ? TimeUnit.MINUTES.toMillis(5) - (System.currentTimeMillis() - startTime) : TimeUnit.MINUTES.toMillis(5) - (System.currentTimeMillis() - startTime);
        return String.format("%s %sm %ss.", (System.currentTimeMillis() - startTime < TimeUnit.MINUTES.toMillis(5))?"You may join for ":"Game over in ",TimeUnit.MILLISECONDS.toMinutes(timeLeft), TimeUnit.MILLISECONDS.toSeconds(startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft)));

    }


}
