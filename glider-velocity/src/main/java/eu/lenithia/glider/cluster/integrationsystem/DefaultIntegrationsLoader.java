package eu.lenithia.glider.cluster.integrationsystem;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.cluster.events.GClusterLoadEvent;
import eu.lenithia.glider.cluster.integrationsystem.builtin.ClusterDefaultSettings;
import eu.lenithia.glider.cluster.system.GCluster;

public class DefaultIntegrationsLoader {

    private GliderVelocity glider;

    public DefaultIntegrationsLoader(GliderVelocity glider) {
        this.glider = glider;
    }

    @Subscribe(priority = Short.MAX_VALUE)
    public void onClusterLoad(GClusterLoadEvent event) {
        glider.getLogger().info("[EVENT] Loading default integrations for cluster {}", event.getGCluster().getClusterName());
        GCluster cluster = event.getGCluster();
        cluster.getClusterInteractions().put("settings", new ClusterDefaultSettings(cluster));
    }

}
