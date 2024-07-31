package net.pinger.disguiseplus.meta;

import java.time.LocalDateTime;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.rank.Rank;

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

    public static class Builder {
        private Skin skin;
        private Rank rank;
        private String name;
        private LocalDateTime endTime;

        public static Builder copyOf(PlayerMeta meta) {
            return new Builder()
                .setSkin(meta.skin)
                .setRank(meta.rank)
                .setName(meta.name)
                .setEndTime(meta.endTime);
        }

        public Builder setSkin(Skin skin) {
            this.skin = skin;
            return this;
        }

        public Builder setRank(Rank rank) {
            this.rank = rank;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public PlayerMeta build() {
            return new PlayerMeta(
                this.skin,
                this.rank,
                this.name,
                LocalDateTime.now(),
                this.endTime
            );
        }

    }
}
