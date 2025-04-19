package eu.lenithia.glider.commands.subcommands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.api.clustersystem.ClusterSystemAPI;
import eu.lenithia.glider.api.sender.SenderAPI;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.commands.GliderCommand;
import eu.lenithia.glider.commands.parsers.ClusterParser;
import eu.lenithia.glider.commands.parsers.GroupParser;
import eu.lenithia.glider.commands.parsers.ServerParser;
import eu.lenithia.glider.sender.Sender;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.velocity.parser.PlayerParser;

import java.util.*;
import java.util.stream.Collectors;

public class SendSubcommand implements GliderCommand.DualCommandProvider {
    private final GliderVelocity glider;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public SendSubcommand(GliderVelocity glider) {
        this.glider = glider;
    }

    @Override
    public void registerStandalone(GliderCommand manager, Command.Builder<CommandSource> builder) {
        registerSendCommands(manager, builder);
    }

    @Override
    public void registerToGlider(GliderCommand commandsManager) {
        registerSendCommands(commandsManager, commandsManager.getMainCommandBuilder().literal("send"));
    }

    private void registerSendCommands(GliderCommand manager, Command.Builder<CommandSource> builder) {
        // Player send command
        manager.getCommandManager().command(builder
                .literal("player")
                .required("playerName", PlayerParser.playerParser())
                .required("targetCluster", ClusterParser.clusterParser(glider))
                .optional("targetGroup", GroupParser.groupParser(glider, "targetCluster"))
                .optional("targetServer", ServerParser.serverParser(glider, "targetGroup"))
                .handler(this::handlePlayerSendCommand));

        /*
        // Cluster send command
        manager.getCommandManager().command(builder
                .literal("cluster")
                .required("sourceCluster", ClusterParser.clusterParser(glider))
                .required("targetCluster", ClusterParser.clusterParser(glider))
                .optional("targetGroup", GroupParser.groupParser(glider, "targetCluster"))
                .optional("targetServer", ServerParser.serverParser(glider, "targetGroup"))
                .handler(this::handleClusterSendCommand));

        // Group send command
        manager.getCommandManager().command(builder
                .literal("group")
                .required("sourceCluster", ClusterParser.clusterParser(glider))
                .required("sourceGroup", GroupParser.groupParser(glider, "sourceCluster"))
                .required("targetCluster", ClusterParser.clusterParser(glider))
                .optional("targetGroup", GroupParser.groupParser(glider, "targetCluster"))
                .optional("targetServer", ServerParser.serverParser(glider, "targetGroup"))
                .handler(this::handleGroupSendCommand));

        // Server send command
        manager.getCommandManager().command(builder
                .literal("server")
                .required("sourceCluster", ClusterParser.clusterParser(glider))
                .required("sourceGroup", GroupParser.groupParser(glider, "sourceCluster"))
                .required("sourceServer", ServerParser.serverParser(glider, "sourceGroup"))
                .required("targetCluster", ClusterParser.clusterParser(glider))
                .optional("targetGroup", GroupParser.groupParser(glider, "targetCluster"))
                .optional("targetServer", ServerParser.serverParser(glider, "targetGroup"))
                .handler(this::handleServerSendCommand));

         */
    }

    private void handlePlayerSendCommand(CommandContext<CommandSource> context) {
        Player player = context.get("playerName");
        GCluster targetCluster = context.get("targetCluster");
        GGroup targetGroup = context.getOrDefault("targetGroup", null);
        GServer targetServer = context.getOrDefault("targetServer", null);

        if (targetServer != null) {
            System.out.println("SEND SERVER");
            new Sender(targetServer, null, player, "command", 1);
            return;
        }
        if (targetGroup != null) {
            System.out.println("SEND GROUP");
            new Sender(targetGroup, null, player, "command", 1);
            return;
        }

        System.out.println("SEND CLUSTER");
        new Sender(targetCluster, null, player, "command", 1);


    }

    private void handleClusterSendCommand(CommandContext<CommandSource> context) {}

    private void handleGroupSendCommand(CommandContext<CommandSource> context) {}

    private void handleServerSendCommand(CommandContext<CommandSource> context) {}

}