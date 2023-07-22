package net.pinger.disguiseplus.internal.user;

import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.statistic.DisguiseStatistic;
import net.pinger.disguiseplus.statistic.NickStatistic;
import net.pinger.disguiseplus.statistic.SkinStatistic;
import net.pinger.disguiseplus.statistic.Statistic;
import net.pinger.disguiseplus.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class UserImpl implements User {

    private final DisguisePlus dp;
    private final UUID id;

    private Rank currentRank = null;
    private SkinStatistic skinStatistic;
    private DisguiseStatistic disguiseStatistic;
    private NickStatistic nickStatistic;

    // The default name of this player when joining the server
    private String defaultName;

    UserImpl(DisguisePlus dp, UUID id) {
        this.dp = dp;
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
        // Just return null then
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
        if (this.isDisguised() || this.hasNickname()) {
            return this.getChangedName();
        }

        return this.getDefaultName();
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
        return this.disguiseStatistic == null ?
                this.nickStatistic == null ? null : this.nickStatistic.getNickname()
                : this.disguiseStatistic.getNickname();
    }

    @Override
    public Rank getCurrentRank() {
        return this.currentRank;
    }

    @Override
    public void setRank(Rank rank) {
        this.currentRank = rank;
    }

    @Override
    public void addStatistic(Statistic statistic) {
        if (statistic instanceof SkinStatistic) {
            this.skinStatistic = (SkinStatistic) statistic;
            return;
        }

        if (statistic instanceof NickStatistic) {
            this.nickStatistic = (NickStatistic) statistic;
            return;
        }

        if (statistic instanceof DisguiseStatistic) {
            this.disguiseStatistic = (DisguiseStatistic) statistic;
        }
    }

    @Override
    public void removeStatistic(Class<? extends Statistic> statistic) {
        if (statistic.isAssignableFrom(DisguiseStatistic.class)) {
            this.disguiseStatistic = null;
            return;
        }

        if (statistic.isAssignableFrom(SkinStatistic.class)) {
            this.skinStatistic = null;
            return;
        }

        if (statistic.isAssignableFrom(NickStatistic.class)) {
            this.nickStatistic = null;
        }
    }

    /**
     * Checks if this player is disguised.
     *
     * @return whether this player is disguised
     */

    @Override
    public boolean isDisguised() {
        return this.disguiseStatistic != null;
    }

    /**
     * Returns whether this player is nicked.
     * <p>
     * If the player is disguised,
     * this method will return false.
     *
     * @return whether nicked
     */

    @Override
    public boolean hasNickname() {
        return this.nickStatistic != null;
    }

    /**
     * Returns whether this user
     * has a skin applied to their
     *
     * @return whether a skin is applied
     */

    @Override
    public boolean hasSkinApplied() {
        return this.skinStatistic != null;
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
     * This method sends a message within the messages.yml file of the plugin.
     *
     * @param key the key of the message
     */

    @Override
    public void sendMessage(String key) {
        if (this.transform() == null)
            return;

        if (!this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().of(key);
        this.transform().sendMessage(message);
    }

    /**
     * This method sends a formatted message from the configuration file
     * to this player.
     * <p>
     * Do not do this if you want to send a message that is not within the messages.yml file.
     *
     * @param key    they key from the config
     * @param format the objects to format
     */

    @Override
    public void sendMessage(String key, Object... format) {
        if (this.transform() == null)
            return;

        if (!this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().ofFormatted(key, format);
        this.transform().sendMessage(message);
    }

    /**
     * This messages sends a raw message to this player
     * from the configuration.
     *
     * @param key the key from the config
     */

    @Override
    public void sendRawMessage(String key) {
        if (this.transform() == null) {
            return;
        }

        if (!this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().of(key);
        this.transform().sendRawMessage(message);
    }

    /**
     * This method sends a raw formatted message from the configuration file
     * to this player.
     * <p>
     * Do not do this if you want to send a message that is not within the messages.yml file.
     *
     * @param key    they key from the config
     * @param format the objects to format
     */

    @Override
    public void sendRawMessage(String key, Object... format) {
        if (this.transform() == null) {
            return;
        }

        if (!this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().ofFormatted(key, format);
        this.transform().sendRawMessage(message);
    }
}
