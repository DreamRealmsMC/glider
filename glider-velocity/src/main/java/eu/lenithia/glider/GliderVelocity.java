package eu.lenithia.glider;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.api.GliderAPI;
import eu.lenithia.glider.api.GliderAPIEvent;
import eu.lenithia.glider.api.GliderAPIImpl;
import eu.lenithia.glider.api.GliderProvider;
import eu.lenithia.glider.clusters.ClusterSystem;
import eu.lenithia.glider.clusters.integrationsystem.DefaultIntegrationsLoader;
import eu.lenithia.glider.commands.GliderCommand;
import eu.lenithia.glider.utils.ConfigLoader;
import eu.lenithia.glider.utils.GliderConsoleText;
import lombok.Getter;
import lombok.Setter;
import org.bstats.velocity.Metrics;
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

    private final Metrics.Factory metricsFactory;

    @Getter
    @Inject
    private Injector injector;

    private final GliderVelocity glider;

    @Setter
    @Getter
    private YamlDocument config;
    @Setter
    @Getter
    private YamlDocument messages;

    @Getter
    private ClusterSystem clusterSystem;
    @Getter
    private GliderCommand gliderCommand;

    @Getter
    private GliderAPI gliderAPI;

    @Inject
    public GliderVelocity(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory, PluginDescription description , Metrics.Factory metrics) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.glider = this;
        this.pluginDescription = description;
        this.metricsFactory = metrics;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {

        String version = pluginDescription.getVersion().orElse("unknown");
        GliderConsoleText.printConsoleText(proxy, version);

        // Load the config
        config = ConfigLoader.getVersionedConfig(dataDirectory, "config", getClass().getResourceAsStream("/config.yml"));
        messages = ConfigLoader.getVersionedConfig(dataDirectory, "messages", getClass().getResourceAsStream("/messages.yml"));
        logger.info("config files loaded");

        // Bstats
        if (getConfig().getBoolean("bstats", true)) {
            Metrics metrics = metricsFactory.make(this, 25403);
        }

        // Load clusters
        proxy.getEventManager().register(glider, new DefaultIntegrationsLoader(glider));
        this.clusterSystem = new ClusterSystem(glider);

        // Load commands
        gliderCommand = new GliderCommand(glider);

        // API register
        GliderAPIImpl apiImpl = new GliderAPIImpl(this);
        GliderProvider.setAPI(apiImpl);
        this.gliderAPI = apiImpl;
        proxy.getEventManager().fire(new GliderAPIEvent(gliderAPI));

    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("Na shledanou!");
    }
}
