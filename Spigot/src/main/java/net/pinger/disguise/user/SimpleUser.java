package net.pinger.disguise.user;

import net.pinger.disguise.cooldown.Cooldown;
import net.pinger.disguise.cooldown.SimpleCooldown;
import net.pinger.disguise.skin.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class SimpleUser implements User {

    private final UUID id;

    // The default name of this player when joining the server
    private String defaultName;
    private Cooldown cooldown = null;

    SimpleUser(UUID id) {
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

    /**
     * This method returns the default name of the player is the user is not disguised,
     * otherwise it returns the {@link #getChangedName()}
     *
     * @return the changed name
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
        return this.cooldown;
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

    /**
     * Transforms the current user to a player.
     *
     * @return the player
     */

    @Override
    public Player transform() {
        return Bukkit.getPlayer(this.id);
    }

    /**
     * This method sets the cooldown of this player.
     *
     * @param cooldown the cooldown
     */

    public void setCooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
    }
}
