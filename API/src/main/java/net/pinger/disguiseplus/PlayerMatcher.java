package net.pinger.disguiseplus;

import org.bukkit.entity.Player;

public interface PlayerMatcher {

    /**
     * This method returns whether the player's uuid returned from the
     * mojang servers uuid from {@link Player#getUniqueId()} match.
     *
     * @param player the player
     * @return whether i
     */

    boolean matches(Player player);

}
