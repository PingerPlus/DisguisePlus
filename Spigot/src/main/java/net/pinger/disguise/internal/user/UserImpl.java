package net.pinger.disguise.internal.user;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.database.Database;
import net.pinger.disguise.skin.SimpleSkin;
import net.pinger.disguise.Skin;
import net.pinger.disguise.statistic.DisguiseStatistic;
import net.pinger.disguise.statistic.NickStatistic;
import net.pinger.disguise.statistic.SkinStatistic;
import net.pinger.disguise.statistic.Statistic;
import net.pinger.disguise.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserImpl implements User {

    private static final Logger logger = LoggerFactory.getLogger("UserConnection");

    private final DisguisePlus dp;
    private final UUID id;

    // The default name of this player when joining the server
    private String defaultName;

    // The skin statistic
    private SkinStatistic skinStatistic = null;

    // The nick statistic
    private NickStatistic nickStatistic = null;

    // The disguise statistic
    private DisguiseStatistic disguiseStatistic = null;

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
        if (this.isDisguised())
            return this.disguiseStatistic.getSkin();

        // Maybe they used the /skin command
        if (this.hasSkinApplied())
            return this.skinStatistic.getSkin();

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
        if (this.isDisguised())
            return this.disguiseStatistic.getNick();

        // They may be nicked
        if (this.hasNickname())
            return this.nickStatistic.getNick();

        // Just return null
        return null;
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
     * This method attaches a certain statistic
     * to this user.
     *
     * @param statistic the statistic
     */

    @Override
    public void setStatistic(Statistic statistic) {
        if (statistic instanceof DisguiseStatistic) {
            // Check if it is possible
            if (this.hasNickname() || this.hasSkinApplied())
                return;

            this.disguiseStatistic = (DisguiseStatistic) statistic;
            return;
        }

        if (statistic instanceof NickStatistic) {
            // Check if possible
            if (this.isDisguised())
                return;

            this.nickStatistic = (NickStatistic) statistic;
            return;
        }

        if (statistic instanceof SkinStatistic) {
            // Check if possible
            if (this.isDisguised())
                return;

            this.skinStatistic = (SkinStatistic) statistic;
        }
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
        if (this.transform() == null)
            return;

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
        if (this.transform() == null)
            return;

        if (!this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().ofFormatted(key, format);
        this.transform().sendRawMessage(message);
    }

    /**
     * This method is used to retrieve data
     * when this user joins the server.
     * <p>
     * Note that you shouldn't try to access
     * this method with reflection.
     */

    public void retrieveInformation() {
        if (!this.dp.getSQLDatabase().isDatabaseSetup()) {
            return;
        }

        Database db = this.dp.getSQLDatabase();

        try (Connection connection = db.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM disguised WHERE `uuid` = ? AND `active` = TRUE;")) {
                // Set the uuid
                statement.setString(1, this.id.toString());
                statement.executeQuery();

                try (ResultSet set = statement.getResultSet()) {
                    // If it's found
                    if (set.next()) {
                        // First retrieve the skin
                        Skin skin = SimpleSkin.retrieveSkin(set.getLong("skin_id"), connection);
                        this.disguiseStatistic = new DisguiseStatistic(this, true, skin, set.getString("nick"));
                    }
                }
            }

            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM nicked WHERE `uuid` = ? AND `active` = TRUE;")) {
                // Set the uuid
                statement.setString(1, this.id.toString());
                statement.executeQuery();

                try (ResultSet set = statement.getResultSet()) {
                    // If it's found
                    if (set.next()) {
                        this.nickStatistic = new NickStatistic(this, true, set.getString("nick"));
                    }
                }
            }

            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM skined WHERE `uuid` = ? AND `active` = TRUE;")) {
                // Set the uuid
                statement.setString(1, this.id.toString());
                statement.executeQuery();

                try (ResultSet set = statement.getResultSet()) {
                    // If it's found
                    if (set.next()) {
                        // Retrieve the skin first
                        Skin skin = SimpleSkin.retrieveSkin(set.getLong("skin_id"), connection);
                        this.skinStatistic = new SkinStatistic(this, true, skin);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(" ", e);
        }
    }

    /**
     * This method saves the information
     * for this used, which can be used for syncing the data between servers.
     * <p>
     * Note that you shouldn't try to access
     * this method with reflection.
     */

    public void saveInformation() {
        if (!this.dp.getSQLDatabase().isDatabaseSetup()) {
            return;
        }

        Database db = this.dp.getSQLDatabase();

        try (Connection connection = db.getConnection()) {
            if (this.disguiseStatistic == null) {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE disguised SET `active` = FALSE WHERE `uuid` = ?;")) {
                    // Set the uuid
                    statement.setString(1, this.id.toString());
                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement("REPLACE INTO disguised VALUES (?, ?, ?, ?);")) {
                    statement.setString(1, this.id.toString());
                    statement.setBoolean(2, true);
                    statement.setString(3, this.disguiseStatistic.getNick());
                    statement.setLong(4,  ((SimpleSkin) this.disguiseStatistic.getSkin()).getId(connection));
                    statement.executeUpdate();
                }
            }

            if (this.nickStatistic == null) {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE nicked SET `active` = FALSE WHERE `uuid` = ?;")) {
                    // Set the uuid
                    statement.setString(1, this.id.toString());
                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement("REPLACE INTO nicked VALUES(?, ?, ?);")) {
                    statement.setString(1, this.id.toString());
                    statement.setInt(2, 1);
                    statement.setString(3, this.nickStatistic.getNick());
                    statement.executeUpdate();
                }
            }

            if (this.skinStatistic == null) {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE skined SET `active` = FALSE WHERE `uuid` = ?;")) {
                    // Set the uuid
                    statement.setString(1, this.id.toString());
                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement("REPLACE INTO skined VALUES(?, ?, ?);")) {
                    statement.setString(1, this.id.toString());
                    statement.setBoolean(2, true);
                    statement.setLong(3, ((SimpleSkin) this.skinStatistic.getSkin()).getId(connection));
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            logger.error(" ", e);
        }
    }
}
