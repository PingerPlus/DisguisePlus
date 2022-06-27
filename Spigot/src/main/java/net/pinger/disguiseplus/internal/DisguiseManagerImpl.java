package net.pinger.disguiseplus.internal;

import net.pinger.disguiseplus.DisguiseManager;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.DisguisePlusAPI;
import net.pinger.disguiseplus.exceptions.InvalidUserException;
import net.pinger.disguiseplus.fetcher.SkinFetcher;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguiseplus.user.User;
import net.pinger.disguiseplus.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DisguiseManagerImpl implements DisguiseManager {

    protected final DisguisePlus dp;
    private final PacketProvider<?> provider;

    // Method responsible for changing the nickname
    private static Method playerHandleMethod;
    private static Method profileMethod;

    // The logger
    private static final Logger logger = LoggerFactory.getLogger("DisguisePlus");

    public DisguiseManagerImpl(DisguisePlus dp, PacketProvider<?> provider) {
        this.dp = dp;
        this.provider = provider;
    }

    /**
     * This method disguises a specific player.
     * <p>
     * It loads a random skin immediately and applies it directly to the player.
     *
     * @param player the player
     */

    @Override
    public void disguise(Player player) {
        this.applySkin(player, DisguisePlusAPI.getSkinFactory().getRandomSkin());

        // Apply a random nick as well
        this.applyNickname(player, StringUtil.randomize());
    }

    /**
     * Removes any skin that this player might have on them.
     *
     * @param player the player
     */

    @Override
    public void undisguise(Player player) {
        this.provider.clearProperties(player);

        // Reset the player nickname
        this.resetNickname(player);

        if (this.dp.getSettings().isOnlineMode()) {
            // Check if the UUID matches to the player name
            if (!NickFetcher.matches(player)) {
                this.provider.sendServerPackets(player);
                return;
            }

            this.applySkinFromPlayer(player, player.getName());
        }
    }

    /**
     * This method applies a skin that another player has.
     *
     * @param player     the player that we are playing the skin to
     * @param playerName the name of the player that the skin is referring to
     * @throws InvalidUserException if the user was not found by the search engine.
     */

    @Override
    public void applySkinFromPlayer(Player player, String playerName) throws InvalidUserException {
        Skin s = SkinFetcher.getSkin(playerName);

        if (s == null)
            throw new InvalidUserException();

        this.applySkin(player, s);
    }

    @Override
    public void applySkinFromUrl(Player player, String url) throws IOException {
        // Check if this user is under cooldown
        SkinFetcher.catchSkin(url, s -> this.applySkin(player, s), e -> {
            throw new RuntimeException(e);
        });
    }

    @Override
    public void applySkin(Player player, @Nonnull Skin skin) {
        this.provider.updateProperties(player, skin);
        this.provider.sendServerPackets(player);
    }

    @Override
    public void applyNickname(Player player, String name) {
        // Check the nick conditions
        if (name.isEmpty() || name.length() >= 16)
            throw new IllegalArgumentException("Length cannot be more or equal to 16");

        this.applyNick(player, ChatColor.translateAlternateColorCodes('&', name));
    }

    @Override
    public void resetNickname(Player player) {
        // Apply the players original nickname
        User s = this.dp.getUserManager().getUser(player);

        // Apply the nick
        this.applyNickname(player, s.getDefaultName());
    }

    /**
     * This method applies a nick to a specific player.
     * It shouldn't be used anywhere outside the {@link DisguiseManagerImpl} class.
     *
     * @param player the player
     * @param name the name
     */

    private void applyNick(Player player, String name) {
        try {
            // Set the player handle method
            if (playerHandleMethod == null)
                playerHandleMethod = player.getClass().getMethod("getHandle");

            Object entity = playerHandleMethod.invoke(player);

            // Set the profile method
            if (profileMethod == null) {
                profileMethod = entity.getClass().getMethod("getProfile");
            }

            Object ep = profileMethod.invoke(entity);
            Field nameField = ep.getClass().getDeclaredField("name");

            // Set the field
            nameField.setAccessible(true);
            nameField.set(ep, name);

            // Change the player fields
            player.setDisplayName(name);
            player.setPlayerListName(name);
        } catch (Exception e) {
            logger.error("Failed to load set a nick for player -> " + player.getName());
            logger.error(e.getMessage());
        }
    }


}
