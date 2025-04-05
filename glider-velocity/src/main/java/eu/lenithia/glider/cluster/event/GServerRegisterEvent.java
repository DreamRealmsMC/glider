package eu.lenithia.glider.cluster.event;


import eu.lenithia.glider.cluster.system.GServer;
import lombok.Getter;

@Getter
public class GServerRegisterEvent {

    private final GServer gServer;

    public GServerRegisterEvent(GServer gServer) {
        this.gServer = gServer;
    }

}
