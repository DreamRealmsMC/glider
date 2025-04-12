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
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.velocity.parser.PlayerParser;

import java.util.*;
import java.util.stream.Collectors;

public class SendSubcommand implements GliderCommand.DualCommandProvider {
    private final GliderVelocity glider;
    private final SenderAPI senderAPI;
    private final ClusterSystemAPI clusterSystemAPI;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public SendSubcommand(GliderVelocity glider) {
        this.glider = glider;
        this.senderAPI = glider.getGliderAPI().getSenderAPI();
        this.clusterSystemAPI = glider.getGliderAPI().getClusterSystemAPI();
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
                .required("targetCluster", StringParser.stringParser())
                .optional("targetGroup", StringParser.stringParser())
                .optional("targetServer", StringParser.stringParser())
                .handler(this::handlePlayerSendCommand));

        // Cluster send command
        manager.getCommandManager().command(builder
                .literal("cluster")
                .required("sourceCluster", StringParser.stringParser())
                .required("targetCluster", StringParser.stringParser())
                .optional("targetGroup", StringParser.stringParser())
                .optional("targetServer", StringParser.stringParser())
                .handler(this::handleClusterSendCommand));

        // Group send command
        manager.getCommandManager().command(builder
                .literal("group")
                .required("sourceGroup", StringParser.stringParser())
                .required("targetCluster", StringParser.stringParser())
                .optional("targetGroup", StringParser.stringParser())
                .optional("targetServer", StringParser.stringParser())
                .handler(this::handleGroupSendCommand));

