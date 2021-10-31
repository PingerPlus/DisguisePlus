package net.pinger.disguise.database.settings;

import net.pinger.disguise.DisguisePlus;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseSettings {

    private String host, database,
            username, password;

    // The port of this database
    private int port;

    private DatabaseSettings(String host, String database, String name, String password, int port) {
        this.host = host;
        this.database = database;
        this.username = name;
        this.password = password;
        this.port = port;
    }

    public static DatabaseSettings create(FileConfiguration cfg) {
        return new DatabaseSettings(
                cfg.getString("mysql.host"),
                cfg.getString("mysql.database"),
                cfg.getString("mysql.username"),
                cfg.getString("mysql.password"),
                cfg.getInt("mysql.port")
        );
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatabase() {
        return database;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void saveSettings(DisguisePlus dp) {
        FileConfiguration cfg = dp.getConfig();

        cfg.set("mysql.host", this.host);
        cfg.set("mysql.username", this.username);
        cfg.set("mysql.database", this.database);
        cfg.set("mysql.password", this.password);
        cfg.set("mysql.enabled", true);
        cfg.set("mysql.port", this.port);

        dp.saveConfig();
    }
}
