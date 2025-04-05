package eu.lenithia.glider;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.dejvokep.boostedyaml.YamlDocument;
import eu.lenithia.glider.cluster.event.GServerRegisterEvent;
import eu.lenithia.glider.cluster.system.GServer;
import eu.lenithia.glider.utils.ConfigLoader;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class GliderVelocity {

    private final Logger logger;
    private final ProxyServer proxy;
    private final Path dataDirectory;

    private YamlDocument config;


    @Inject
    public GliderVelocity(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {
        logger.info("GliderVelocity initialized");

        // Load the config
        YamlDocument config = ConfigLoader.getVersionedConfig(dataDirectory, "config", getClass().getResourceAsStream("/config.yml"));
        logger.info("GliderVelocity loaded config");

        String configString = String.valueOf(config);

        logger.info("GliderVelocity config: {}", configString);

        ServerInfo info = new ServerInfo("Auth", new InetSocketAddress("23.88.13.8", 40010));
        RegisteredServer server = proxy.registerServer(info);

        new GServer(proxy,null);
        new GServer(proxy,null);
        new GServer(proxy,null);

    }

    @Subscribe
    public void onServerRegister(GServerRegisterEvent event) {
        logger.info("!!! A SERVER WAS REGISTERED !!! event works lol");
    }

}
