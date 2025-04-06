package eu.lenithia.glider;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.clusters.ClusterSystem;
import eu.lenithia.glider.clusters.integrationsystem.DefaultIntegrationsLoader;
import eu.lenithia.glider.commands.GliderCommand;
import eu.lenithia.glider.commands.subcommands.PingSubcommand;
import eu.lenithia.glider.utils.ConfigLoader;
import eu.lenithia.glider.utils.GliderConsoleText;
import lombok.Getter;
import org.slf4j.Logger;


import java.io.IOException;
import java.nio.file.Path;

public class GliderVelocity {

    @Getter
    private final Logger logger;
    @Getter
    private final ProxyServer proxy;
    @Getter
    private final Path dataDirectory;
    @Getter
    private PluginDescription pluginDescription;

    @Getter
    @Inject
    private Injector injector;

    private final GliderVelocity glider;

    @Getter
    private YamlDocument config;

    @Getter
    private ClusterSystem clusterSystem;


    @Inject
    public GliderVelocity(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory, PluginDescription description ) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.glider = this;
        this.pluginDescription = description;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {

        String version = pluginDescription.getVersion().orElse("unknown");
        GliderConsoleText.printConsoleText(proxy, version);

        // Load the config
        YamlDocument config = ConfigLoader.getVersionedConfig(dataDirectory, "config", getClass().getResourceAsStream("/config.yml"));
        logger.info("config files loaded");

        // Load clusters
        proxy.getEventManager().register(glider, new DefaultIntegrationsLoader(glider));
        this.clusterSystem = new ClusterSystem(glider);

        // Load sender



        // Load commands
        GliderCommand gliderCommand = new GliderCommand(glider);

        PingSubcommand pingCommand = new PingSubcommand();
        gliderCommand.registerAsDual(pingCommand, "ping", "latency");

        //gliderCommand.registerSubcommandProvider(new PingSubcommand());

    }

}
