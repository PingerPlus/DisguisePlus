package net.pinger.disguiseplus;

import net.pinger.disguise.Skin;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface DisguiseManager {

    /**
     * This method applies a skin to the specified {@link Player}.
     *
     * @param player the player
     * @param skin the skin to apply
     */

    void applySkin(Player player, @Nonnull Skin skin);

    /**
     * This method applies a skin to the specified {@link Player} by
     * using their UUID.
     *
     * @param id the id of the player
     * @param skin the skin to apply
     */

    void applySkin(UUID id, @Nonnull Skin skin);

    /**
     * This method applies a skin to the specified {@link Player}.
     * The skin is fetched from mojang's REST API.
     *
     * @param player the player
     * @param playerName the playerName
     */

    void applySkinFromName(Player player, @Nonnull String playerName);

    /**
     * This method applies a skin fetched from the image to the specified {@link Player}.
     * The skin is fetched using mineskin's REST API.
     *
     * @param player the player
     * @param url the url of the image
     */

    void applySkinFromUrl(Player player, @Nonnull String url);

    /**
     * This method disguises the specified {@link Player}.
     *
     * @param player the player to disguise
     */

    void disguise(Player player);

    /**
     * This method undisguised the specified {@link Player}.
     *
     * @param player the player to undisguise
     */

    void undisguise(Player player);
}
