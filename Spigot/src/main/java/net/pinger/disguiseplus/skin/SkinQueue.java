package net.pinger.disguiseplus.skin;

import net.pinger.common.lang.Maps;

import java.util.Map;

public class SkinQueue {

    private static final Map<String, Skin> skins = Maps.newConcurrentHashMap();

    /**
     * Returns a skin from the given url.
     *
     * @param url the url of the skin
     * @return the skin
     */

    public static Skin getSkinFromUrl(String url) {
        return skins.get(url);
    }

    /**
     * This method stores the url and the skin which was obtained by this url.
     *
     * @param url the url
     * @param skin the skin
     * @return the skin
     */

    public static Skin storeUrl(String url, Skin skin) {
        return skins.putIfAbsent(url, skin);
    }

}
