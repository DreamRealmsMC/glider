package eu.lenithia.glider.cluster.events;


import eu.lenithia.glider.cluster.system.GServer;
import lombok.Getter;

@Getter
public class GServerLoadEvent {

    private final GServer gServer;

    public GServerLoadEvent(GServer gServer) {
        this.gServer = gServer;
    }

}
