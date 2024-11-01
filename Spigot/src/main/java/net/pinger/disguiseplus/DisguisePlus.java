package net.pinger.disguiseplus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.jonahseguin.drink.annotation.Sender;
import com.tchristofferson.configupdater.ConfigUpdater;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.DisguiseProvider;
import net.pinger.disguise.gson.GsonSkinAdapter;
import net.pinger.disguise.registration.RegistrySystem;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguise.skull.SkullManager;
import net.pinger.disguiseplus.adapter.SkinPackAdapter;
import net.pinger.disguiseplus.config.MessageConfiguration;
import net.pinger.disguiseplus.executors.*;
import net.pinger.disguiseplus.executors.drink.DisguiseUserProvider;
import net.pinger.disguiseplus.internal.SkinFactoryImpl;
import net.pinger.disguiseplus.libraries.DependencyManager;
import net.pinger.disguiseplus.rank.RankManager;
import net.pinger.disguiseplus.user.DisguiseUser;
import net.pinger.disguiseplus.user.DisguisePlayerManager;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.listeners.PlayerListener;
import net.pinger.disguiseplus.placeholders.DisguisePlusExpansion;
import net.pinger.disguiseplus.skin.SkinFactory;
import net.pinger.disguiseplus.skin.SkinPack;
import net.pinger.disguiseplus.storage.Storage;
import net.pinger.disguiseplus.storage.credentials.StorageConfig;
import net.pinger.disguiseplus.storage.credentials.StorageCredentials;
import net.pinger.disguiseplus.utils.ConversationUtil;
import net.pinger.disguiseplus.vault.VaultManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

public class DisguisePlus extends JavaPlugin implements Disguise {
    private static final Logger logger = LoggerFactory.getLogger("DisguisePlus");

    // Gson
    public static Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(SkinPack.class, new SkinPackAdapter())
            .registerTypeHierarchyAdapter(Skin.class, new GsonSkinAdapter())
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create();

    private Storage storage;
    private DisguiseProvider provider;
    private FeatureManager featureManager;
    private MessageConfiguration configuration;
    private SkinFactory skinFactory;
    private ConversationUtil conversation;
    private InventoryManager inventoryManager;
    private SkullManager skullManager;
    private DisguisePlayerManager userManager;
    private RankManager rankManager;
    private VaultManager vaultManager;

    @Override
    public void onEnable() {
        DisguisePlusAPI.setDisguise(this);

        // Load the config and register default events
        this.addDefaultConfig();
        final boolean baseSkins = this.getConfig().getBoolean("downloadBaseSkins");

        // Check if the PlaceholderAPI is enabled
        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new DisguisePlusExpansion(this).register();
        }

        // Check here if there is any need for downloading skins
        // If the provider was not found, it would just be a waste of time
        // To download skins, because this plugin doesn't have its functionality
        // Without the DisguiseAPI
        if (!DisguiseAPI.isEnabled()) {
            this.getLogger().info("Disabling since DisguiseAPI is disabled");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        new DependencyManager(this).loadDependencies();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.featureManager = new BukkitFeatureManager();
        this.rankManager = new RankManager(this);
        this.vaultManager = new VaultManager(this);

        if (!this.loadStorage()) {
            return;
        }

        this.provider = DisguiseAPI.createProvider(RegistrySystem.DEFAULT_REGISTRATION);

        // Load all modules here
        // Without downloading the skins
        this.userManager = new DisguisePlayerManager(this);
        this.configuration = new MessageConfiguration(this);
        this.conversation = new ConversationUtil(this);
        this.inventoryManager = new InventoryManager(this);
        this.skullManager = new SkullManager();
        this.skinFactory = new SkinFactoryImpl(this, baseSkins);
        this.vaultManager = new VaultManager(this);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // Load all the features
        this.featureManager.load();

        // Download skins at last
        // And register default commands after that
        this.skinFactory.downloadSkins();

        // Register all commands here
        final CommandService service = Drink.get(this);
        service.bind(DisguiseUser.class).annotatedWith(Sender.class).toProvider(new DisguiseUserProvider(this));
        service.register(new DisguisePlusExecutor(this), "dp");
        service.register(new NicknameExecutor(this),  "nickname", "setnick");
        service.register(new ResetNicknameExecutor(this), "unnick", "resetnick", "unnickname");
        service.register(new DisguiseExecutor(this), "d", "disguise");
        service.register(new ResetDisguiseExecutor(this), "und", "undisguise");
        service.register(new RepeatDisguiseExecutor(this), "rd", "redisguise");
        service.register(new SkinExecutor(this), "setskin");
        service.register(new ResetSkinExecutor(this), "resetskin", "unskin");
        service.registerCommands();

        // Create metrics
        new Metrics(this, 11053);
    }

    private void addDefaultConfig() {
        this.saveDefaultConfig();
        final File config = new File(this.getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(this, "config.yml", config, new ArrayList<>());
        } catch (Exception e) {
            getOutput().error("Failed to update the config: " + e.getMessage());
        }

        getOutput().info("Successfully loaded the config.yml");
        this.reloadConfig();
    }

    public boolean isDatabaseEnabled() {
        return this.storage != null;
    }

    private boolean loadStorage() {
        if (!this.getConfig().getBoolean("database.enabled")) {
            return true;
        }

        final ConfigurationSection section = this.getConfig().getConfigurationSection("database");
        if (section == null) {
            this.getLogger().log(Level.SEVERE, "Failed to create a database");
            this.getPluginLoader().disablePlugin(this);
            return false;
        }

        // Create the database credentials
        final StorageCredentials credentials = new StorageCredentials(
            section.getString("host"),
            section.getString("username"),
            section.getString("password")
        );

        final ConfigurationSection pool = section.getConfigurationSection("hikari-pool");
        final StorageConfig config = new StorageConfig(
            pool.getInt("connection-timeout"),
            pool.getInt("keep-alive-time"),
            pool.getInt("minimum-idle"),
            pool.getInt("maximum-pool-size"),
            pool.getInt("maximum-lifetime"),
            section.getString("driverClass"),
            section.getString("customJdbcUrl")
        );

        // Create the database
        try {
            this.storage = new Storage(this, credentials, config);
        } catch (Exception e) {
            this.getLogger().log(Level.SEVERE, "Failed to create a database", e);
            this.getPluginLoader().disablePlugin(this);
            return false;
        }

        return true;
    }

    @Override
    public void onDisable() {
        if (this.storage != null) {
            this.storage.shutdown();
        }

        if (this.conversation != null) {
            this.conversation.cancelAllConversations();
        }

        if (this.skinFactory != null) {
            this.skinFactory.saveSkins();
        }
    }

    public static Logger getOutput() {
        return logger;
    }

    public DisguiseProvider getProvider() {
        return this.provider;
    }

    public Storage getStorage() {
        return this.storage;
    }

    @Override
    public SkinFactory getSkinFactory() {
        return this.skinFactory;
    }

    public DisguisePlayerManager getUserManager() {
        return this.userManager;
    }

    public RankManager getRankManager() {
        return this.rankManager;
    }

    @Override
    public FeatureManager getFeatureManager() {
        return this.featureManager;
    }

    public ConversationUtil getConversation() {
        return this.conversation;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public MessageConfiguration getConfiguration() {
        return this.configuration;
    }

    public SkullManager getSkullManager() {
        return this.skullManager;
    }

    public VaultManager getVaultManager() {
        return this.vaultManager;
    }
}
