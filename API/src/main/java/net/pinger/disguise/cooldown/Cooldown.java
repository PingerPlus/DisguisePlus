package net.pinger.disguise.cooldown;

import net.pinger.disguise.user.User;
import org.joda.time.DateTime;

public interface Cooldown {

    /**
     * Returns the user that is on cooldown.
     *
     * @return the user
     */

    User getUser();

    /**
     * Returns the expire date of this cooldown.
     *
     * @return the expiring date
     */

    DateTime getExpireDate();

    /**
     * Checks if this cooldown is still active.
     *
     * @return if the cooldown is active
     */

    default boolean isActive() {
        return getExpireDate().isAfterNow();
    }

}
