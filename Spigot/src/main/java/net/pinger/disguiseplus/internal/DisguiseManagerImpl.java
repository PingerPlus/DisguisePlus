package net.pinger.disguiseplus.internal;

import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.Skin;
import net.pinger.disguise.exception.UserNotFoundException;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguise.server.MinecraftServer;
import net.pinger.disguiseplus.DisguiseManager;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.UUID;

public class DisguiseManagerImpl implements DisguiseManager {

    protected final DisguisePlus dp;
    private final PacketProvider provider;

    public DisguiseManagerImpl(DisguisePlus dp, PacketProvider provider) {
        this.dp = dp;
        this.provider = provider;
    }

    @Override
    public void applySkin(Player player, @Nonnull Skin skin) {
        // First we need to apply the skin
        this.provider.updateProperties(player, skin);

        // Then we just need to send the needed packets to update
        this.provider.sendServerPackets(player);
    }

    @Override
    public void applySkin(UUID id, @Nonnull Skin skin) {
        // Try to get the player from the uuid
        Player player = Bukkit.getPlayer(id);

        // Check if the player is online
        // If not exit
        if (player == null) {
            return;
        }

        this.applySkin(player, skin);
    }

    @Override
    public void applySkinFromName(Player player, @Nonnull String playerName) {
        // Catch the skin from the name
        try {
            // Get the skin from mojang servers
            Skin skin = DisguiseAPI.getSkinManager().getFromMojang(playerName);

            // Check if the skin was not
            // Successfully downloaded
            if (skin == null) {
                return;
            }

            // Apply the skin
            // As last task
            this.applySkin(player, skin);
        } catch (UserNotFoundException e) {
            return;
        }
    }

    @Override
    public void applySkinFromUrl(Player player, @Nonnull String url) {
        DisguiseAPI.getSkinManager().getFromImage(url, response -> {
            if (!response.success()) {
                return;
            }

            // Apply the wanted skin
            this.applySkin(player, response.get());
        });
    }

    @Override
    public void disguise(Player player) {
        // Try to get a random skin
        // From the factory
        Skin randomSkin = this.dp.getSkinFactory().getRandomSkin();

        // Check if the random skin is null
        // And if so apply the default value
        // Which is the skin of ITSPINGER
        if (randomSkin == null) {
            randomSkin = DisguiseAPI.getSkinManager().getFromMojang("ITSPINGER");
        }

        // Now apply the skin to the player
        this.applySkin(player, randomSkin);
    }

    @Override
    public void undisguise(Player player) {
        // Clear the properties of this player
        this.provider.clearProperties(player);

        // Here we need to check for NickMatching
        // Condition
    }
}
