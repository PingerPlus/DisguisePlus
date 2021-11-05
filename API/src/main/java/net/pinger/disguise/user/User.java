package net.pinger.disguise.user;

import net.pinger.disguise.cooldown.Cooldown;
import net.pinger.disguise.skin.Skin;

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


}
