package eu.lenithia.glider.sender.events;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.Sender;
import eu.lenithia.glider.sender.objects.SenderResponse;
import lombok.Getter;

import java.util.List;

public class SenderFindBestServerEvent {
    private final Sender sender;

    @Getter
    private final List<Player> players;

    @Getter
    private final String senderName;

    @Getter
    private final SenderResponse senderResponse;

    @Getter
    private final GliderVelocity glider;

    public SenderFindBestServerEvent(Sender sender) {
        this.sender = sender;
        this.players = sender.getPlayers();
        this.senderName = sender.getSender();
        this.senderResponse = sender.getSenderResponse();
        this.glider = sender.getGlider();
    }

    /**
     * Adds points to a server's score
     * @param server The server to add points to
     * @param points The number of points to add
     */
    public void addPoints(GServer server, int points) {
        if (sender.getBestServers().containsKey(server)) {
            sender.getBestServers().put(server, sender.getBestServers().get(server) + points);
        }
    }

    /**
     * Removes points from a server's score
     * @param server The server to remove points from
     * @param points The number of points to remove
     */
    public void removePoints(GServer server, int points) {
        if (sender.getBestServers().containsKey(server)) {
            sender.getBestServers().put(server, sender.getBestServers().get(server) - points);
        }
    }

    /**
     * Sets exact points for a server's score
     * @param server The server to set points for
     * @param points The number of points to set
     */
    public void setPoints(GServer server, int points) {
        if (sender.getBestServers().containsKey(server)) {
            sender.getBestServers().put(server, points);
        }
    }
}