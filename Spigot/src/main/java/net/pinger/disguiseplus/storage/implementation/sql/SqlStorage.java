package net.pinger.disguiseplus.storage.implementation.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.UUID;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.user.DisguiseUser;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.storage.Storage;
import net.pinger.disguiseplus.storage.implementation.StorageImplementation;
import net.pinger.disguiseplus.utils.IndexedList;

public class SqlStorage implements StorageImplementation {
    private static final String LOAD_PLAYER = "SELECT * FROM users WHERE uuid = ?;";
    private static final String INSERT_PLAYER = "INSERT INTO users(uuid, username, join_date, last_login) VALUES(?, ?, NOW(), NOW());";
    private static final String UPDATE_PLAYER = "UPDATE users SET last_login = NOW() WHERE uuid = ?;";
    private static final String LOAD_DISGUISES = "SELECT * FROM disguises WHERE uuid = ?;";
    private static final String LOAD_DISGUISE_META = "SELECT * FROM disguises WHERE uuid = ? AND start_time = ?;";
    private static final String LOAD_SKIN = "SELECT * FROM skins WHERE skin_id = ?;";
    private static final String LOAD_SKIN_VALUE = "SELECT * FROM skins WHERE skin_value = ?;";
    private static final String SAVE_SKIN = "INSERT INTO skins(skin_value, skin_signature) VALUES(?, ?);";
    private static final String UPDATE_META = "UPDATE disguises SET end_time = ? WHERE uuid = ? AND start_time = ?;";
    private static final String INSERT_META = "INSERT INTO disguises(uuid, skin_id, name, rank, start_time, end_time) VALUES(?, ?, ?, ?, ?, NULL);";

    private final DisguisePlus dp;
    private final Storage storage;

    public SqlStorage(DisguisePlus dp, Storage storage) {
        this.dp = dp;
        this.storage = storage;
    }

    @Override
    public DisguiseUser loadUser(UUID id) throws SQLException {
        try (final Connection connection = this.storage.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(LOAD_PLAYER)) {
                statement.setString(1, id.toString());
                try (final ResultSet set = statement.executeQuery()) {
                    if (!set.next()) {
                        return new DisguiseUser(this.dp, id);
                    }
                }

                final IndexedList<PlayerMeta> meta = this.getPlayerMeta(connection, id);
                return new DisguiseUser(this.dp, id, meta);
            }
        }
    }

    @Override
    public void saveUser(DisguiseUser user) throws SQLException {
        try (final Connection connection = this.storage.getConnection()) {
            if (this.existsPlayer(connection, user.getId())) {
                try (final PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER)) {
                    statement.setString(1, user.getId().toString());
                    statement.executeUpdate();
                    return;
                }
            }

            try (final PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER)) {
                statement.setString(1, user.getId().toString());
                statement.setString(2, DisguiseAPI.getDisguisePlayer(user.getId()).getDefaultName());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public int getSkin(String value) throws SQLException {
        try (final Connection connection = this.storage.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(LOAD_SKIN_VALUE)) {
                statement.setString(1, value);
                try (final ResultSet set = statement.executeQuery()) {
                    if (set.next()) {
                        return set.getInt("skin_id");
                    }
                }
            }
        }

        return -1;
    }

    @Override
    public void savePlayerMeta(DisguiseUser user, PlayerMeta meta) throws SQLException {
        try (final Connection connection = this.storage.getConnection()) {
            boolean saved = this.existsPlayerMeta(connection, user, meta);
            if (saved) {
                try (final PreparedStatement statement = connection.prepareStatement(UPDATE_META)) {
                    statement.setTimestamp(1, Timestamp.valueOf(meta.getEndTime()));
                    statement.setString(2, user.getId().toString());
                    statement.setTimestamp(3, Timestamp.valueOf(meta.getStartTime()));
                    return;
                }
            }

            // If the meta doesn't exist in the database, save both skin and meta
            int def = this.saveAndGetSkin(meta.getSkin());
            try (final PreparedStatement statement = connection.prepareStatement(INSERT_META)) {
                statement.setString(1, user.getId().toString());
                statement.setObject(2, def == -1 ? null : def, Types.INTEGER);
                statement.setString(3, meta.getName());
                statement.setObject(4, meta.getRank() == null ? null : meta.getRank().getName());
                statement.setTimestamp(5, Timestamp.valueOf(meta.getStartTime()));
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void saveSkin(Skin skin) throws SQLException {
        if (this.existsSkin(skin)) {
            return;
        }

        try (final Connection connection = this.storage.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(SAVE_SKIN)) {
                statement.setString(1, skin.getValue());
                statement.setString(2, skin.getSignature());
                statement.executeUpdate();
            }
        }
    }

    public int saveAndGetSkin(Skin skin) throws SQLException {
        if (skin == null) {
            return -1;
        }

        this.saveSkin(skin);
        return this.getSkin(skin.getValue());
    }

    private boolean existsPlayer(Connection c, UUID id) throws SQLException {
        try (final PreparedStatement statement = c.prepareStatement(LOAD_PLAYER)) {
            statement.setString(1, id.toString());
            try (final ResultSet set = statement.executeQuery()) {
                return set.next();
            }
        }
    }

    private boolean existsPlayerMeta(Connection c, DisguiseUser user, PlayerMeta meta) throws SQLException {
        try (final PreparedStatement statement = c.prepareStatement(LOAD_DISGUISE_META)) {
            statement.setString(1, user.getId().toString());
            statement.setTimestamp(2, Timestamp.valueOf(meta.getStartTime()));
            try (final ResultSet set = statement.executeQuery()) {
                return set.next();
            }
        }
    }

    private boolean existsSkin(Skin skin) throws SQLException {
        try (final Connection connection = this.storage.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(LOAD_SKIN_VALUE)) {
                statement.setString(1, skin.getValue());
                try (final ResultSet set = statement.executeQuery()) {
                    return set.next();
                }
            }
        }
    }

    private IndexedList<PlayerMeta> getPlayerMeta(Connection c, UUID id) throws SQLException {
        final IndexedList<PlayerMeta> meta = new IndexedList<>();
        try (final PreparedStatement statement = c.prepareStatement(LOAD_DISGUISES)) {
            statement.setString(1, id.toString());
            try (final ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    final Rank rank = this.dp.getRankManager().getRank(set.getString("rank"));
                    final Skin skin = this.retrieveSkin(c, set.getInt("skin_id"));
                    final Timestamp endTime = set.getTimestamp("end_time");
                    meta.add(new PlayerMeta(
                        skin,
                        rank,
                        set.getString("name"),
                        set.getTimestamp("start_time").toLocalDateTime(),
                        endTime == null ? null : endTime.toLocalDateTime()
                    ));
                }
            }
        }
        return meta;
    }

    private Skin retrieveSkin(Connection c, int skinId) throws SQLException {
        try (final PreparedStatement statement = c.prepareStatement(LOAD_SKIN)) {
            statement.setInt(1, skinId);
            try (final ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return new Skin(
                        set.getString("skin_value"),
                        set.getString("skin_signature")
                    );
                }
            }
        }

        return null;
    }
}
