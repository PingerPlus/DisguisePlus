package net.pinger.disguiseplus.internal;

import java.time.LocalDateTime;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.user.User;

public class PlayerMeta {
    private final Skin skin;
    private final Rank rank;
    private final String name;
    private final LocalDateTime startTime;

    private LocalDateTime endTime;

    public PlayerMeta(Skin skin, Rank rank, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.skin = skin;
        this.rank = rank;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return this.name;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public Rank getRank() {
        return this.rank;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }
}
