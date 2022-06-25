package disguiseplus.util;

import java.util.UUID;

public class HttpUtil {

    /**
     * The url of the Mojang site which is used as an api
     */

    private static final String MOJANG_SESSION = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    /**
     * Gets a mojang session url from the given uuid.
     *
     * @param uuid the uuid of the player
     * @return the url
     */

    public static String getNewSession(UUID uuid) {
        return String.format(MOJANG_SESSION, uuid);
    }

}
