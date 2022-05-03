package net.pinger.disguise.packet;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R2.*;
import net.pinger.disguise.Skin;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashSet;

public class PacketProvider_v1_16_2 implements PacketProvider<Packet> {
    /**
     * This method applies a certain property to a player.
     *
     * @param player the player that the skin is applied to
     * @param skin   the skin that is being applied
     */

    @Override
    public void updateProperties(Player player, @Nonnull Skin skin) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        GameProfile profile = ep.getProfile();

        if (!(skin.toProperty() instanceof Property)) {
            return;
        }

        Property prop = (Property) skin.toProperty();

        // Clear the props
        this.clearProperties(player);
        profile.getProperties().put("textures", prop);
    }

    /**
     * This method clears the whole properties of the player.
     * It is equal to just clearing the game profile of the player.
     *
     * @param player the player
     */

    @Override
    public void clearProperties(Player player) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        profile.getProperties().removeAll("textures");
    }

    /**
     * Sends a specific packet or multiple packets to this player.
     *
     * @param player the player that the packets are being sent
     * @param packet the packets that are being sent
     */

    @Override
    public void sendPacket(Player player, Packet... packet) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();

        for (Packet<?> p : packet)
            handle.playerConnection.sendPacket(p);
    }

    /**
     * This method sends all packets that contribute
     * to changing the player properties.
     * <p>
     * The packets that are sent may vary depending on the version.
     *
     * @param player the player
     */

    @Override
    public void sendServerPackets(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();

        Location loc = player.getLocation();
        BlockPosition position = new BlockPosition(player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ());

        PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(ep.world.getDimensionManager(),
                ep.world.getDimensionKey(), 0,
                ep.playerInteractManager.getGameMode(),
                ep.playerInteractManager.getGameMode(),
                false, false, false);

        PacketPlayOutPosition playerPosition = new PacketPlayOutPosition(loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getYaw(),
                loc.getPitch(),
                new HashSet<>(),
                (byte) 0);

        this.sendPacket(new PacketPlayOutEntityDestroy(ep.getId()));
        this.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep));
        this.sendPacket(player, new PacketPlayOutSpawnPosition(position, 0));
        this.sendPacket(player, respawn);
        this.sendPacket(player, playerPosition);
        this.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep));

        player.updateInventory();
    }
}
