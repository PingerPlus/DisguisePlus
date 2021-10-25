package net.pinger.disguise.utils;

import com.google.gson.JsonObject;
import net.pinger.disguise.skin.SimpleSkin;
import net.pinger.disguise.skin.Skin;

public class SkinUtil {

    /**
     * Returns a new {@link Skin} from the specified json object.
     * This method is used when catching skins from {@link HttpUtil#CLOUD_URL}.
     *
     * @param object the json object
     * @return a new skin object
     */

    public static Skin getFromJson(JsonObject object) {
        // Get the properties object from the base
        JsonObject properties = object.getAsJsonObject("properties");

        // Return the new Skin
        return SimpleSkin.newBuilder(
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
        return SimpleSkin.newBuilder(
                object.get("value").getAsString(),
                object.get("signature").getAsString()
        );
    }

    /**
     * Returns a new skin from a valid mojang server response.
     *
     * @param object the response
     * @return a new skin
     */

    public static Skin getFromMojang(JsonObject object) {
        JsonObject obj = object.getAsJsonArray("properties").get(0)
                .getAsJsonObject();

        return SimpleSkin.newBuilder(
                obj.get("value").getAsString(),
                obj.get("signature").getAsString());
    }

}
