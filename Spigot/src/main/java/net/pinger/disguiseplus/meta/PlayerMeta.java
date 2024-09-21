package net.pinger.disguiseplus.meta;

import java.time.LocalDateTime;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.DisguisePlayer;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.user.DisguiseUser;

public class PlayerMeta {
    private final DisguiseUser user;
    private final Skin skin;
    private final Rank rank;
    private final String name;
    private final LocalDateTime startTime;

    private LocalDateTime endTime;

    public PlayerMeta(Builder builder) {
        this.user = builder.user;
        this.skin = builder.skin;
        this.rank = builder.rank;
        this.name = builder.name;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static Builder builder(DisguiseUser user) {
        return new Builder(user);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public DisguiseUser getUser() {
        return this.user;
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
        private final DisguiseUser user;

        private Skin skin;
        private Rank rank;
        private String name;
        private LocalDateTime endTime;
        private LocalDateTime startTime = LocalDateTime.now();

        public Builder(DisguiseUser user) {
            this.user = user;

            final DisguisePlayer player = DisguiseAPI.getDisguisePlayer(user.getId());
            this.skin = player.getDefaultSkin();
            this.name = player.getDefaultName();
        }

        public static Builder copyOf(PlayerMeta meta) {
            return new Builder(meta.getUser())
                .skin(meta.skin)
                .rank(meta.rank)
                .name(meta.name);
        }

        public Builder skin(Skin skin) {
            this.skin = skin;
            return this;
        }

        public Builder rank(Rank rank) {
            this.rank = rank;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public PlayerMeta build() {
            return new PlayerMeta(this);
        }

    }
}
