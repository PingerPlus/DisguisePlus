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

    private final DisguisePlus disguisePlus;
    private HikariDataSource hikariDataSource;

    // The basic data
    private final DatabaseSettings settings;

    private boolean setup = false;
    private static final Logger logger = LoggerFactory.getLogger("Database");

    public Database(DisguisePlus dp, DatabaseSettings settings) {
        this.disguisePlus = dp;
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
            try (PreparedStatement s = c.prepareStatement("CREATE TABLE IF NOT EXISTS users(`id` VARCHAR(36) PRIMARY KEY NOT NULL)")) {
                s.executeUpdate();
            }

            try (PreparedStatement s = c.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS disguised(`active` BOOLEAN DEFAULT FALSE, `genName` VARCHAR(16) NOT NULL, " +
                    "`id` VARCHAR(36) NOT NULL, FOREIGN KEY (`id`) REFERENCES users(`id`));")) {
                s.executeUpdate();
            }
        } catch (SQLException e) {
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
