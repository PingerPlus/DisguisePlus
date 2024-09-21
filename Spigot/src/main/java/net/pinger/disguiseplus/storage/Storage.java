package net.pinger.disguiseplus.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.user.DisguiseUser;
import net.pinger.disguiseplus.storage.credentials.StorageConfig;
import net.pinger.disguiseplus.storage.credentials.StorageCredentials;
import net.pinger.disguiseplus.storage.implementation.StorageImplementation;
import net.pinger.disguiseplus.storage.implementation.sql.SqlStorage;

public class Storage {
    private static final ScheduledExecutorService STORAGE_EXECUTOR = Executors.newScheduledThreadPool(4);

    private final HikariDataSource source;
    private final StorageImplementation implementation;

    public Storage(DisguisePlus plus, StorageCredentials credentials, StorageConfig config) throws Exception {
        final HikariConfig hikari = new HikariConfig();

        // Set the jdbc url
        final String jdbc = "jdbc:mysql://" + credentials.getHost() + "?useSSL=false&useUnicode=yes&characterEncoding=UTF-8&serverTimezone=" + TimeZone.getDefault().getID();
        hikari.setJdbcUrl(config.hasCustomJdbcUrl() ? config.getCustomJdbcUrl() : jdbc);

        // Set the credentials
        hikari.setUsername(credentials.getUsername());
        hikari.setPassword(credentials.getPassword());
        hikari.setConnectionTimeout(config.getConnectionTimeout()); // 60 seconds
        hikari.setMinimumIdle(config.getMinimumIdle());
        hikari.setMaximumPoolSize(config.getMaximumPoolSize());
        hikari.setMaxLifetime(config.getMaximumLifetime()); // 5 minutes

        if (config.hasDriverClassName()) {
            hikari.setDriverClassName(config.getDriverClassName());
        }

        this.source = new HikariDataSource(hikari);
        try (final InputStream stream = plus.getResource("disguiseplus.sql")) {
            StorageLoader.init(this, stream);
        }

        this.implementation = new SqlStorage(plus, this);
    }

    public Storage(DisguisePlus plus) throws Exception {
        this(plus, null, null);
    }

    /**
     * This method returns a new connection fetched from
     * the pool.
     *
     * @return the connection
     * @throws SQLException if an error occurred while fetching
     */

    public Connection getConnection() throws SQLException {
        return this.source.getConnection();
    }

    /**
     * This method shuts down the entire connection pool.
     * <p>
     * Once the source has been shutdown, we won't be able
     * to connect to the database anymore.
     */

    public void shutdown() {
        this.source.close();
    }

    private <T> CompletableFuture<T> future(Callable<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, Storage.STORAGE_EXECUTOR);
    }

    public CompletableFuture<DisguiseUser> loadUser(UUID id) {
        return this.future(() -> this.implementation.loadUser(id));
    }

    public CompletableFuture<Void> savePlayerMeta(DisguiseUser user, PlayerMeta meta) {
        return this.future(() -> {
            this.implementation.savePlayerMeta(user, meta);
            return null;
        });
    }

    public CompletableFuture<Void> savePlayer(DisguiseUser user) {
        return this.future(() -> {
            this.implementation.saveUser(user);
            return null;
        });
    }

}
