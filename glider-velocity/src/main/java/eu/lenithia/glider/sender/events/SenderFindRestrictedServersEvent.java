package eu.lenithia.glider.sender.events;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.objects.SenderResponse;
import lombok.Getter;

import java.util.List;

public class SenderFindRestrictedServersEvent {

    @Getter
    private final List<Player> players;

    private final GliderVelocity glider;

    private SenderResponse senderResponse;

    @Getter
    private final String sender;

    @Getter
    private final List<GServer> authorizedServers;

    public SenderFindRestrictedServersEvent(GliderVelocity glider, List<Player> players, String sender, SenderResponse senderResponse, List<GServer> authorizedServers) {
        this.players = players;
        this.glider = glider;
        this.sender = sender;
        this.senderResponse = senderResponse;
        this.authorizedServers = authorizedServers;
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public void removeAuthorizedServer(GServer server) {
        if (authorizedServers.contains(server)) {
            this.authorizedServers.remove(server);
        } else {
            glider.getLogger().error("An addon attempted to remove a server that is not in the authorized servers list.");
        }
    }

    public void setCancelled() {
        senderResponse = SenderResponse.CANCELED;
    }
}
