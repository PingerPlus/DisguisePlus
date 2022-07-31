package net.pinger.disguiseplus.utils;

import net.pinger.disguiseplus.SkinPack;

public class HttpUtil {

    /**
     * The link to retrieve a uuid of a player
     */

    private static final String MOJANG_NAME_URL = "https://api.mojang.com/users/profiles/minecraft/%s";

    /**
     * This link refers to the skin pack categories url
     */

    public static final String CATEGORY_URL = "https://raw.githubusercontent.com/ITSPINGER/Skins/master/SkinPacks/categories.json";

    /**
     * The base url of the skinspacks
     */

    private static final String SKINPACK = "https://raw.githubusercontent.com/ITSPINGER/Skins/master/SkinPacks/%s/%s/data.json";

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
     * This method creates a URL link for a specific {@link SkinPack}.
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
