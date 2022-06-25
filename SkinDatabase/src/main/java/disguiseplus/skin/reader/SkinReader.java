package disguiseplus.skin.reader;

import com.google.gson.JsonObject;
import disguiseplus.skin.Skin;
import disguiseplus.util.HttpUtil;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpGetRequest;
import disguiseplus.SkinDatabase;

import java.io.IOException;
import java.util.UUID;

public class SkinReader {

    /**
     * Reads and creates a new {@link Skin} object from a given {@link UUID}.
     *
     * @param uuid the uuid of the player
     * @return the new skin
     * @throws IOException if an error was thrown while reading from the url
     */

    public static Skin read(UUID uuid) throws IOException {
        // Create a new http request
        HttpGetRequest req = new HttpGetRequest(HttpUtil.getNewSession(uuid));
        HttpResponse connect = req.connect();

        // First edge case
        if (connect.getCode() != 200) {
            return null;
        }

        // Edge case
        if (connect.getResponse().isEmpty() || connect.getResponse() == null) {
            return null;
        }

        // Convert to a JsonObject
        JsonObject obj = SkinDatabase.GSON.fromJson(connect.getResponse(), JsonObject.class);

        // The properties object inside the base JsonObject
        JsonObject properties = obj.getAsJsonArray("properties").get(0).getAsJsonObject();

        return new Skin(uuid,
                obj.get("name").getAsString(),
                properties.get("value").getAsString(),
                properties.get("signature").getAsString());
    }

    /**
     * Returns a new {@link Skin} based on the returned data from the mineskin request.
     *
     * @param object the json object
     * @return a new skin
     */

    public static Skin getSkinFromMineskin(JsonObject object) {
        return new Skin(null, null,
                object.get("value").getAsString(),
                object.get("signature").getAsString()
        );
    }

}
