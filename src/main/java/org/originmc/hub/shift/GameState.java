package org.originmc.hub.shift;

/**
 * Created by meyerzinn on 1/21/16.
 * Originally by gogobebe2 in ShiftSpawn.
 */

public enum GameState {
    WAITING(0),
    STARTING(1),
    STARTED(2),
    RESTARTING(3),
    ERROR(-1);

    private int code;

    GameState(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static GameState getByCode(int i) {
        for (GameState gs : GameState.values()) {
            if (gs.getCode() == i) return gs;
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
