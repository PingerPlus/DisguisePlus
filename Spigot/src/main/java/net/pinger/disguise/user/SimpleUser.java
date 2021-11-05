package net.pinger.disguise.user;

import net.pinger.disguise.cooldown.Cooldown;
import net.pinger.disguise.skin.Skin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class SimpleUser implements User {

    private final UUID id;

    // The default name of this player when joining the server
    private String defaultName;

    public SimpleUser(UUID id) {
        this.id = id;
    }

    /**
     * This is a method which sets the default name of this user.
     * We must ensure that this method can only get run once at the user join.
     *
     * @param defaultName the name
     */

    public void setDefaultName(String defaultName) {
        if (this.defaultName != null)
            return;

        this.defaultName = defaultName;
    }

    /**
     * Returns the uuid of this player.
     *
     * @return the uuid
     */

    @Nonnull
    @Override
    public UUID getId() {
        return this.id;
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

    /**
     * Checks if this player is disguised.
     *
     * @return whether this player is disguised
     */
    @Override
    public boolean isDisguised() {
        return false;
    }

    public void setCooldown(Cooldown cooldown) {

    }
}
