package net.pinger.disguiseplus.utils;

import org.bukkit.ChatColor;

import java.security.SecureRandom;
import java.util.Random;

public class StringUtil {

    private static final Random RANDOM = new SecureRandom();
    private static final String CONSONANTS = "BCDFGHJKLMNPQRSTUVWXYZ";
    private static final String VOWELS = "AEIOU";

    /**
     * Checks if this string contains chat colors.
     *
     * @param s the colors
     * @return if it contains colors
     */

    public static boolean containsColor(String s) {
        return !ChatColor.stripColor(s).equalsIgnoreCase(s);
    }

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
        char firstCharacter = CONSONANTS.charAt(RANDOM.nextInt(21));

        // Add the consonant as the first
        builder.append(Character.toUpperCase(firstCharacter));

        // Add to the builder depending on the i in the loop
        for (int i = 1; i < length; i++) {
            builder.append(
                    i % 2 == 0 ?
                            CONSONANTS.charAt(RANDOM.nextInt(21)) :
                            VOWELS.charAt(RANDOM.nextInt(5))
            );
        }

        // Build the string at the end
        return builder.toString();
    }


    public static String value(String x) {
        if (x != null && !x.isEmpty())
            return x;

        return "Not set";
    }
}
