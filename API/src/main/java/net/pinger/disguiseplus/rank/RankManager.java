package net.pinger.disguiseplus.rank;

import org.bukkit.entity.Player;

import java.util.List;

public interface RankManager {

    /**
     * This method returns whether this feature should
     * be enabled.
     *
     * @return whether it should be enabled
     */

    boolean isEnabled();

    /**
     * This method returns list of ranks available to this player.
     *
     * @param player the player
     * @return list of ranks
     */

    List<Rank> getAvailableRanks(Player player);

}
