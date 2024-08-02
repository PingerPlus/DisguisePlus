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

    public PlayerMeta(DisguiseUser user, Skin skin, Rank rank, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.user = user;
        this.skin = skin;
        this.rank = rank;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
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
        private final DisguisePlayer player;

        private Skin skin;
        private Rank rank;
        private String name;
        private LocalDateTime endTime;

        public Builder(DisguiseUser user) {
            this.user = user;
            this.player = DisguiseAPI.getDisguisePlayer(user.getId());
            this.skin = this.player.getDefaultSkin();
            this.name = this.player.getDefaultName();
        }

        public static Builder copyOf(PlayerMeta meta) {
            return new Builder(meta.getUser())
                .setSkin(meta.skin)
                .setRank(meta.rank)
                .setName(meta.name);
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
                this.user,
                this.skin,
                this.rank,
                this.name,
                LocalDateTime.now(),
                this.endTime
            );
        }

    }
}
