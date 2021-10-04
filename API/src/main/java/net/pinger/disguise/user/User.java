package net.pinger.disguise.user;

import net.pinger.disguise.skin.Skin;

import java.util.UUID;

public interface User {

    /**
     * Returns the uuid of this player.
     *
     * @return the uuid
     */

    UUID getId();

    /**
     * Returns the current skin this player is wearing.
     * <p>
     * This method will never be null, regardless if the player is using a default skin or not.
     *
     * @return the current skin
     */

    Skin getCurrentSkin();

    /**
     * Returns the default name of this user which is defined by the id of this player.
     *
     * @return the default name
     */

    String getDefaultName();

    /**
     * Returns the current display name of this player.
     * This method may be null, depending on if the player has used the disguise or nick command.
     *
     * @return the changed name
     */

    String getChangedName();

    /**
     * Returns whether this user currently has a cooldown.
     * If this value returns true, the user may not be using any skinning commands until this returns false.
     *
     * @return whether this user has a cooldown
     */

    boolean hasCooldown();

}
