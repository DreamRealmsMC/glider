package eu.lenithia.glider.sender;

import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.events.*;
import eu.lenithia.glider.sender.objects.SenderResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
public class Sender {

    private final GliderVelocity glider;

    private final String sender;
    private final int tryCount;

    private final GCluster cluster;

    private final List<Player> players = new ArrayList<>();

    private final List<SkipAbleSenderEvent> skipEvents = new ArrayList<>();

    private GGroup group;
    private GServer server;

    private List<GServer> targetServers = new ArrayList<>();
    private List<GServer> authorizedServers = new ArrayList<>();
    private List<GServer> availableServers = new ArrayList<>();
    private ConcurrentMap<GServer, Integer> bestServers = new ConcurrentHashMap<>();

    private SenderResponse senderResponse;


    public Sender(GCluster cluster, List<SkipAbleSenderEvent> skipEvents, Player player, String sender, int tryCount) {
        this.glider = cluster.getGlider();
        this.cluster = cluster;
        this.players.add(player);
        this.sender = sender;
        this.tryCount = tryCount;

        if (skipEvents != null) {
            this.skipEvents.addAll(skipEvents);
        }

        init();
    }

    public Sender(GGroup group, List<SkipAbleSenderEvent> skipEvents, Player player, String sender, int tryCount) {
        this.group = group;
        this.cluster = group.getCluster();
        this.players.add(player);
        this.sender = sender;
        this.tryCount = tryCount;
        this.glider = cluster.getGlider();

        if (skipEvents != null) {
            this.skipEvents.addAll(skipEvents);
        }

        init();
    }

    public Sender(GServer server, List<SkipAbleSenderEvent> skipEvents, Player player, String sender, int tryCount) {
        this.server = server;
        this.group = server.getGroup();
        this.cluster = server.getGroup().getCluster();
        this.players.add(player);
        this.sender = sender;
        this.tryCount = tryCount;
        this.glider = cluster.getGlider();

        if (skipEvents != null) {
            this.skipEvents.addAll(skipEvents);
        }

        init();
    }

    private void init() {
        if(!skipEvents.contains(SkipAbleSenderEvent.FIND_PLAYERS)) {
            glider.getProxy().getEventManager().fire(new SenderFindPlayersEvent(cluster, players, sender, senderResponse)).thenRun(this::findGroup);
        }
    }

    private void findGroup() {
        if (group == null) {
            group = getCluster().getDefaultGroup();
            glider.getProxy().getEventManager().fire(new SenderFindGroupEvent(cluster, players, sender, group)).thenRun(this::findRestrictedServers);
        }
    }

    private void findRestrictedServers() {
        if (server == null) {
            targetServers = new ArrayList<>(group.getGroupServers().values());
        } else {
            targetServers = new ArrayList<>();
            targetServers.add(server);
        }


        if (targetServers.isEmpty()) {
            senderResponse = SenderResponse.EPIC_FAIL;
            proceed();
            return;
        }


        authorizedServers = targetServers;

        if(!skipEvents.contains(SkipAbleSenderEvent.FIND_RESTRICTED_SERVERS)) {
            glider.getProxy().getEventManager().fire(new SenderFindRestrictedServersEvent(glider, players, sender, senderResponse, authorizedServers)).thenRun(this::findAvailableServers);
        }
    }

    private void findAvailableServers() {
        if (senderResponse == SenderResponse.CANCELED) {
            return;
        }

        if (authorizedServers.isEmpty()) {
            senderResponse = SenderResponse.UNAUTHORIZED;
            proceed();
            return;
        }

        availableServers = authorizedServers;

        if (!skipEvents.contains(SkipAbleSenderEvent.FIND_AVAILABLE_SERVERS)) {
            glider.getProxy().getEventManager().fire(new SenderFindAvailableServersEvent(glider, players, sender, senderResponse, availableServers)).thenRun(this::findBestServer);
        }
    }

    private void findBestServer() {
        if (senderResponse == SenderResponse.CANCELED) {
            return;
        }

        if (availableServers.isEmpty()) {
            senderResponse = SenderResponse.UNAVAILABLE;
            proceed();
            return;
        }

        if (server != null) {
            bestServers.put(server, 0);
            proceed();
            return;
        }

        bestServers = new ConcurrentHashMap<>();
        for (GServer server : availableServers) {
            bestServers.put(server, 0);
        }

        if (!skipEvents.contains(SkipAbleSenderEvent.FIND_BEST_SERVER)) {
            glider.getProxy().getEventManager().fire(new SenderFindBestServerEvent(this)).thenRun(this::proceed);
        } else {
            proceed();
        }
    }

    public GServer getBestServer() {
        return bestServers.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null); // Return null if no servers are available
    }

    private void proceed() {
        if (senderResponse == null) { senderResponse = SenderResponse.SUCCESS; }

        SenderProceedEvent event = new SenderProceedEvent(this);
        glider.getProxy().getEventManager().fire(event).thenAccept(proceedEvent -> {

            if (proceedEvent.isHandled() || !proceedEvent.getResult().isAllowed()) {
                return;
            }

            new SenderResponseHandler() {}.handleResponse(this);
        });
    }

}
