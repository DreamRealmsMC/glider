package eu.lenithia.glider.commands.parsers;

import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import eu.lenithia.glider.clusters.system.GGroup;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;

import java.util.List;

public class GroupParser<C> implements ArgumentParser<C, GGroup>, BlockingSuggestionProvider<C> {

    private final GliderVelocity glider;
    private final String clusterArgumentName;

    public GroupParser(GliderVelocity glider, String clusterArgumentName) {
        this.glider = glider;
        this.clusterArgumentName = clusterArgumentName;
    }

    public static <C> @NonNull ParserDescriptor<C, GGroup> groupParser(
            GliderVelocity glider,
            String clusterArgumentName) {
        return ParserDescriptor.of(new GroupParser<>(glider, clusterArgumentName), GGroup.class);
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull GGroup> parse(
            @NonNull CommandContext<@NonNull C> commandContext,
            @NonNull CommandInput commandInput) {

        final String input = commandInput.readString();

        GCluster cluster = commandContext.get(clusterArgumentName);
        if (cluster == null) {
            return ArgumentParseResult.failure(
                    new IllegalArgumentException("Target cluster not available for group lookup"));
        }

        GGroup group = cluster.getClusterGroups().get(input);

        if (group == null) {
            return ArgumentParseResult.failure(
                    new IllegalArgumentException("No group found with name '" + input + "' in cluster '" +
                            cluster.getClusterName() + "'"));
        }

        return ArgumentParseResult.success(group);
    }

    @Override
    public @NonNull Iterable<? extends @NonNull Suggestion> suggestions(
            @NonNull CommandContext<C> context,
            @NonNull CommandInput input) {

        final String currentInput = input.peekString().toLowerCase();


        GCluster cluster = context.get(clusterArgumentName);
        if (cluster == null) {
            return List.of();
        }

        return cluster.getClusterGroups().keySet().stream()
                .filter(name -> name.toLowerCase().startsWith(currentInput))
                .map(Suggestion::suggestion)
                .toList();
    }
}