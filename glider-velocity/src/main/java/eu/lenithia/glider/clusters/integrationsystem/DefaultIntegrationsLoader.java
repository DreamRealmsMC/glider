package eu.lenithia.glider.clusters.integrationsystem;

import com.velocitypowered.api.event.Subscribe;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.events.GClusterLoadEvent;
import eu.lenithia.glider.clusters.events.GGroupLoadEvent;
import eu.lenithia.glider.clusters.events.GServerLoadEvent;
import eu.lenithia.glider.clusters.integrationsystem.builtin.GClusterDefaultSettings;
import eu.lenithia.glider.clusters.integrationsystem.builtin.GGroupDefaultSettings;
import eu.lenithia.glider.clusters.integrationsystem.builtin.GServerDefaultSettings;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;

public class DefaultIntegrationsLoader {

    private final GliderVelocity glider;

    public DefaultIntegrationsLoader(GliderVelocity glider) {
        this.glider = glider;
    }

    @Subscribe(priority = Short.MAX_VALUE)
    public void onClusterLoad(GClusterLoadEvent event) {
        GCluster cluster = event.getGCluster();
        cluster.getClusterInteractions().put("settings", new GClusterDefaultSettings(cluster));
    }

    @Subscribe(priority = Short.MAX_VALUE)
    public void onGroupLoad(GGroupLoadEvent event) {
        GGroup group = event.getGroup();
        group.getGroupIntegrations().put("settings", new GGroupDefaultSettings(group));
    }

    @Subscribe(priority = Short.MAX_VALUE)
    public void onServerLoad(GServerLoadEvent event) {
        GServer server = event.getGServer();
        server.getServerIntegrations().put("settings", new GServerDefaultSettings(server));
    }

}
