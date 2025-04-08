package eu.lenithia.glider.sender.events;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.sender.objects.SenderResponse;
import lombok.Getter;

import java.util.List;

public class SenderFindPlayersEvent {

    @Getter
    private final GCluster cluster;

    @Getter
    private final String sender;

    @Getter
    private final List<Player> players;

    private SenderResponse senderResponse;

    public SenderFindPlayersEvent(GCluster cluster, List<Player> players, String sender, SenderResponse senderResponse) {
        this.players = players;
        this.sender = sender;
        this.cluster = cluster;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void setCancelled() {
        senderResponse = SenderResponse.CANCELED;
    }


}
