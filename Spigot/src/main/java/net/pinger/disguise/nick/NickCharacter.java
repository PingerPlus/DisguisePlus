package net.pinger.disguise.nick;

import net.pinger.common.lang.Strings;
import net.pinger.disguise.nick.flag.NickFlag;

import java.util.Random;

public class NickCharacter {

    private final String target;
    private boolean optional = true;
    private final NickFlag flag;

    public NickCharacter(NickFlag flag, boolean optional) {
        this.flag = flag;

        if (flag == NickFlag.VOWEL)
            target = Strings.VOWELS_LOWERCASE;
        else if (flag == NickFlag.CONSONANT)
            target = Strings.CONSONANTS_LOWERCASE;
        else
            target = new Random().nextInt(2) == 0 ?
                    Strings.VOWELS_LOWERCASE :
                    Strings.CONSONANTS_LOWERCASE;

        this.optional = optional;
    }

    public NickCharacter(char c, boolean optional) {
        this(NickFlag.getByKey(c), optional);
    }

    public NickFlag getFlag() {
        return flag;
    }

    public Character generate() {
        return this.target.charAt(new Random().nextInt(this.target.length()));
    }

    public boolean isOptional() {
        return this.optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
