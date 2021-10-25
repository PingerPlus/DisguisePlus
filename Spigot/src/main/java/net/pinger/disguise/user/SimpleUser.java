package net.pinger.disguise.user;

import net.pinger.disguise.skin.Skin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class SimpleUser implements User {
    /**
     * Returns the uuid of this player.
     *
     * @return the uuid
     */
    @Nonnull
    @Override
    public UUID getId() {
        return null;
    }

    /**
     * Returns the current skin this player is wearing.
     * <p>
     * This method will never be null, regardless if the player is using a default skin or not.
     *
     * @return the current skin
     */
    @Nonnull
    @Override
    public Skin getCurrentSkin() {
        return null;
    }

    /**
     * This method returns the name of the user.
     * It is equal to
     *
     * @return the name
     */

    @Nonnull
    @Override
    public String getName() {
        return null;
    }

    /**
     * Returns the default name of this user which is defined by the id of this player.
     *
     * @return the default name
     */
    @Nonnull
    @Override
    public String getDefaultName() {
        return null;
    }

    /**
     * Returns the current display name of this player.
     * This method may be null, depending on if the player has used the disguise or nick command.
     *
     * @return the changed name
     */
    @Nullable
    @Override
    public String getChangedName() {
        return null;
    }

    /**
     * Returns whether this user currently has a cooldown.
     * If this value returns true, the user may not be using any skinning commands until this returns false.
     *
     * @return whether this user has a cooldown
     */
    @Override
    public boolean hasCooldown() {
        return false;
    }
}
