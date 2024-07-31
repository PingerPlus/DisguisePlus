package net.pinger.disguiseplus.storage.implementation;

import java.sql.SQLException;
import java.util.UUID;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.user.DisguiseUser;

public interface StorageImplementation {

    DisguiseUser loadUser(UUID id) throws SQLException;

    void saveUser(DisguiseUser user) throws SQLException;

    int getSkin(String value) throws SQLException;

    void savePlayerMeta(DisguiseUser user, PlayerMeta meta) throws SQLException;

    void saveSkin(Skin skin) throws SQLException;

}
