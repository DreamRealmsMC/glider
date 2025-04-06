package eu.lenithia.glider.clusters.events;


import eu.lenithia.glider.clusters.system.GCluster;
import lombok.Getter;

@Getter
public class GClusterLoadEvent {

    private final GCluster gCluster;

    public GClusterLoadEvent(GCluster gCluster) {
        this.gCluster = gCluster;
    }
}
