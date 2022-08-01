package net.pinger.disguiseplus.utils;

import com.google.gson.JsonObject;
import net.pinger.disguise.Skin;

public class SkinUtil {

    /**
     * Returns a new {@link Skin} from the specified json object.
     * This method is used when catching skins.
     *
     * @param object the json object
     * @return a new skin object
     */

    public static Skin getFromJson(JsonObject object) {
        // Get the properties object from the base
        JsonObject properties = object.getAsJsonObject("properties");

        // Return the new Skin
        return new Skin(
                properties.get("value").getAsString(),
                properties.get("signature").getAsString());
    }

}
