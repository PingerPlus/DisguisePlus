package net.pinger.disguise.nick.flag;

public enum NickFlag {

    CONSONANT('A'), VOWEL('a'), RANDOM('R');

    private final char key;

    NickFlag(char key) {
        this.key = key;
    }

    public static NickFlag getByKey(char key) {
        for (NickFlag flag : values()) {
            if (flag.key == key)
                return flag;
        }

        return null;
    }
}
