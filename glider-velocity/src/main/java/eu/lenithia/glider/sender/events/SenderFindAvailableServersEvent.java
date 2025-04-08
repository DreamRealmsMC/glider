package eu.lenithia.glider.sender.events;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.objects.SenderResponse;
import lombok.Getter;

import java.util.List;

public class SenderFindAvailableServersEvent {

    @Getter
    private final List<Player> players;

    private final GliderVelocity glider;

    private SenderResponse senderResponse;

    @Getter
    private final String sender;

    @Getter
    private final List<GServer> availableServers;

    public SenderFindAvailableServersEvent(GliderVelocity glider, List<Player> players, String sender, SenderResponse senderResponse, List<GServer> availableServers) {
        this.players = players;
        this.glider = glider;
        this.sender = sender;
        this.senderResponse = senderResponse;
        this.availableServers = availableServers;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void removeAvailableServer(GServer server) {
        if (availableServers.contains(server)) {
            this.availableServers.remove(server);
        } else {
            glider.getLogger().error("An addon attempted to remove a server that is not in the available servers list.");
        }
    }

    public void setCancelled() {
        senderResponse = SenderResponse.CANCELED;
    }
}