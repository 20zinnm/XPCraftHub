package org.originmc.hub;

import org.bukkit.permissions.Permissible;

public enum Perm {
    DOUBLEJUMP("doublejump"),
    FIREWORKS("fireworks"),
    FLOORSLAM("floorslam"),
    LAUNCHPAD("launchpad"),
    PLAYERHIDER("playerhider"),
    PLAYERSTACKER("playerstacker"),
    SERVERSELECTOR("serverselector"),
    BUILD("build"),
    INVENTORY("inventory"),
    EJECT("eject"),
    HELP("help"),
    RELOAD("reload"),
    LINKS("links"),
    PING("ping"),
    SETSPAWN("setspawn");

    public final String node;

    Perm(String node) {
        this.node = "hub." + node;
    }

    public boolean has(Permissible permissible) {
        return permissible.hasPermission(node);
    }
}
