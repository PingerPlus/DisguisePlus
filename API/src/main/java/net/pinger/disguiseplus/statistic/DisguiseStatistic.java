package net.pinger.disguiseplus.statistic;

import net.pinger.disguise.Skin;

public class DisguiseStatistic extends Statistic {

    private final Skin skin;
    private final String nickname;

    public DisguiseStatistic(Skin skin, String nickname) {
        this.skin = skin;
        this.nickname = nickname;
    }

    public Skin getSkin() {
        return skin;
    }

    public String getNickname() {
        return nickname;
    }
}
