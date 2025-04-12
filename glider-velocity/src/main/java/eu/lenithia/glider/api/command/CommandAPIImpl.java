package eu.lenithia.glider.api.command;

import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.commands.GliderCommand;

public class CommandAPIImpl implements CommandAPI {

    private final GliderVelocity glider;

    public CommandAPIImpl(GliderVelocity glider) {
        this.glider = glider;
    }

    @Override
    public void registerStandalone(GliderCommand.StandaloneCommandProvider provider, String commandName, String... aliases) {

    }

    @Override
    public void registerInGlider(GliderCommand.GliderSubcommandProvider provider) {

    }

    @Override
    public void registerAsDual(GliderCommand.GliderSubcommandProvider provider, String commandName, String... aliases) {

    }
}
