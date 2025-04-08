package eu.lenithia.glider.sender;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;

public class Sender {


    private final GliderVelocity glider;

    public Sender(GliderVelocity glider) {
        this.glider = glider;
    }

    public boolean send(GCluster cluster, Player player) {
        return false;
    }

    public boolean send(GCluster cluster, Player[] players) {
        return false;
    }

    public boolean send(GCluster cluster, GGroup group, Player player) {
        return false;
    }

    public boolean send(GCluster cluster, GGroup group, Player[] players) {
        return false;
    }

    public boolean send(GCluster cluster, GGroup group, GServer server, Player player) {
        return false;
    }

    public boolean send(GCluster cluster, GGroup group, GServer server, Player[] players) {
        return false;
    }

}
