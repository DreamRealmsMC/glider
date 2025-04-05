package eu.lenithia.glider.cluster.system;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.cluster.event.GServerRegisterEvent;
import eu.lenithia.glider.cluster.integration.ServerIntegration;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GServer {

    private Section configSection;
    private ProxyServer proxy;

    @Getter
    private RegisteredServer registeredServer = null;

    @Getter
    private List<ServerIntegration> serverIntegrations;

    public GServer (ProxyServer proxy, Section configSection) {
        this.configSection = configSection;

        proxy.getEventManager().fire(new GServerRegisterEvent(this)).thenAccept(event -> {});

    }

    private void register(ServerInfo info) {
        Optional<RegisteredServer> optionalServer = proxy.getServer(info.getName());
        registeredServer = optionalServer.orElseGet(() -> proxy.registerServer(info));
    }

    public void unregister() {
        proxy.unregisterServer(registeredServer.getServerInfo());
    }


}
