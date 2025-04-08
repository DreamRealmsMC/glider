package eu.lenithia.glider.api;

import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.api.clustersystem.ClusterSystemAPI;
import eu.lenithia.glider.api.sender.SenderAPI;

public class GliderAPIImpl implements GliderAPI {

    private GliderVelocity glider;

    public GliderAPIImpl(GliderVelocity glider) {
        this.glider = glider;
    }

    @Override
    public SenderAPI getSenderAPI() {
        glider.getClusterSystem();
        return null;
    }

    @Override
    public ClusterSystemAPI getClusterSystemAPI() {
        return null;
    }

    @Override
    public int getAPIVersion() {
        return 1;
    }
}
