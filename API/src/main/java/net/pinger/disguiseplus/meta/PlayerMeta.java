package net.pinger.disguiseplus.meta;

import net.pinger.disguiseplus.user.User;

public abstract class PlayerMeta {

    protected final User user;

    protected PlayerMeta(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
