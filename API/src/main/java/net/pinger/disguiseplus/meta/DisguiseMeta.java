package net.pinger.disguiseplus.meta;

import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.user.User;

public class DisguiseMeta extends PlayerMeta {

    private final String name;
    private final Skin skin;

    protected DisguiseMeta(User user, String name, Skin skin) {
        super(user);
        this.name = name;
        this.skin = skin;
    }

    public String getName() {
        return this.name;
    }

    public Skin getSkin() {
        return this.skin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        DisguiseMeta meta = (DisguiseMeta) obj;
        return this.user.equals(meta.user) && this.name.equals(meta.name) && this.skin.equals(meta.getSkin());
    }
}
