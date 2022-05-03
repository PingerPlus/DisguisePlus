package net.pinger.disguise.statistic;

import net.pinger.disguise.Skin;
import net.pinger.disguise.user.User;

public class SkinStatistic implements Statistic {

    private final User user;
    private boolean active;

    private final Skin skin;

    public SkinStatistic(User user, boolean active, Skin skin) {
        this.user = user;
        this.active = active;
        this.skin = skin;
    }

    /**
     * Returns the user which possesses
     * this statistic.
     *
     * @return the user
     */

    @Override
    public User getUser() {
        return this.user;
    }

    /**
     * Whether this data is still active.
     *
     * @return whether it is active
     */

    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Sets this statistic to be false or true.
     *
     * @param active whether to be active or not
     */

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    public Skin getSkin() {
        return this.skin;
    }
}
