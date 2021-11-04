package net.pinger.disguise.nick.flag;

public enum NickFlag {

    CONSONANT('A', "Consonant"), VOWEL('a', "Vowel"), RANDOM('R', "Either consonant or vowel");

    private final char key;
    private final String description;

    NickFlag(char key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public char getKey() {
        return key;
    }

    public static NickFlag getByKey(char key) {
        for (NickFlag flag : values()) {
            if (flag.key == key)
                return flag;
        }

        return null;
    }
}
