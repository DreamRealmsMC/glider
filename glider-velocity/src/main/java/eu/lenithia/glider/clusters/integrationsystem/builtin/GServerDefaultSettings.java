package eu.lenithia.glider.clusters.integrationsystem.builtin;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.proxy.server.ServerPing;
import eu.lenithia.glider.clusters.integrationsystem.templates.GServerIntegration;
import eu.lenithia.glider.clusters.system.GServer;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class GServerDefaultSettings extends GServerIntegration {

    private RegisteredServer registeredServer;;

    public GServerDefaultSettings(GServer gServer) {
        super(gServer);
        setRegisteredServer();
    }

    private void setRegisteredServer() {
        String ip = getServerConfig().getString("settings.ip");
        int port = getServerConfig().getInt("settings.port");

        ServerInfo info = new ServerInfo(getGServer().getGroup().getCluster().getClusterPrefix() + getGServer().getServerName(), new InetSocketAddress(ip, port));
        registeredServer = getGlider().getProxy().getServer(info.getName()).orElseGet(() -> getGlider().getProxy().registerServer(info));
    }

    @Override
    public void onLoad() {
        registeredServer.getServerInfo();
    }

    @Override
    public void onReload() {
        // Implement reload logic if needed
    }

    @Override
    public void onUnload() {

    }

    @Getter
    @Setter
    public static class ServerStatus {
        private int maxPlayers;
        private boolean online;

        public ServerStatus(int maxPlayers, boolean online) {
            this.maxPlayers = maxPlayers;
            this.online = online;
        }
    }
}