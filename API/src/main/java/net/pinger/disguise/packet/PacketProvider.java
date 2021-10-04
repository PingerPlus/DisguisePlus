package net.pinger.disguise.packet;

import net.pinger.disguise.skin.Skin;
import org.bukkit.entity.Player;

public interface PacketProvider<T> {

    void disguise(Player player, Skin skin);

    void sendPacket(T... packet);

    void sendPacket(Player player, T... packet);

    void sendServerPackets(Player player);
}
