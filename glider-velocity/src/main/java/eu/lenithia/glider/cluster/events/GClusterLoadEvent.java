package eu.lenithia.glider.cluster.events;


import eu.lenithia.glider.cluster.system.GCluster;
import lombok.Getter;

@Getter
public class GClusterLoadEvent {

    private final GCluster gCluster;

    public GClusterLoadEvent(GCluster gCluster) {
        this.gCluster = gCluster;
    }
}
