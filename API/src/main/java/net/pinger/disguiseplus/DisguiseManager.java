package net.pinger.disguiseplus;

import net.pinger.disguiseplus.exceptions.InvalidUserException;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.IOException;

public interface DisguiseManager {

    /**
     * This method disguises a specific player.
     * <p>
     * It loads a random skin immediately and applies it directly to the player.
     *
     * @param player the player
     */

    void disguise(Player player);

    /**
     * Removes any skin that this player might have on them.
     *
     * @param player the player
     */

    void undisguise(Player player);

    /**
     * This method applies a skin that another player has.
     *
     * @throws InvalidUserException if the user was not found by the search engine.
     * @param player the player that we are playing the skin to
     * @param playerName the name of the player that the skin is referring to
     */

    void applySkinFromPlayer(Player player, String playerName) throws IOException;

    /**
     * This method applies a skin from the specified url
     *
     * @throws IOException if an error was caught while catching the skin
     * @param player the player that the skin is being applied to
     * @param url the url of the image
     */

    void applySkinFromUrl(Player player, String url) throws IOException;

    void applySkin(Player player, @Nonnull Skin skin);

    void applyNickname(Player player, String name);

    void resetNickname(Player player);


}
