package net.pinger.disguise.database;

import net.pinger.disguise.DisguisePlus;
import org.bukkit.configuration.file.FileConfiguration;

public class  Database {

    private final DisguisePlus disguisePlus;

    // The basic data
    private String host, database,
                   username, password;

    // The port of this database
    private int port;

    private Database(DisguisePlus disguise, String host, String database, String username, String password, int port) {
        this.disguisePlus = disguise;
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public static Database loadDatabase(DisguisePlus dp, FileConfiguration configuration) {
        return new Database(dp, configuration.getString("mysql.host"), configuration.getString("mysql.database"),
                                configuration.getString("mysql.username"), configuration.getString("mysql.password"),
                                configuration.getInt("mysql.port"));
    }

    public static Database emptyDatabase(DisguisePlus dp) {
        return new Database(dp, null, null, null, null, 3306);
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
}
