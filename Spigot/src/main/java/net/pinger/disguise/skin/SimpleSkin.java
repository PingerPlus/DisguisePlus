package net.pinger.disguise.skin;

import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.disguise.Skin;
import net.pinger.disguise.skull.SkullManager;
import net.pinger.disguise.context.PropertyContext;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleSkin implements Skin {

    private final String signature, value;
    private final ItemStack skull = new ItemBuilder(FreshMaterial.PLAYER_HEAD.toMaterial(), (short) 3).toItemStack();

    // This is the id retrieved from sql
    private long id;

    private SimpleSkin(String value, String signature) {
        this.signature = signature;
        this.value = value;

        // Update the meta
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        SkullManager.mutateItemMeta(meta, this);
        this.skull.setItemMeta(meta);
    }

    public static Skin newBuilder(String value, String signature) {
        return new SimpleSkin(value, signature);
    }

    /**
     * Returns the value of this skin.
     *
     * @return the value
     */
    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * Returns the signature of this skin.
     *
     * @return the signature
     */
    @Override
    public String getSignature() {
        return this.signature;
    }

    /**
     * Transform this skin into a property object based on the current version of the server.
     *
     * @return the property
     */

    @Override @Nonnull
    public Object getHandle() {
        return PropertyContext.createProperty(this);
    }

    /**
     * Transforms this skin to a skull.
     * <p>
     * This instance is created once the skin has been initialized,
     * and it will never change unless the server has been restarted.
     *
     * @return the skull from this item
     */

    @Override
    public ItemStack toSkull() {
        return this.skull;
    }

    public long getId(Connection connection) throws SQLException {
        if (this.id != 0)
            return this.id;

        try (PreparedStatement statement = connection.prepareStatement("INSERT IGNORE INTO skins VALUES (?, ?);")) {
            statement.setString(1, this.value);
            statement.setString(2, this.signature);
            statement.executeUpdate();
        }

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM skins WHERE `signature` = ?;")) {
            statement.setString(1, this.signature);
            statement.executeQuery();

            try (ResultSet set = statement.getResultSet()) {
                return this.id = set.getLong("skin_id");
            }
        }
    }

    /**
     * This method is a quick way to retrieve
     * a skin from the database.
     *
     * @param id the id of the skin
     * @param connection the connection object
     * @return the skin
     * @throws SQLException if an error occurred while performing these operations
     */

    public static Skin retrieveSkin(long id, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM skins WHERE skin_id = ?;")){
            statement.setLong(1, id);
            statement.executeQuery();

            try (ResultSet set = statement.getResultSet()) {
                if (set.next()) {
                    return new SimpleSkin(set.getString("texture"), set.getString("signature"));
                }
            }
        }

        return null;
    }
}

