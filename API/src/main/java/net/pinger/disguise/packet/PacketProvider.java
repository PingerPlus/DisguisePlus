package net.pinger.disguise.packet;

import net.pinger.disguise.skin.Skin;
import org.bukkit.entity.Player;

public interface PacketProvider<T> {

    /**
     * This method sends all packets that contribute
     * to changing the player properties.
     * <p>The packets that are sent may vary depending on the version.
     *
     * @param player the player
     */

    void sendServerPackets(Player player);
}
