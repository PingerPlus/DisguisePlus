package net.pinger.disguise.user.loader;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.user.SimpleUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleUserLoader {

    private final SimpleUser user;
    private final DisguisePlus dp;

    public SimpleUserLoader(SimpleUser user, DisguisePlus dp) {
        this.user = user;
        this.dp = dp;
    }

    public void loadUser() {
        try (Connection con = this.dp.getSQLDatabase().getConnection()) {
            try (PreparedStatement st = con.prepareStatement(
                    "SELECT * FROM users JOIN disguised ON users.id = disguised.user_id WHERE users.id = ?")) {
                st.setString(1, this.user.getId().toString());
                st.executeQuery();
            }
        } catch (SQLException e) {

        }
    }
}
