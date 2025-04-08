package eu.lenithia.glider.api.sender;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.events.SkipAbleSenderEvent;

import java.util.List;

public interface SenderAPI {


    /**
     * Sends a player to a cluster.
     *
     * @param cluster    The cluster to send the player to.
     * @param player     The player to send.
     */
    void send(GCluster cluster, Player player);

    /**
     * Sends a player to a cluster with specified skip events.
     *
     * @param cluster    The cluster to send the player to.
     * @param player     The player to send.
     * @param skipEvents The list of events to skip.
     */
    void send(GCluster cluster, Player player, List<SkipAbleSenderEvent> skipEvents);

    /**
     * Sends a player to a cluster with specified skip events, sender name, and try count.
     *
     * @param cluster    The cluster to send the player to.
     * @param player     The player to send.
     * @param skipEvents The list of events to skip.
     * @param sender     The name of the sender implementation (Use it to track events invoked by you).
     * @param tryCount   The number of attempts to send the player (Mainly intended for queue implementations).
     */
    void send(GCluster cluster, Player player, List<SkipAbleSenderEvent> skipEvents, String sender, int tryCount);

    /**
     * Sends a player to a group.
     *
     * @param group      The group to send the player to.
     * @param player     The player to send.
     */
    void send(GGroup group, Player player);

    /**
     * Sends a player to a group with specified skip events.
     *
     * @param group      The group to send the player to.
     * @param player     The player to send.
     * @param skipEvents The list of events to skip.
     */
    void send(GGroup group, Player player, List<SkipAbleSenderEvent> skipEvents);

    /**
     * Sends a player to a group with specified skip events, sender name, and try count.
     *
     * @param group      The group to send the player to.
     * @param player     The player to send.
     * @param skipEvents The list of events to skip.
     * @param sender     The name of the sender implementation (Use it to track events invoked by you).
     * @param tryCount   The number of attempts to send the player (Mainly intended for queue implementations).
     */
    void send(GGroup group, Player player, List<SkipAbleSenderEvent> skipEvents, String sender, int tryCount);

    /**
     * Sends a player to a server.
     *
     * @param server     The server to send the player to.
     * @param player     The player to send.
     */
    void send(GServer server, Player player);

    /**
     * Sends a player to a server with specified skip events.
     *
     * @param server     The server to send the player to.
     * @param player     The player to send.
     * @param skipEvents The list of events to skip.
     */
    void send(GServer server, Player player, List<SkipAbleSenderEvent> skipEvents);

    /**
     * Sends a player to a server with specified skip events, sender name, and try count.
     *
     * @param server     The server to send the player to.
     * @param player     The player to send.
     * @param skipEvents The list of events to skip.
     * @param sender     The name of the sender implementation (Use it to track events invoked by you).
     * @param tryCount   The number of attempts to send the player (Mainly intended for queue implementations).
     */
    void send(GServer server, Player player, List<SkipAbleSenderEvent> skipEvents, String sender, int tryCount);

}
