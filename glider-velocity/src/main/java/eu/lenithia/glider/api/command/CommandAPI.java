package eu.lenithia.glider.api.command;

import eu.lenithia.glider.commands.GliderCommand;


/**
 * CommandAPI is a way to "easily" implement commands that use Cloud library.
 * It allows you to register standalone commands and /glider subcommands with the Glider API.
 *
 */
public interface CommandAPI {

    /**
     * Register a command with the Glider API
     * @param provider The command provider
     * @param commandName The name of the command
     * @param aliases The aliases of the command
     */
    void registerStandalone(GliderCommand.StandaloneCommandProvider provider, String commandName, String... aliases);

    /**
     * Register a command as a subcommand of the Glider command (/glider <your_subcommand>)
     * @param provider The command provider
     */
    void registerInGlider(GliderCommand.GliderSubcommandProvider provider);

    /**
     * Register a command as both a subcommand and a standalone command
     * @param provider The command provider
     * @param commandName The name of the command
     * @param aliases The aliases of the command
     */
    void registerAsDual(GliderCommand.GliderSubcommandProvider provider, String commandName, String... aliases);




}
