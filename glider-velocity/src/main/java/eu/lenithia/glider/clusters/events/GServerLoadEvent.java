package eu.lenithia.glider.clusters.events;


import eu.lenithia.glider.clusters.system.GServer;
import lombok.Getter;

@Getter
public class GServerLoadEvent {

    private final GServer gServer;

    public GServerLoadEvent(GServer gServer) {
        this.gServer = gServer;
    }

}
