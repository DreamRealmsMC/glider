package eu.lenithia.glider.api.sender;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.events.SkipAbleSenderEvent;

import java.util.List;

public class SenderAPIImpl implements SenderAPI {

    private final GliderVelocity glider;

    public SenderAPIImpl(GliderVelocity glider) {
        this.glider = glider;
    }

    @Override
    public void send(GCluster cluster, Player player) {

    }

    @Override
    public void send(GCluster cluster, Player player, List<SkipAbleSenderEvent> skipEvents) {

    }

    @Override
    public void send(GCluster cluster, Player player, List<SkipAbleSenderEvent> skipEvents, String sender, int tryCount) {

    }

    @Override
    public void send(GGroup group, Player player) {

    }

    @Override
    public void send(GGroup group, Player player, List<SkipAbleSenderEvent> skipEvents) {

    }

    @Override
    public void send(GGroup group, Player player, List<SkipAbleSenderEvent> skipEvents, String sender, int tryCount) {

    }

    @Override
    public void send(GServer server, Player player) {

    }

    @Override
    public void send(GServer server, Player player, List<SkipAbleSenderEvent> skipEvents) {

    }

    @Override
    public void send(GServer server, Player player, List<SkipAbleSenderEvent> skipEvents, String sender, int tryCount) {

    }
}
