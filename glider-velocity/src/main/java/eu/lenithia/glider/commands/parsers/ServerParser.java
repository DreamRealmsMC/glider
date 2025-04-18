package eu.lenithia.glider.commands.parsers;

import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GGroup;
import eu.lenithia.glider.clusters.system.GServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;

import java.util.List;

public class ServerParser<C> implements ArgumentParser<C, GServer>, BlockingSuggestionProvider<C> {

    private final GliderVelocity glider;
    private final String groupArgumentName;

    public ServerParser(GliderVelocity glider, String groupArgumentName) {
        this.glider = glider;
        this.groupArgumentName = groupArgumentName;
    }

    public static <C> @NonNull ParserDescriptor<C, GServer> serverParser(
            GliderVelocity glider,
            String groupArgumentName) {
        return ParserDescriptor.of(new ServerParser<>(glider, groupArgumentName), GServer.class);
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull GServer> parse(
            @NonNull CommandContext<@NonNull C> commandContext,
            @NonNull CommandInput commandInput) {

        final String input = commandInput.readString();
        int serverId;

        try {
            serverId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return ArgumentParseResult.failure(
                    new IllegalArgumentException("Server ID must be a number, got '" + input + "'"));
        }

        GGroup group = commandContext.get(groupArgumentName);
        if (group == null) {
            return ArgumentParseResult.failure(
                    new IllegalArgumentException("Target group not available for server lookup"));
        }

        GServer server = group.getGroupServers().get(serverId);

        if (server == null) {
            return ArgumentParseResult.failure(
                    new IllegalArgumentException("No server found with ID '" + serverId + "' in group '" +
                            group.getGroupName() + "'"));
        }

        return ArgumentParseResult.success(server);
    }

    @Override
    public @NonNull Iterable<? extends @NonNull Suggestion> suggestions(
            @NonNull CommandContext<C> context,
            @NonNull CommandInput input) {

        final String currentInput = input.peekString().toLowerCase();


        GGroup group = context.get(groupArgumentName);
        if (group == null) {
            return List.of();
        }

        return group.getGroupServers().keySet().stream()
                .map(Object::toString)
                .filter(id -> id.startsWith(currentInput))
                .map(Suggestion::suggestion)
                .toList();
    }
}