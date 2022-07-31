package net.pinger.disguiseplus.internal;

import com.google.gson.JsonObject;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.http.HttpRequest;
import net.pinger.disguise.http.HttpResponse;
import net.pinger.disguise.http.request.HttpGetRequest;
import net.pinger.disguiseplus.PlayerMatcher;
import net.pinger.disguiseplus.utils.ConverterUtil;
import net.pinger.disguiseplus.utils.HttpUtil;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class PlayerMatcherImpl implements PlayerMatcher {

    private UUID getIdFromName(String name) {
        try {
            // Create a request
            HttpRequest request = new HttpGetRequest(HttpUtil.toMojangUrl(name));
            HttpResponse response = request.connect();

            // If the response is null
            // Then the player is invalid
            if (response.getResponse() == null || response.getResponse().isEmpty()) {
                return null;
            }

            // Read as json
            JsonObject object = DisguiseAPI.GSON.fromJson(response.getResponse(), JsonObject.class);

            // This will never return null
            // Since the UUID is valid
            return ConverterUtil.fromString(object.get("id").getAsString());
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean matches(Player player) {
        // Try to load an id this players name
        UUID id = this.getIdFromName(player.getName());

        // If the id is null
        // Then either an error occurred
        // Or the server is running in offline
        return id != null && id.equals(player.getUniqueId());
    }

}
