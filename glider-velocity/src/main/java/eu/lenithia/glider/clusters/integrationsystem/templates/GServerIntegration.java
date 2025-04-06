package eu.lenithia.glider.clusters.integrationsystem.templates;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GServer;
import lombok.Getter;

@Getter
public abstract class GServerIntegration {

    private final GServer gServer;

    public GServerIntegration(GServer gServer) {
        this.gServer = gServer;
    }

    public Section getServerConfig() {
        return gServer.getServerConfig();
    }

    public GliderVelocity getGlider() {
        return gServer.getGlider();
    }

    public abstract void onLoad();

    public abstract void onReload();

    public abstract void onUnload();

}
