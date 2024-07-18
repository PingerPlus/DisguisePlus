package net.pinger.disguiseplus.user;

import net.pinger.disguiseplus.rank.Rank;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface User {

    /**
     * Returns the uuid of this player.
     *
     * @return the uuid
     */

    @Nonnull
    UUID getId();

    /**
     * This method returns the name of the user.
     * It is equal to the current name of the player.
     *
     * @return the name
     */

    @Nonnull
    String getName();

    /**
     * This method returns the current rank applied to this user.
     *
     * @return the rank
     */

    Rank getCurrentRank();

    boolean isDisguised();

    /**
     * This method sets the rank of this player.
     *
     * @param rank the rank of this player
     */

    void setRank(Rank rank);

    /**
     * Transforms the current user to a player.
     *
     * @return the player
     */

    Player transform();

    /**
     * This method sends a message from the configuration file
     * to this player.
     * <p>
     * Do not do this if you want to send a message that is not within the messages.yml file.
     *
     * @param key they key from the config
     */

    void sendMessage(String key);

    /**
     * This method sends a formatted message from the configuration file
     * to this player.
     * <p>
     * Do not do this if you want to send a message that is not within the messages.yml file.
     *
     * @param key they key from the config
     * @param format the objects to format
     */

    void sendMessage(String key, Object... format);

    /**
     * This messages sends a raw message to this player
     * from the configuration.
     *
     * @param key the key from the config
     */

    void sendRawMessage(String key);

    /**
     * This method sends a raw formatted message from the configuration file
     * to this player.
     * <p>
     * Do not do this if you want to send a message that is not within the messages.yml file.
     *
     * @param key they key from the config
     * @param format the objects to format
     */

    void sendRawMessage(String key, Object... format);
}
