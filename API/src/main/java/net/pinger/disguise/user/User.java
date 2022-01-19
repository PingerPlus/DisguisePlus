package net.pinger.disguise.user;

import net.pinger.disguise.cooldown.Cooldown;
import net.pinger.disguise.skin.Skin;
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
     * This method will never be null, regardless if the player is using a default skin or not.
     *
     * @return the current skin
     */

    @Nonnull
    Skin getCurrentSkin();

    /**
     * This method returns the name of the user.
     * It is equal to
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
     * Returns the cooldown of this user.
     *
     * @return the cooldown
     */

    @Nullable
    Cooldown getCooldown();

    /**
     * Checks if this player is disguised.
     *
     * @return whether this player is disguised
     */

    boolean isDisguised();

    /**
     * Returns whether this user currently has a cooldown.
     * If this value returns true, the user may not be using any skinning commands until this returns false.
     *
     * @return whether this user has a cooldown
     */

    default boolean hasCooldown() {
        return getCooldown() != null
                & getCooldown().isActive();
    }

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
