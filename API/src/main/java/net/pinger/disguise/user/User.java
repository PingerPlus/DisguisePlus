package net.pinger.disguise.user;

import net.pinger.disguise.skin.Skin;
import net.pinger.disguise.statistic.Statistic;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
     * Returns the current skin this player is wearing.
     * <p>
     * This method may be nullable, since we don't cache the default skin of the player.
     *
     * @return the current skin
     */

    @Nullable
    Skin getCurrentSkin();

    /**
     * This method returns the name of the user.
     * It is equal to the current name of the player.
     *
     * @return the name
     */

    @Nonnull
    String getName();

    /**
     * Returns the default name of this user which is defined by the id of this player.
     *
     * @return the default name
     */

    @Nonnull
    String getDefaultName();

    /**
     * Returns the current display name of this player.
     * This method may be null, depending on if the player has used the disguise or nick command.
     *
     * @return the changed name
     */

    @Nullable
    String getChangedName();

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
     * This method attaches a certain statistic
     * to this user.
     *
     * @param statistic the statistic
     */

    void setStatistic(Statistic statistic);

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
