package net.pinger.disguiseplus.utils;

import net.pinger.common.lang.Strings;
import org.bukkit.ChatColor;

import java.util.Random;

public class StringUtil {

    /**
     * A random object used to generate a string
     */

    private static final Random RANDOM = new Random();


    /**
     * Returns a random {@link String} with the length of 5 - 7 characters.
     * By calculations, the chances of this function returning the same value is 1 in 231 525 if the length is 5.
     *
     * @return the generized string
     */

    public static String randomize() {
        StringBuilder builder = new StringBuilder();

        // Length can be only of 5 - 7 characters
        final int length = RANDOM.nextInt(7 - 5) + 5;

        // Add the consonant as the first
        builder.append(Strings.CONSONANTS_UPPERCASE.charAt(RANDOM.nextInt(21)));

        // Add to the builder depending on the i in the loop
        for (int i = 1; i < length; i++) {
            builder.append(
                    i % 2 == 0 ?
                            Strings.CONSONANTS_LOWERCASE.charAt(RANDOM.nextInt(21))
                            : Strings.VOWELS_LOWERCASE.charAt(RANDOM.nextInt(5))
            );
        }

        // Build the string at the end
        return builder.toString();
    }

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
