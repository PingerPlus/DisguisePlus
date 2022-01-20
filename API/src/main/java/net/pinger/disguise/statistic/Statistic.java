package net.pinger.disguise.statistic;

import net.pinger.disguise.user.User;

public interface Statistic {

    /**
     * Returns the user which possesses
     * this statistic.
     *
     * @return the user
     */

    User getUser();

    /**
     * Whether this data is still active.
     *
     * @return whether it is active
     */

    boolean isActive();

    /**
     * Sets this statistic to be false or true.
     *
     * @param active whether to be active or not
     */

    void setActive(boolean active);

}
