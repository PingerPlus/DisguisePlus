package net.pinger.disguise.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.database.settings.DatabaseSettings;
import net.pinger.disguise.settings.DisguiseSettings;
import org.bukkit.configuration.file.FileConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private HikariDataSource hikariDataSource;

    // The basic data
    private final DatabaseSettings settings;

    private boolean setup = false;
    private static final Logger logger = LoggerFactory.getLogger("Database");

    public Database(DisguisePlus dp, DatabaseSettings settings) {
        this.settings = settings;

        if (dp.getConfig().getBoolean("mysql.enabled")) {
            this.setup = true;

            // Try setting up
            this.setupConnection();
        }
    }

    private void setupConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            this.setup = false;
            logger.error("Could not find the appropriate SQL drivers.");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + this.settings.getHost() + ":" + this.settings.getPort() + "/" + this.settings.getDatabase() + "?useSSL=false&serverTimezone=Europe/Amsterdam&useUnicode=true&characterEncoding=utf-8");
        config.setUsername(this.settings.getUsername());
        config.setPassword(this.settings.getPassword());
        config.setConnectionTimeout(30 * 1000);
        config.setIdleTimeout(15 * 1000);
        config.setMaxLifetime(12 * 15 * 1000);
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(30);

        try {
            this.hikariDataSource = new HikariDataSource(config);
        } catch (Exception e) {
            this.setup = false;
            logger.error("An error occurred while setting up HikariCP -> ");
            logger.error(e.getMessage());
        }

        if (!setup)
            return;

        logger.info("Successfully loaded the Database");
        this.loadTables();
    }

    private void loadTables() {
        if (!this.isDatabaseSetup())
            return;

        try (Connection c = this.getConnection()) {
            try (PreparedStatement s = c.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS skins(`skin_id` INT(8) PRIMARY KEY AUTO_INCREMENT, `texture` TEXT NOT NULL, `signature` TEXT UNIQUE NOT NULL);")) {
                s.executeUpdate();
            }

            try (PreparedStatement s = c.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS disguised(`user_id` VARCHAR(36) PRIMARY KEY, `active` BOOLEAN DEFAULT FALSE, `nick` VARCHAR(16) NOT NULL, " +
                    "`skin_id` INT(8) NOT NULL, " +
                            "FOREIGN KEY (`skin_id`) REFERENCES skins(`skin_id`));")) {
                s.executeUpdate();
            }

            try (PreparedStatement s = c.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS nicked(`user_id` VARCHAR(36) PRIMARY KEY, `active` BOOLEAN DEFAULT FALSE, `nick` VARCHAR(32) NOT NULL);")) {
                s.executeUpdate();
            }

            try (PreparedStatement s = c.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS skined(`user_id` VARCHAR(36) PRIMARY KEY, `active` BOOLEAN DEFAULT FALSE, " +
                             "`skin_id` INT(8) NOT NULL, " +
                            "FOREIGN KEY(`skin_id`) REFERENCES skins(`skin_id`));")) {
                s.executeUpdate();
            }

        } catch (SQLException e) {
            this.setup = false;

            logger.error("Failed to create MYSQL tables -> ");
            logger.error(e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

    public boolean isDatabaseSetup() {
        return this.setup;
    }

    public DatabaseSettings getSettings() {
        return settings;
    }

}