        // Server send command
        manager.getCommandManager().command(builder
                .literal("server")
                .required("sourceServer", StringParser.stringParser())
                .required("targetCluster", StringParser.stringParser())
                .optional("targetGroup", StringParser.stringParser())
                .optional("targetServer", StringParser.stringParser())
                .handler(this::handleServerSendCommand));
    }

    private void handlePlayerSendCommand(CommandContext<CommandSource> context) {
        CommandSource source = context.sender();
        Player player = context.get("playerName");
        String targetClusterName = context.get("targetCluster");
        String targetGroupName = context.getOrDefault("targetGroup", null);
        String targetServerName = context.getOrDefault("targetServer", null);

        sendPlayer(source, player, targetClusterName, targetGroupName, targetServerName);
    }

    private void handleClusterSendCommand(CommandContext<CommandSource> context) {
        CommandSource source = context.sender();
        String sourceClusterName = context.get("sourceCluster");
        String targetClusterName = context.get("targetCluster");
        String targetGroupName = context.getOrDefault("targetGroup", null);
        String targetServerName = context.getOrDefault("targetServer", null);

        Optional<GCluster> sourceCluster = clusterSystemAPI.getCluster(sourceClusterName);
        if (sourceCluster.isEmpty()) {
            source.sendMessage(miniMessage.deserialize("<red>Source cluster not found.</red>"));
            return;
        }

        List<Player> playersToSend = getPlayersInCluster(sourceCluster.get());
        sendMultiplePlayers(source, playersToSend, targetClusterName, targetGroupName, targetServerName);
    }

    private void handleGroupSendCommand(CommandContext<CommandSource> context) {
        CommandSource source = context.sender();
        String sourceGroupName = context.get("sourceGroup");
        String targetClusterName = context.get("targetCluster");
        String targetGroupName = context.getOrDefault("targetGroup", null);
        String targetServerName = context.getOrDefault("targetServer", null);

        Optional<GGroup> sourceGroup = findGroupByName(sourceGroupName);
        if (sourceGroup.isEmpty()) {
            source.sendMessage(miniMessage.deserialize("<red>Source group not found.</red>"));
            return;
        }

        List<Player> playersToSend = getPlayersInGroup(sourceGroup.get());
        sendMultiplePlayers(source, playersToSend, targetClusterName, targetGroupName, targetServerName);
    }

    private void handleServerSendCommand(CommandContext<CommandSource> context) {
        CommandSource source = context.sender();
        String sourceServerName = context.get("sourceServer");
        String targetClusterName = context.get("targetCluster");
        String targetGroupName = context.getOrDefault("targetGroup", null);
        String targetServerName = context.getOrDefault("targetServer", null);

        int serverId;
        try {
            serverId = Integer.parseInt(sourceServerName);
        } catch (NumberFormatException e) {
            source.sendMessage(miniMessage.deserialize("<red>Invalid server ID format.</red>"));
            return;
        }

        Optional<GServer> sourceServer = findServerById(serverId);
        if (sourceServer.isEmpty()) {
            source.sendMessage(miniMessage.deserialize("<red>Source server not found.</red>"));
            return;
        }

        List<Player> playersToSend = getPlayersInServer(sourceServer.get());
        sendMultiplePlayers(source, playersToSend, targetClusterName, targetGroupName, targetServerName);
    }

    private Optional<GGroup> findGroupByName(String groupName) {
        for (GCluster cluster : clusterSystemAPI.getClusters().values()) {
            for (GGroup group : cluster.getClusterGroups().values()) {
                if (group.getGroupName().equals(groupName)) {
                    return Optional.of(group);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<GServer> findServerById(int serverId) {
        for (GCluster cluster : clusterSystemAPI.getClusters().values()) {
            for (GGroup group : cluster.getClusterGroups().values()) {
                if (group.getGroupServers().containsKey(serverId)) {
                    return Optional.of(group.getGroupServers().get(serverId));
                }
            }
        }
        return Optional.empty();
    }

    private void sendPlayer(CommandSource source, Player player, String targetClusterName, String targetGroupName, String targetServerName) {
        Optional<GCluster> targetClusterOpt = clusterSystemAPI.getCluster(targetClusterName);
        if (targetClusterOpt.isEmpty()) {
            source.sendMessage(miniMessage.deserialize("<red>Target cluster not found.</red>"));
            return;
        }

        GCluster targetCluster = targetClusterOpt.get();

        if (targetServerName != null && targetGroupName != null) {
            // Send to specific server in group
            int serverIdTarget;
            try {
                serverIdTarget = Integer.parseInt(targetServerName);
            } catch (NumberFormatException e) {
                source.sendMessage(miniMessage.deserialize("<red>Invalid target server ID format.</red>"));
                return;
            }

            Optional<GGroup> targetGroup = Optional.ofNullable(targetCluster.getClusterGroups().get(targetGroupName));
            if (targetGroup.isEmpty()) {
                source.sendMessage(miniMessage.deserialize("<red>Target group not found.</red>"));
                return;
            }

            Optional<GServer> targetServer = Optional.ofNullable(targetGroup.get().getGroupServers().get(serverIdTarget));
            if (targetServer.isEmpty()) {
                source.sendMessage(miniMessage.deserialize("<red>Target server not found.</red>"));
                return;
            }

            senderAPI.send(targetServer.get(), player);
            source.sendMessage(miniMessage.deserialize("<green>Sending " + player.getUsername() +
                    " to server " + serverIdTarget + " in group " + targetGroupName + "</green>"));
        } else if (targetGroupName != null) {
            // Send to group
            senderAPI.send(SenderAPI.TargetType.GROUP, targetGroupName, player);
            source.sendMessage(miniMessage.deserialize("<green>Sending " + player.getUsername() +
                    " to group " + targetGroupName + "</green>"));
        } else {
            // Send to cluster
            senderAPI.send(targetCluster, player);
            source.sendMessage(miniMessage.deserialize("<green>Sending " + player.getUsername() +
                    " to cluster " + targetClusterName + "</green>"));
        }
    }

    private void sendMultiplePlayers(CommandSource source, List<Player> players, String targetClusterName,
                                     String targetGroupName, String targetServerName) {
        if (players.isEmpty()) {
            source.sendMessage(miniMessage.deserialize("<red>No players found to send.</red>"));
            return;
        }

        for (Player player : players) {
            sendPlayer(source, player, targetClusterName, targetGroupName, targetServerName);
        }

        source.sendMessage(miniMessage.deserialize("<green>Sending " + players.size() +
                " players to the destination.</green>"));
    }

    private List<Player> getPlayersInCluster(GCluster cluster) {
        return glider.getProxy().getAllPlayers().stream()
                .filter(player -> clusterSystemAPI.getCluster(player)
                        .map(c -> c.equals(cluster))
                        .orElse(false))
                .collect(Collectors.toList());
    }

    private List<Player> getPlayersInGroup(GGroup group) {
        return glider.getProxy().getAllPlayers().stream()
                .filter(player -> clusterSystemAPI.getGroup(player)
                        .map(g -> g.equals(group))
                        .orElse(false))
                .collect(Collectors.toList());
    }

    private List<Player> getPlayersInServer(GServer server) {
        return glider.getProxy().getAllPlayers().stream()
                .filter(player -> clusterSystemAPI.getServer(player)
                        .map(s -> s.equals(server))
                        .orElse(false))
                .collect(Collectors.toList());
    }
}