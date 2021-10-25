package net.pinger.disguise.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class HttpUtil {

    /**
     * The cloud url where the skins are downloaded from
     */

    public static final String CLOUD_URL = "https://raw.githubusercontent.com/ITSPINGER/DisguisePlus/master/SkinDatabase/src/main/resources/skins.json";

    /**
     * The link to the mojang servers where the skin information is downloaded from
     */

    private static final String MOJANG_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    /**
     * The link to retrieve a uuid of a player
     */

    private static final String MOJANG_NAME_URL = "https://api.mojang.com/users/profiles/minecraft/%s";

    /**
     * This link refers to the skin pack categories url
     */

    public static final String CATEGORY_URL = "https://raw.githubusercontent.com/ITSPINGER/DisguisePlus/master/SkinPacks/categories.json";

    /**
     * The base url of the skinspacks
     */

    private static final String SKINPACK = "https://raw.githubusercontent.com/ITSPINGER/DisguisePlus/master/SkinPacks/%s/%s/data.json";

    /**
     * This field represents the value of the mineskin app
     */

    private final static String MINESKIN_URL = "https://api.mineskin.org/generate/url?url=%s";

    /**
     * Retunrs a new string which represents a formatted url pointing to the uuid of this player
     * within the mojang servers.
     *
     * @param s the name of the player
     * @return the url
     */

    public static String toMojangUrl(String s) {
        return String.format(MOJANG_NAME_URL, s);
    }

    /**
     * Returns a {@link String} which represents a formatted url to the mojang servers.
     * It specifically formats the {@link HttpUtil#MOJANG_URL} with the specified uuid.
     *
     * @param uuid the uuid of the player
     * @return the formatted string
     */

    public static String toMojangUrl(UUID uuid) {
        return String.format(MOJANG_URL, uuid);
    }

    /**
     * This method creates a URL link for a specific {@link net.pinger.disguise.skin.SkinPack}.
     *
     * @param category the category of the pack
     * @param name the name of the pack
     * @return the raw github url of the pack
     */

    public static String toSkinPack(String category, String name) {
        return String.format(SKINPACK, category, name).replace(" ", "%20");
    }

    /**
     * This method returns a new mineskin url used in for requesting new skins.
     *
     * @param url the url of the image
     * @return the url of the request
     */

    public static String toMineskin(String url) {
        return String.format(MINESKIN_URL, url);
    }


}
