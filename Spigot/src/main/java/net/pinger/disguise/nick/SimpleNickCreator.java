package net.pinger.disguise.nick;

import net.pinger.disguise.nick.flag.NickFlag;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SimpleNickCreator {

    private List<NickCharacter> flags = new LinkedList<>();

    public static SimpleNickCreator createFrom(String pattern) {
        SimpleNickCreator creator = new SimpleNickCreator();

        if (pattern.length() > 16)
            pattern = pattern.substring(0, 16);

        String xs = StringUtils.substringBetween(pattern, "|");

        char[] optionals = xs == null ?
                new char[0] :
                xs.toCharArray();

        char[] chars = xs != null ?
                pattern.substring(0, pattern.indexOf("|"))
                    .toCharArray() :
                pattern.toCharArray();

        for (char s : chars) {
            if (s != 'A' && s != 'a' && s != 'R')
                continue;

            creator.flags.add(new NickCharacter(s, false));
        }

        for (char s : optionals) {
            if (s != 'A' && s != 'a' && s != 'R')
                continue;

            creator.flags.add(new NickCharacter(s, true));
        }

        return creator;
    }

    public String createNick() {
        StringBuilder builder = new StringBuilder();

        List<NickCharacter> optionals =
                this.flags.stream().filter(NickCharacter::isOptional)
                .collect(Collectors.toList());

        List<NickCharacter> required =
                this.flags.stream().filter(n -> !n.isOptional())
                .collect(Collectors.toList());

        for (int i = 0; i < required.size(); i++) {
            builder.append(
                    i == 0 ?
                            required.get(i).generate().toString().toUpperCase() :
                            required.get(i).generate()
            );
        }

        if (optionals.size() == 0)
            return builder.toString();

        int random = new Random().nextInt(optionals.size());

        // Account for the optional characters aswell
        for (int i = 0; i < random; i++) {
            builder.append(optionals.get(i).generate());
        }

        return builder.toString();
    }

    /**
     * This method creates a default flag which then gets added to the flag list.
     * <p>
     * Ensures that the pattern is configured correctly, instead of letting the
     * player figuring out the pattern.
     *
     * @since 2.0
     */

    public void createDefaultFlag() {
        if (this.flags.size() == 16)
            return;

        boolean optional = this.flags.get(this.flags.size() - 1)
                .isOptional();

        this.flags.add(new NickCharacter(NickFlag.CONSONANT, optional));
    }

    public void saveToConfig(FileConfiguration cfg) {
        StringBuilder builder = new StringBuilder();

        boolean optional = false;

        for (NickCharacter character : this.flags) {
            if (character.isOptional() && !optional) {
                builder.append("|");
                optional = true;
            }

            builder.append(character.getFlag().getKey());
        }

        if (optional)
            builder.append("|");

        cfg.set("nick.pattern", builder.toString());
    }

    public List<NickCharacter> getFlags() {
        return flags;
    }

    //    private static final Random RANDOM = new Random();
//
//    // The pattern of the builder
//    private String pattern;
//
//    public SimpleNickCreator(String pattern) {
//        this.pattern = pattern;
//    }
//
//    /**
//     * Checks if a specific pattern is valid for use.
//     *
//     * @param pattern the pattern
//     * @return is the pattern is valid
//     */
//
//    public static boolean isValid(String pattern) {
//        // Checks if the string had found the | character
//        boolean foundOptional = false;
//
//        int count = 0;
//        int index = 0;
//
//        // Loop through each character
//        for (char c : pattern.toCharArray()) {
//            if (c != 'a' && c != 'A' && c != '?' && c != '|') {
//                return false;
//            }
//
//            if (c != '|') {
//                ++count;
//            } else {
//                // Checks if the optional pattern was found, but it isn't the last character
//                // Makes sure that at least one character is optional
//                if (!foundOptional && index <= pattern.length() - 3)
//                    foundOptional = true;
//                else
//                    // If it's closed before the end
//                    if (foundOptional && c != pattern.charAt(pattern.length() - 1))
//                        return false;
//            }
//
//            ++index;
//        }
//
//        // Check if optional was started but not finished
//        if (foundOptional && pattern.charAt(pattern.length() - 1) != '|')
//            return false;
//
//        return count >= 4 && count <= 16;
//    }
//
//    public void setPattern(String pattern) {
//        if (!SimpleNickCreator.isValid(pattern)) {
//            return;
//        }
//
//        this.pattern = pattern;
//    }
//
//    /**
//     * This method creates a random nick out of the specified pattern.
//     *
//     * @return the nick
//     */
//
//    public String createNick() {
//        StringBuilder builder = new StringBuilder();
//
//        // Getting the optional characters
//        List<Character> optionals = new LinkedList<>();
//        boolean foundOptional = false;
//
//        for (int i = 0; i < this.pattern.length(); i++) {
//            // The char
//            Character c = this.pattern.charAt(i);
//
//            if (c == '|' && !foundOptional) {
//                foundOptional = true;
//            }
//
//            if (foundOptional && c != '|') {
//                optionals.add(this.getCharacterFrom(c));
//            }
//
//            if (c != '|' && !foundOptional) {
//                builder.append(
//                        i == 0 ?
//                                String.valueOf(getCharacterFrom(c)).toUpperCase() :
//                                getCharacterFrom(c)
//                );
//            }
//        }
//
//        int size = RANDOM.nextInt(optionals.size()) + 1;
//
//        // Adding the optional chars
//        for (int i = 0; i < size; i++) {
//            builder.append(optionals.get(i));
//        }
//
//        return builder.toString();
//    }
//
//    private Character getCharacterFrom(Character other) {
//        if (other == 'A')
//            return Strings.CONSONANTS_LOWERCASE.charAt(RANDOM.nextInt(21));
//
//        if (other == 'a')
//            return Strings.VOWELS_LOWERCASE.charAt(RANDOM.nextInt(5));
//
//        // Checking if the random is 0 or 1
//        if (RANDOM.nextInt(1) == 1)
//            return getCharacterFrom('a');
//
//        return getCharacterFrom('A');
//    }
}
