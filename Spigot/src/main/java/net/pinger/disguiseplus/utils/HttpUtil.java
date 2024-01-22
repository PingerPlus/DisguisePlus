package net.pinger.disguiseplus.utils;

import net.pinger.disguiseplus.skin.SkinPack;

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
     * The base url of the skin packs
     */

    private static final String SKINPACK = "https://raw.githubusercontent.com/ITSPINGER/Skins/master/SkinPacks/%s/%s/data.json";

    /**
     * Returns a new string which represents a formatted url pointing to the uuid of this player
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
}
