package eu.lenithia.glider.api.sender;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.Sender;
import eu.lenithia.glider.sender.events.SkipAbleSenderEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SenderAPIImpl implements SenderAPI {

    private final GliderVelocity glider;

    public SenderAPIImpl(GliderVelocity glider) {
        this.glider = glider;
    }

    @Override
    public void send(GCluster cluster, Player player) {
        new Sender(cluster, Collections.emptyList(), player, "API", 0);
    }

    @Override
    public void send(GCluster cluster, Player player, List<SkipAbleSenderEvent> skipEvents) {
        new Sender(cluster, skipEvents, player, "API", 0);
    }

    @Override
    public void send(GCluster cluster, Player player, List<SkipAbleSenderEvent> skipEvents, String sender, int tryCount) {
        new Sender(cluster, skipEvents, player, sender, tryCount);
    }

    @Override
    public void send(GGroup group, Player player) {
        new Sender(group, Collections.emptyList(), player, "API", 0);
    }

    @Override
    public void send(GGroup group, Player player, List<SkipAbleSenderEvent> skipEvents) {
        new Sender(group, skipEvents, player, "API", 0);
    }

    @Override
    public void send(GGroup group, Player player, List<SkipAbleSenderEvent> skipEvents, String sender, int tryCount) {
        new Sender(group, skipEvents, player, sender, tryCount);
    }

    @Override
    public void send(GServer server, Player player) {
        new Sender(server, Collections.emptyList(), player, "API", 0);
    }

    @Override
    public void send(GServer server, Player player, List<SkipAbleSenderEvent> skipEvents) {
        new Sender(server, skipEvents, player, "API", 0);
    }

    @Override
    public void send(GServer server, Player player, List<SkipAbleSenderEvent> skipEvents, String sender, int tryCount) {
        new Sender(server, skipEvents, player, sender, tryCount);
    }
}