package eu.lenithia.glider.sender;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.events.SenderSkipAbleEvent;

public class Sender {


    private final GliderVelocity glider;

    public Sender(GliderVelocity glider) {
        this.glider = glider;

        send(glider.getClusterSystem().getClusters().get("kokot"), new SenderSkipAbleEvent[]{SenderSkipAbleEvent.FIND_ALL_AVAILABLE_SERVERS, SenderSkipAbleEvent.FIND_PLAYERS}, new Player[]{});
    }

    private void send(GCluster cluster, SenderSkipAbleEvent[] skipEvents, Player[] players) {
    }

    public boolean send(GCluster cluster, SenderSkipAbleEvent[] skipEvents , Player player) {
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
