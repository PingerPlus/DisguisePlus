package net.pinger.disguiseplus.storage.implementation;

import java.sql.SQLException;
import java.util.UUID;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.internal.PlayerMeta;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.user.User;

public interface StorageImplementation {

    UserImpl loadUser(UUID id) throws SQLException;

    void saveUser(User user) throws SQLException;

    int getSkin(String value) throws SQLException;

    void savePlayerMeta(User user, PlayerMeta meta) throws SQLException;

    void saveSkin(Skin skin) throws SQLException;

}
