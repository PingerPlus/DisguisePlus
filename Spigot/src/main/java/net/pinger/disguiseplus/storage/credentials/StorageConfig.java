package net.pinger.disguiseplus.storage.credentials;

public class StorageConfig {
    private final int connectionTimeout;
    private final int keepAlive;
    private final int minimumIdle;
    private final int maximumPoolSize;
    private final int maximumLifetime;
    private final String driverClassName;
    private final String customJdbcUrl;

    /**
     * This method creates a new database {@link StorageConfig config} with the given
     * database pool settings.
     *
     * @param connectionTimeout the connection timeout
     * @param keepAlive the keep alive time
     * @param minimumIdle the minimumIdle time
     * @param maximumPoolSize the maximum size of the pool
     * @param maximumLifetime the maximum lifetime of a single connection to the database
     */

    public StorageConfig(int connectionTimeout, int keepAlive, int minimumIdle, int maximumPoolSize, int maximumLifetime, String driverClassName, String customJdbcUrl) {
        this.connectionTimeout = connectionTimeout;
        this.keepAlive = keepAlive;
        this.minimumIdle = minimumIdle;
        this.maximumPoolSize = maximumPoolSize;
        this.maximumLifetime = maximumLifetime;
        this.driverClassName = driverClassName;
        this.customJdbcUrl = customJdbcUrl;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public int getKeepAlive() {
        return this.keepAlive;
    }

    public int getMinimumIdle() {
        return this.minimumIdle;
    }

    public int getMaximumPoolSize() {
        return this.maximumPoolSize;
    }

    public int getMaximumLifetime() {
        return this.maximumLifetime;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public boolean hasDriverClassName() {
        return this.driverClassName != null && !this.driverClassName.isEmpty();
    }

    public String getCustomJdbcUrl() {
        return this.customJdbcUrl;
    }

    public boolean hasCustomJdbcUrl() {
        return this.customJdbcUrl != null && !this.customJdbcUrl.isEmpty();
    }

}
