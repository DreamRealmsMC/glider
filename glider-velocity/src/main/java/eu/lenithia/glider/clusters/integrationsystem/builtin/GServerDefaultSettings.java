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

    public GServerDefaultSettings(GServer gServer) {
        super(gServer);
    }



    @Override
    public void onLoad() {

    }

    @Override
    public void onReload() {

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