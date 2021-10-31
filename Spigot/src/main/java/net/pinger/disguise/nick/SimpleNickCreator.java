package net.pinger.disguise.nick;

import net.pinger.common.lang.Strings;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SimpleNickCreator {

    private static final Random RANDOM = new Random();

    // The pattern of the builder
    private String pattern;

    public SimpleNickCreator(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Checks if a specific pattern is valid for use.
     *
     * @param pattern the pattern
     * @return is the pattern is valid
     */

    public static boolean isValid(String pattern) {
        // Checks if the string had found the | character
        boolean foundOptional = false;

        int count = 0;
        int index = 0;

        // Loop through each character
        for (char c : pattern.toCharArray()) {
            if (c != 'a' && c != 'A' && c != '?' && c != '|') {
                return false;
            }

            if (c != '|') {
                ++count;
            } else {
                // Checks if the optional pattern was found, but it isn't the last character
                // Makes sure that at least one character is optional
                if (!foundOptional && index <= pattern.length() - 3)
                    foundOptional = true;
                else
                    // If it's closed before the end
                    if (foundOptional && c != pattern.charAt(pattern.length() - 1))
                        return false;
            }

            ++index;
        }

        // Check if optional was started but not finished
        if (foundOptional && pattern.charAt(pattern.length() - 1) != '|')
            return false;

        return count >= 4 && count <= 16;
    }

    public void setPattern(String pattern) {
        if (!SimpleNickCreator.isValid(pattern)) {
            return;
        }

        this.pattern = pattern;
    }

    /**
     * This method creates a random nick out of the specified pattern.
     *
     * @return the nick
     */

    public String createNick() {
        StringBuilder builder = new StringBuilder();

        // Getting the optional characters
        List<Character> optionals = new LinkedList<>();
        boolean foundOptional = false;

        for (int i = 0; i < this.pattern.length(); i++) {
            // The char
            Character c = this.pattern.charAt(i);

            if (c == '|' && !foundOptional) {
                foundOptional = true;
            }

            if (foundOptional && c != '|') {
                optionals.add(this.getCharacterFrom(c));
            }

            if (c != '|' && !foundOptional) {
                builder.append(
                        i == 0 ?
                                String.valueOf(getCharacterFrom(c)).toUpperCase() :
                                getCharacterFrom(c)
                );
            }
        }

        int size = RANDOM.nextInt(optionals.size()) + 1;

        // Adding the optional chars
        for (int i = 0; i < size; i++) {
            builder.append(optionals.get(i));
        }

        return builder.toString();
    }

    private Character getCharacterFrom(Character other) {
        if (other == 'A')
            return Strings.CONSONANTS_LOWERCASE.charAt(RANDOM.nextInt(21));

        if (other == 'a')
            return Strings.VOWELS_LOWERCASE.charAt(RANDOM.nextInt(5));

        // Checking if the random is 0 or 1
        if (RANDOM.nextInt(1) == 1)
            return getCharacterFrom('a');

        return getCharacterFrom('A');
    }
}
