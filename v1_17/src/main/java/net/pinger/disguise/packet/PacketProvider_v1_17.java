package net.pinger.disguise.packet;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.pinger.disguise.Skin;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class PacketProvider_v1_17 implements PacketProvider<Packet> {

    /**
     * This method applies a certain property to a player.
     *
     * @param player the player that the skin is applied to
     * @param skin   the skin that is being applied
     */

    @Override
    public void updateProperties(Player player, @Nonnull Skin skin) {
        ServerPlayer handle = ((CraftPlayer) player).getHandle();
        GameProfile profile = handle.getGameProfile();

        if (!(skin.toProperty() instanceof Property)) {
            return;
        }

        Property prop = (Property) skin.toProperty();

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
        ServerPlayer handle = ((CraftPlayer) player).getHandle();

        for (Packet<?> p : packet)
            handle.connection.send(p);
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
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        BlockPos position = new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());

        ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(entityPlayer.getCommandSenderWorld().dimensionType(),
                entityPlayer.getCommandSenderWorld().dimension(),
                entityPlayer.getId(),
                entityPlayer.gameMode.getGameModeForPlayer(),
                entityPlayer.gameMode.getPreviousGameModeForPlayer(),
                false, false, false);

        this.sendPacket(new ClientboundRemoveEntityPacket(entityPlayer.getId()));
        this.sendPacket(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, entityPlayer));
        this.sendPacket(player, new ClientboundSetDefaultSpawnPositionPacket(position, 45));
        this.sendPacket(player, respawn);
        this.sendPacket(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, entityPlayer));

        player.updateInventory();
    }

}
