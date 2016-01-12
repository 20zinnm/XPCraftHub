package org.originmc.hub;

public final class User {

    private boolean hidePlayers;

    private boolean jumping;

    private boolean slamming;

    private boolean stacking;

    User(Hub plugin) {
        hidePlayers = plugin.getSettings().isPlayerHiderDefault();
        stacking = plugin.getSettings().isPlayerStackerDefault();
    }

    public boolean isHidePlayers() {
        return hidePlayers;
    }

    public void setHidePlayers(boolean hidePlayers) {
        this.hidePlayers = hidePlayers;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isSlamming() {
        return slamming;
    }

    public void setSlamming(boolean slamming) {
        this.slamming = slamming;
    }

    public boolean isStacking() {
        return stacking;
    }

    public void setStacking(boolean stacking) {
        this.stacking = stacking;
    }
}
