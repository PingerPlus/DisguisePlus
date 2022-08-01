package net.pinger.disguiseplus.statistic;

import net.pinger.disguise.Skin;

public class SkinStatistic extends Statistic {

    private final Skin skin;

    public SkinStatistic(Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin() {
        return skin;
    }
}

