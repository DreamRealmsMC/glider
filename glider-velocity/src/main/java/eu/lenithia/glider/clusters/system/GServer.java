package eu.lenithia.glider.clusters.system;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.events.GServerLoadEvent;
import eu.lenithia.glider.clusters.integrationsystem.templates.GServerIntegration;
import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Getter
public class GServer {

    private final GliderVelocity glider;
    private final GGroup group;
    private Section serverConfig;
    private final String serverName;

    private final HashMap<String, GServerIntegration> serverIntegrations = new HashMap<>();

    public GServer(GGroup gGroup, String name, Section section, CompletableFuture<Void> loadFuture) {
        this.glider = gGroup.getGlider();
        this.group = gGroup;
        this.serverName = name;
        this.serverConfig = section;
        init(loadFuture);
    }

    private void init(CompletableFuture<Void> loadFuture) {
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

    }

    public void unload() {

    }


}
