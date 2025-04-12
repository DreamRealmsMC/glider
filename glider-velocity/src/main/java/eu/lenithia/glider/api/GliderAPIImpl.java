package eu.lenithia.glider.api;

import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.api.clustersystem.ClusterSystemAPI;
import eu.lenithia.glider.api.clustersystem.ClusterSystemAPIImpl;
import eu.lenithia.glider.api.command.CommandAPI;
import eu.lenithia.glider.api.command.CommandAPIImpl;
import eu.lenithia.glider.api.sender.SenderAPI;
import eu.lenithia.glider.api.sender.SenderAPIImpl;

public class GliderAPIImpl implements GliderAPI {

    private GliderVelocity glider;

    public GliderAPIImpl(GliderVelocity glider) {
        this.glider = glider;
    }

    @Override
    public SenderAPI getSenderAPI() {
        return new SenderAPIImpl(glider);
    }

    @Override
    public ClusterSystemAPI getClusterSystemAPI() {
        return new ClusterSystemAPIImpl(glider);
    }

    @Override
    public CommandAPI getCommandAPI() {
        return new CommandAPIImpl(glider);
    }

    @Override
    public int getAPIVersion() {
        return 1;
    }
}
