package net.pinger.disguise.nick;

import net.pinger.common.lang.Strings;
import net.pinger.disguise.nick.flag.NickFlag;

import java.util.Random;

public class NickCharacter {

    private String target;
    private boolean optional = true;
    private NickFlag flag;

    NickCharacter(NickFlag flag, boolean optional) {
        this.setFlag(flag);
        this.optional = optional;
    }

    NickCharacter(char c, boolean optional) {
        this(NickFlag.getByKey(c), optional);
    }

    private void setFlag(NickFlag flag) {
        this.flag = flag;

        if (flag == NickFlag.VOWEL)
            target = Strings.VOWELS_LOWERCASE;
        else if (flag == NickFlag.CONSONANT)
            target = Strings.CONSONANTS_LOWERCASE;
        else
            target = new Random().nextInt(2) == 0 ?
                    Strings.VOWELS_LOWERCASE :
                    Strings.CONSONANTS_LOWERCASE;
    }

    public void nextFlag() {
        this.setFlag(NickFlag.values()[(this.flag.ordinal() + 1) % NickFlag.values().length]);
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
