package net.pinger.disguiseplus.statistic;

public class NickStatistic extends Statistic {

    private final String nickname;

    public NickStatistic(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
