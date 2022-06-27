package net.pinger.disguiseplus.utils;

import org.bukkit.ChatColor;

public class StringUtil {

    /**
     * Checks if a this string contains chat colors.
     *
     * @param s the colors
     * @return if it contains colors
     */

    public static boolean containsColor(String s) {
        return !ChatColor.stripColor(s).equalsIgnoreCase(s);
    }

    public static String value(String x) {
        if (x != null && !x.isEmpty())
            return x;

        return "Not set";
    }
}
