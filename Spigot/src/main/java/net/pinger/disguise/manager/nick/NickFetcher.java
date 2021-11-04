package net.pinger.disguise.manager.nick;

import com.google.gson.JsonObject;
import net.pinger.common.http.HttpRequest;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpGetRequest;
import net.pinger.disguise.exceptions.InvalidUserException;
import net.pinger.disguise.utils.ConverterUtil;
import net.pinger.disguise.utils.HttpUtil;
import net.pinger.disguise.utils.ReferenceUtil;
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
                throw new InvalidUserException();

            // Response
            JsonObject object = ReferenceUtil.GSON.fromJson(response.getResponse(), JsonObject.class);

            if (object.has("id"))
                return ConverterUtil.fromString(object.get("id").getAsString());

        } catch (IOException e) {
            logger.error("Failed to load the uuid of the player -> " + name);
            logger.error(e.getMessage());
        }

        return null;
    }

}