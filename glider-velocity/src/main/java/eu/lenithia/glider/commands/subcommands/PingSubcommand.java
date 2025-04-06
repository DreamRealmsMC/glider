package eu.lenithia.glider.commands.subcommands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.commands.GliderCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;

public class PingSubcommand implements GliderCommand.DualCommandProvider {

    @Override
    public void registerToGlider(GliderCommand commandsManager) {
        commandsManager.getCommandManager().command(
                commandsManager.getMainCommandBuilder()
                        .literal("pingfromglider")
                        .handler(this::handlePingCommand)
        );
    }

    @Override
    public void registerStandalone(GliderCommand manager, Command.Builder<CommandSource> builder) {
        manager.getCommandManager().command(
                builder.handler(this::handlePingCommand)
        );
    }

    private void handlePingCommand(CommandContext<CommandSource> context) {
        CommandSource source = context.sender();

        if (source instanceof Player player) {
            // Get player ping (latency) and display it
            int pingMs = (int) player.getPing();

            Component message = Component.text()
                    .append(Component.text("Your ping: ", NamedTextColor.GOLD))
                    .append(Component.text(pingMs + "ms", getPingColor(pingMs)))
                    .build();

            source.sendMessage(message);
        } else {
            // Console or other non-player sender
            source.sendMessage(Component.text("Pong!", NamedTextColor.GREEN));
        }
    }

    private NamedTextColor getPingColor(int ping) {
        if (ping < 50) return NamedTextColor.GREEN;
        if (ping < 150) return NamedTextColor.YELLOW;
        if (ping < 300) return NamedTextColor.GOLD;
        return NamedTextColor.RED;
    }
}