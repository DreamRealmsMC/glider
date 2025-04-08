package eu.lenithia.glider.clusters.system;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.events.GServerLoadEvent;
import eu.lenithia.glider.clusters.integrationsystem.templates.GServerIntegration;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GServer {

    private final GliderVelocity glider;
    private final GGroup group;
    private Section serverConfig;
    private final String serverName;
    private RegisteredServer registeredServer;

    private final ConcurrentHashMap<String, GServerIntegration> serverIntegrations = new ConcurrentHashMap<>();

    public GServer(GGroup gGroup, String name, Section section, CompletableFuture<Void> loadFuture) {
        this.glider = gGroup.getGlider();
        this.group = gGroup;
        this.serverName = name;
        this.serverConfig = section;
        init(loadFuture);
    }

    private void init(CompletableFuture<Void> loadFuture) {
        String ip = getServerConfig().getString("settings.ip");
        int port = getServerConfig().getInt("settings.port");

        ServerInfo info = new ServerInfo(getGroup().getCluster().getClusterPrefix() + getServerName(), new InetSocketAddress(ip, port));
        registeredServer = getGlider().getProxy().getServer(info.getName()).orElseGet(() -> getGlider().getProxy().registerServer(info));

        glider.getProxy().getEventManager().fire(new GServerLoadEvent(this)).thenRun(() -> {
            for (GServerIntegration serverIntegration : serverIntegrations.values()) {
                serverIntegration.onLoad();
            }
            glider.getLogger().info("[SERVER] ({}) {} loaded with {} integrations",
                    getGroup().getGroupName(), serverName, serverIntegrations.size());
            loadFuture.complete(null);
        });
    }

    public void reload() {
        String newIp = getServerConfig().getString("settings.ip");
        int newPort = getServerConfig().getInt("settings.port");
        InetSocketAddress newAddress = new InetSocketAddress(newIp, newPort);

        if (!registeredServer.getServerInfo().getAddress().equals(newAddress)) {
            getGlider().getProxy().unregisterServer(registeredServer.getServerInfo());
            ServerInfo newInfo = new ServerInfo(getGroup().getCluster().getClusterPrefix() + getServerName(), newAddress);
            registeredServer = getGlider().getProxy().registerServer(newInfo);
        }
    }

    public void unload() {

    }


}
