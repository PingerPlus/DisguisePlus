package net.pinger.disguise.user;

import net.pinger.disguise.cooldown.Cooldown;
import net.pinger.disguise.skin.Skin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class SimpleUser implements User {

    // The default name of this player when joining the server
    private String defaultName;

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
        return this.defaultName;
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
     * Returns the cooldown of this user.
     *
     * @return the cooldown
     */
    @Nullable
    @Override
    public Cooldown getCooldown() {
        return null;
    }

    public void setCooldown(Cooldown cooldown) {

    }
}
