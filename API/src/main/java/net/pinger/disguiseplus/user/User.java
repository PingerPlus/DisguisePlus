package net.pinger.disguiseplus.user;

import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.statistic.Statistic;
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

    /**
     * This method sets the rank of this player.
     *
     * @param rank the rank of this player
     */

    void setRank(Rank rank);

    /**
     * This method adds a specific statistic
     * to this user.
     *
     * @param statistic the statistic to add
     */

    void addStatistic(Statistic statistic);

    /**
     * This method removes a specific statistic
     * from this user.
     *
     * @param statistic the statistic to remove
     */

    void removeStatistic(Class<? extends Statistic> statistic);

    /**
     * Checks if this player is disguised.
     *
     * @return whether this player is disguised
     */

    boolean isDisguised();

    /**
     * Returns whether this player is nicked.
     * <p>
     * If the player is disguised,
     * this method will return false.
     *
     * @return whether nicked
     */

    boolean hasNickname();

    /**
     * Returns whether this user
     * has a skin applied to their
     *
     * @return whether a skin is applied
     */

    boolean hasSkinApplied();

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
