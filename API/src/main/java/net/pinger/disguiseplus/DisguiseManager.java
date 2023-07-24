package net.pinger.disguiseplus;

import net.pinger.disguiseplus.user.User;
import org.bukkit.entity.Player;

public interface DisguiseManager {

    /**
     * This method disguises the specified {@link Player}.
     *
     * @param user the player to disguise
     */

    void disguise(User user);

}
