package net.pinger.disguiseplus.internal;

import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.Skin;
import net.pinger.disguise.exception.UserNotFoundException;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguiseplus.DisguiseManager;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.User;
import net.pinger.disguiseplus.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class DisguiseManagerImpl implements DisguiseManager {

    private Method playerProfileMethod;
    private Field playerNameField;

    protected final DisguisePlus dp;
    protected final PacketProvider provider;

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
        } catch (UserNotFoundException ignored) {

        }
    }

    @Override
    public void resetSkin(Player player) {
        // First clear the properties of this player
        this.provider.clearProperties(player);

        // Check for name matching
        if (!this.dp.getPlayerMatcher().matches(player)) {
            this.provider.sendServerPackets(player);
            return;
        }

        // Apply the skin here
        this.applySkinFromName(player, player.getName());
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
    public void setNickname(Player player, @Nonnull String nickname) {
        if (nickname.length() > 16 || nickname.length() < 3) {
            throw new IllegalArgumentException("A nickname must be between 3 and 16 characters");
        }

        this.updatePlayerNickname(player, nickname);
        this.provider.sendServerPackets(player);
    }

    @Override
    public void resetNickname(Player player) {
        User user = this.dp.getUserManager().getUser(player);

        // Reset the nickname by setting
        // It to the default name
        this.setNickname(player, user.getDefaultName());
    }

    @Override
    public void disguise(Player player) {
        // Try to get a random skin and nick
        // From the factory
        Skin randomSkin = this.dp.getSkinFactory().getRandomSkin();
        String nickname = StringUtil.randomize();

        // Check if the random skin is null
        // And if so apply the default value
        // Which is the skin of ITSPINGER
        if (randomSkin == null) {
            randomSkin = DisguiseAPI.getSkinManager().getFromMojang("ITSPINGER");
        }

        // Now apply the skin to the player
        this.updatePlayerNickname(player, nickname);
        this.applySkin(player, randomSkin);
    }

    @Override
    public void undisguise(Player player) {
        // Clear the properties of this player
        this.provider.clearProperties(player);

        // Reset the player nickname
        this.resetNickname(player);

        // Here we need to check for NickMatching
        // Condition
        if (!this.dp.getPlayerMatcher().matches(player)) {
            this.provider.sendServerPackets(player);
            return;
        }

        // Apply the skin here
        this.applySkinFromName(player, player.getName());
    }

    protected void updatePlayerNickname(Player player, @Nonnull String nickname) {
        try {
            // Set the profile method
            if (this.playerProfileMethod == null) {
                this.playerProfileMethod = player.getClass().getMethod("getProfile");
            }

            // Get the GameProfile object
            // And update the name field
            Object gameProfile = this.playerProfileMethod.invoke(player);

            if (this.playerNameField == null) {
                this.playerNameField = gameProfile.getClass().getDeclaredField("name");
            }

            this.playerNameField.setAccessible(true);
            this.playerNameField.set(gameProfile, nickname);

            // Change for the API
            player.setDisplayName(nickname);
            player.setPlayerListName(nickname);
        } catch (Exception e) {
            DisguisePlus.getOutput().info("Failed to update player nickname", e);
        }
    }
}
