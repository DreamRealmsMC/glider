package eu.lenithia.glider.commands.parsers;

import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GCluster;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

public class ClusterParser<C> implements ArgumentParser<C, GCluster>, BlockingSuggestionProvider<C> {

    private final GliderVelocity glider;

    public ClusterParser(GliderVelocity glider) {
        this.glider = glider;
    }

    public static <C> @NonNull ParserDescriptor<C, GCluster> clusterParser(GliderVelocity glider) {
        return ParserDescriptor.of(new ClusterParser(glider), GCluster.class);
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull GCluster> parse(
            @NonNull CommandContext<@NonNull C> commandContext,
            @NonNull CommandInput commandInput) {

        final String input = commandInput.readString();

        // Look up the cluster by name
        GCluster cluster = glider.getClusterSystem().getClusters().get(input);

        if (cluster == null) {
            return ArgumentParseResult.failure(
                    new IllegalArgumentException("No cluster found with name '" + input + "'"));
        }

        return ArgumentParseResult.success(cluster);
    }

    @Override
    public @NonNull Iterable<? extends @NonNull Suggestion> suggestions(
            @NonNull CommandContext<C> context,
            @NonNull CommandInput input) {

        final String currentInput = input.peekString().toLowerCase();

        return glider.getClusterSystem().getClusters().keySet().stream()
                .filter(name -> name.toLowerCase().startsWith(currentInput))
                .map(Suggestion::suggestion)
                .toList();
    }



}
