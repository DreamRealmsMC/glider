package eu.lenithia.glider.sender.events;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class SenderFindGroupEvent {

    @Getter
    private final GCluster cluster;

    private GGroup group;

    @Getter
    private final String sender;

    @Getter
    private final List<Player> players;

    public SenderFindGroupEvent(GCluster cluster, List<Player> players, String sender, GGroup group) {
        this.cluster = cluster;
        this.players = players;
        this.sender = sender;

        // Validate the initial group
        if (cluster.getClusterGroups().containsValue(group)) {
            this.group = group;
        } else {
            throw new IllegalArgumentException("The provided group does not exist in the cluster.");
        }
    }

    public void setGroup(GGroup group) {
        if (cluster.getClusterGroups().containsValue(group)) {
            this.group = group;
        } else {
            throw new IllegalArgumentException("The provided group does not exist in the cluster.");
        }
    }
}
