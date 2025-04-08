package eu.lenithia.glider.sender;


import com.velocitypowered.api.proxy.Player;
import eu.lenithia.glider.clusters.system.GServer;
import eu.lenithia.glider.sender.objects.SenderResponse;
import net.kyori.adventure.text.Component;

/**
 * Interface for handling responses from the sender.
 */
public interface SenderResponseHandler {

    /**
     * Main handler method that routes to the appropriate response handler
     */
    default void handleResponse(Sender sender) {
        SenderResponse response = sender.getSenderResponse();

        switch (response) {
            case SUCCESS -> handleSuccess(sender);
            case UNAUTHORIZED -> handleUnauthorized(sender);
            case UNAVAILABLE -> handleUnavailable(sender);
            case CANCELED -> handleCanceled(sender);
            case EPIC_FAIL -> handleEpicFail(sender);
        }
    }

    /**
     * Handle successful transfer
     */
    default void handleSuccess(Sender sender) {
        GServer bestServer = sender.getBestServer();
        if (bestServer != null) {
            for (Player player : sender.getPlayers()) {
                player.createConnectionRequest(bestServer.getRegisteredServer()).connect();
                player.sendMessage(Component.text("Connected to " + bestServer.getRegisteredServer().getServerInfo().getName()));
            }
        }
    }

    /**
     * Handle unauthorized transfer attempt
     */
    default void handleUnauthorized(Sender sender) {
        for (Player player : sender.getPlayers()) {
            player.sendMessage(Component.text("You are not authorized to access this server"));
        }
    }

    /**
     * Handle unavailable server scenario
     */
    default void handleUnavailable(Sender sender) {
        for (Player player : sender.getPlayers()) {
            player.sendMessage(Component.text("No available servers to connect to"));
        }
    }

    /**
     * Handle canceled transfer
     */
    default void handleCanceled(Sender sender) {
        // Default empty implementation
        for (Player player : sender.getPlayers()) {
            player.sendMessage(Component.text("Your send was cancelled"));
        }
    }

    /**
     * Handle epic failure scenario
     */
    default void handleEpicFail(Sender sender) {
        for (Player player : sender.getPlayers()) {
            player.sendMessage(Component.text("An epic failure occurred"));
        }
    }
}
