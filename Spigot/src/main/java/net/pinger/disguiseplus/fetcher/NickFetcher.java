package net.pinger.disguiseplus.fetcher;

import com.google.gson.JsonObject;
import net.pinger.common.http.HttpRequest;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpGetRequest;
import net.pinger.disguiseplus.utils.ConverterUtil;
import net.pinger.disguiseplus.utils.HttpUtil;
import net.pinger.disguiseplus.utils.ReferenceUtil;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class NickFetcher {

    private static final Logger logger = LoggerFactory.getLogger("NickFetcher");

    public static UUID retrieveUUID(String name) {
        try {
            HttpRequest request = new HttpGetRequest(HttpUtil.toMojangUrl(name));
            HttpResponse response = request.connect();

            if (response.getCode() == 404)
                return null;

            // Response
            JsonObject object = ReferenceUtil.GSON.fromJson(response.getResponse(), JsonObject.class);

            if (object == null || object.isJsonNull()) {
                return null;
            }

            if (object.has("id"))
                return ConverterUtil.fromString(object.get("id").getAsString());

        } catch (IOException e) {
            logger.error("Failed to load the uuid of the player -> " + name);
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * This method checks if the player's
     * uuid matches the name of the player.
     *
     * @param p the player
     * @return whether the uuid matches the name
     */

    public static boolean matches(Player p) {
        UUID id = retrieveUUID(p.getName());

        if (id == null)
            return false;

        return id.equals(p.getUniqueId());
    }

}
