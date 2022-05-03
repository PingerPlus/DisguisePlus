package net.pinger.disguise.packet;

import net.pinger.bukkit.nms.NMS;
import net.pinger.disguise.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

public interface PacketProvider<T> {

    /**
     * This method applies a certain property to a player.
     *
     * @param player the player that the skin is applied to
     * @param skin the skin that is being applied
     */

    void updateProperties(Player player, @Nonnull Skin skin);

    /**
     * This method clears the whole properties of the player.
     * It is equal to just clearing the game profile of the player.
     *
     * @param player the player
     */

    void clearProperties(Player player);

    /**
     * Sends a specific packet or multiple packets to this player.
     *
     * @param player the player that the packets are being sent
     * @param packet the packets that are being sent
     */

    void sendPacket(Player player, T... packet);

    /**
     * This method sends packets to all servers that are
     * currently logged in to the server.
     *
     * @param packet the packets that are sent
     */

    @SuppressWarnings("unchecked")
    default void sendPacket(T... packet) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            this.sendPacket(player, packet);
        }
    }

    /**
     * This method sends all packets that contribute
     * to changing the player properties.
     * <p>
     * The packets that are sent may vary depending on the version.
     *
     * @param player the player
     */

    void sendServerPackets(Player player);

    static void refreshPlayer(Plugin plugin, Player p) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (NMS.atLeast("1.17")) {
                other.hidePlayer(plugin, p);
                other.showPlayer(plugin, p);
            } else {
                other.hidePlayer(p);
                other.showPlayer(p);
            }
        }
    }

}
