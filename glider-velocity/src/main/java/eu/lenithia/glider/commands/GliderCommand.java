package eu.lenithia.glider.commands;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.commands.subcommands.SendSubcommand;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.Command;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.incendo.cloud.velocity.CloudInjectionModule;
import org.incendo.cloud.velocity.VelocityCommandManager;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@Getter
public class GliderCommand {

    private final GliderVelocity glider;
    private final VelocityCommandManager<CommandSource> commandManager;
    private final Command.Builder<CommandSource> mainCommandBuilder;
    private final List<GliderSubcommandProvider> subcommandProviders = new ArrayList<>();

    public GliderCommand(GliderVelocity glider) {
        this.glider = glider;


        Injector childInjector = glider.getInjector().createChildInjector(
                new CloudInjectionModule<>(
                        CommandSource.class,
                        ExecutionCoordinator.simpleCoordinator(),
                        SenderMapper.identity()
                )
        );

        commandManager = childInjector.getInstance(
                Key.get(new TypeLiteral<VelocityCommandManager<CommandSource>>() {})
        );

        MinecraftExceptionHandler.<CommandSource>createNative()
                .defaultHandlers()
                .decorator(component -> Component.text()
                        .append(miniMessage().deserialize(getGlider().getMessages().getString("prefix", "<#ea93ed>glider</#ea93ed> <gray>» ")))
                        .append(component)
                        .build())
                .registerTo(commandManager);


        List<String> aliasList = glider.getConfig().getStringList("command-aliases.glider");
        if (aliasList == null) {
            aliasList = new ArrayList<>();
        }
        String[] aliases = aliasList.toArray(new String[0]);

        mainCommandBuilder = commandManager.commandBuilder("glider", aliases);

        registerDefaultCommands();

        commandManager.command(
                mainCommandBuilder
                    .permission("glider.command.main")
                    .handler(context -> {
                        StringBuilder message = new StringBuilder();
                        message.append("<strikethrough><white>                                                                                <reset><newline>");
                        message.append("<#ea93ed><bold>                           glider </bold><reset><newline>");
                        message.append("<strikethrough><white>                                                                                <reset><newline>");
                        message.append("<reset><newline>");
                        message.append("<white> running <#ea93ed>v").append(glider.getPluginDescription().getVersion().orElse("unknown")).append("<white> by <#ea93ed>Len_137 <reset><newline>");
                        message.append("<reset><newline>");
                        message.append(" <bold><hover:show_text:'<yellow>Click for help</yellow>'><click:run_command:'/glider help'><#ea93ed>help</#ea93ed></click></hover> <hover:show_text:'<yellow>Click for status</yellow>'><click:run_command:'/glider status'><#ea93ed>status</#ea93ed></click></hover>    <reset><newline>");
                        //message.append("<reset><newline>");

                        context.sender().sendMessage(
                                miniMessage().deserialize(message.toString())
                        );
                    })
        );

        glider.getLogger().info("Glider command loaded with aliases {} ", String.join(", ", aliases));

    }

    private void registerDefaultCommands() {
        // Register built-in subcommands
        List<String> aliasListGSend = glider.getConfig().getStringList("command-aliases.gsend");
        if (aliasListGSend == null) {
            aliasListGSend = new ArrayList<>();
        }
        String[] aliasesGSend = aliasListGSend.toArray(new String[0]);
        registerAsDual(new SendSubcommand(glider), "gsend", aliasesGSend);
    }

    /**
     * Register a new subcommand provider to the main Glider command
     * @param provider The subcommand provider to register
     */
    public void registerSubcommandProvider(GliderSubcommandProvider provider) {
        subcommandProviders.add(provider);
        provider.registerToGlider(this);
    }

    /**
     * Registers a command as standalone only (not as a subcommand)
     * @param provider The standalone command provider
     * @param commandName The name for the standalone command
     * @param aliases Optional aliases for the standalone command
     */
    public void registerStandalone(StandaloneCommandProvider provider, String commandName, String... aliases) {
        Command.Builder<CommandSource> standaloneBuilder = commandManager.commandBuilder(commandName, aliases);
        provider.registerStandalone(this, standaloneBuilder);
    }

    /**
     * Interface for subcommand providers
     */
    public interface GliderSubcommandProvider {
        void registerToGlider(GliderCommand commandsManager);
    }

    /**
     * Interface for standalone command providers
     */
    public interface StandaloneCommandProvider {
        void registerStandalone(GliderCommand manager, Command.Builder<CommandSource> builder);
    }


    /**
     * Registers a command provider as both a subcommand and a standalone command
     * @param provider The subcommand provider to register
     * @param commandName The name for the standalone command
     * @param aliases Optional aliases for the standalone command
     */
    public void registerAsDual(GliderSubcommandProvider provider, String commandName, String... aliases) {
        // Register as subcommand
        registerSubcommandProvider(provider);

        // Register as standalone command
        if (provider instanceof DualCommandProvider dualProvider) {
            Command.Builder<CommandSource> standaloneBuilder = commandManager.commandBuilder(commandName, aliases);
            dualProvider.registerStandalone(this, standaloneBuilder);
        }
    }

    /**
     * Interface for command providers that can work as both subcommands and standalone commands
     */
    public interface DualCommandProvider extends GliderSubcommandProvider {
        void registerStandalone(GliderCommand manager, Command.Builder<CommandSource> builder);
    }

}