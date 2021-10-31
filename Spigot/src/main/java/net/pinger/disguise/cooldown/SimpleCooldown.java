package net.pinger.disguise.cooldown;

import net.pinger.disguise.user.User;
import org.joda.time.DateTime;

public class SimpleCooldown implements Cooldown {

    private final User user;
    private final DateTime expireDate;

    public SimpleCooldown(User user, DateTime expireDate) {
        this.user = user;
        this.expireDate = expireDate;
    }

    /**
     * Returns the user that is on cooldown.
     *
     * @return the user
     */
    @Override
    public User getUser() {
        return this.user;
    }

    /**
     * Returns the expire date of this cooldown.
     *
     * @return the expiring date
     */
    @Override
    public DateTime getExpireDate() {
        return this.expireDate;
    }
}
