package eu.lenithia.glider.sender;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.clusters.system.GCluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Send {

    private final GCluster cluster;

    private List<Player> players = new ArrayList<>();

    public Send(GCluster cluster, Player player) {
        this.cluster = cluster;
        players.add(player);

    }




}
