package net.pinger.disguise.settings;

import org.bukkit.World;
import org.bukkit.entity.Player;

public interface DisguiseSettings {

    boolean isAllowedInWorld(World world);

    void setDisabledForWorld(World world);

    /**
     * This method checks if this player is currently suspended
     * from using commands such as /disguise and /nick.
     *
     * @param player
     * @return
     */

    boolean hasCooldown(Player player);

}
