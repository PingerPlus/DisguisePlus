package net.pinger.disguise;

import net.pinger.disguise.exceptions.InvalidUrlException;
import net.pinger.disguise.exceptions.InvalidUserException;
import net.pinger.disguise.skin.Skin;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

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

    void applySkinFromPlayer(Player player, String playerName) throws InvalidUserException;

    /**
     *
     *
     * @param player
     * @param url
     */

    void applySkinFromUrl(Player player, String url) throws InvalidUrlException;

    void applySkin(Player player, @Nonnull Skin skin);

    void applyNickname(Player player, String name);

    void resetNickname(Player player);


}
