package eu.lenithia.glider.sender;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.events.SkipAbleSenderEvent;
import eu.lenithia.glider.sender.objects.SenderResponse;

import java.util.Arrays;
import java.util.List;

public class Sender {


    private final GliderVelocity glider;

    public Sender(GliderVelocity glider) {
        this.glider = glider;

        send(glider.getClusterSystem().getClusters().get("kokot"), Arrays.asList(SkipAbleSenderEvent.FIND_AVAILABLE_SERVERS, SkipAbleSenderEvent.FIND_PLAYERS), null);
    }

    public void send(GCluster cluster, Player player) {

    }

    public void send(GCluster cluster, List<SkipAbleSenderEvent> skipEvents , Player player) {

    }

    public void send(GGroup group, Player player) {

    }

    public void send(GGroup group, List<SkipAbleSenderEvent> skipEvents , Player player) {

    }

    public void send(GServer server, Player player) {

    }

    public void send(GServer server, List<SkipAbleSenderEvent> skipEvents , Player player) {

    }

}
