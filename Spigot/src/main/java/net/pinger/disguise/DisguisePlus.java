package net.pinger.disguise;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.tchristofferson.configupdater.ConfigUpdater;
import net.pinger.bukkit.plugin.BukkitPlugin;
import net.pinger.common.lang.Lists;
import net.pinger.disguise.configuration.BaseConfiguration;
import net.pinger.disguise.database.Database;
import net.pinger.disguise.database.settings.DatabaseSettings;
import net.pinger.disguise.executors.DatabaseExecutor;
import net.pinger.disguise.executors.NickExecutor;
import net.pinger.disguise.factory.SimpleSkinFactory;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.listeners.LoginListener;
import net.pinger.disguise.manager.implementation.BaseDisguiseManager;
import net.pinger.disguise.packet.PacketContext;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguise.settings.DisguiseSettings;
import net.pinger.disguise.skull.SkullManager;
import net.pinger.disguise.internal.user.UserImpl;
import net.pinger.disguise.internal.user.UserManagerImpl;
import net.pinger.disguise.user.UserManager;
import net.pinger.disguise.utils.ConversationUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DisguisePlus extends JavaPlugin implements Disguise {

    private static final Logger logger = LoggerFactory.getLogger("DisguisePlus");

    private final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create();

    private BaseConfiguration configuration;
    private SimpleSkinFactory skinFactory;
    private ConversationUtil conversationUtil;
    private Database database;
    private SimpleInventoryManager inventoryManager;
    private DisguiseSettings settings = new DisguiseSettings(this);
    private final SkullManager skullManager = new SkullManager();
    private UserManagerImpl sum;
    private BaseDisguiseManager customManager;

    private PacketProvider<?> provider;

    @Override
    public void onEnable() {
        // Make sure to register the custom provider
        DateTimeZone.setProvider(new UTCProvider());

        // Load the config
        this.addDefaultConfig();

        try {
            this.provider = PacketContext.getCorrespondingProvider();
        } catch (Exception e) {
            logger.error(" ", e);

            // Disable this plugin
            this.getPluginLoader().disablePlugin(this);
        }

        this.database = new Database(this, DatabaseSettings.create(this.getConfig()));
        this.conversationUtil = new ConversationUtil(this);
        this.inventoryManager = new SimpleInventoryManager(this);
        this.skinFactory = new SimpleSkinFactory(this);
        this.configuration = new BaseConfiguration(this);
        this.sum = new UserManagerImpl(this);

        CommandService service = Drink.get(this);

        // Registering the commands
        service.register(new DatabaseExecutor(this), "dp");
        service.register(new NickExecutor(this), "nick");
        service.registerCommands();

        Bukkit.getPluginManager().registerEvents(new LoginListener(this), this);
        this.customManager = new BaseDisguiseManager(this, this.provider);

        // Make sure that we created all instances
        // Of the api, before we connect to the api
        DisguisePlusAPI.setDisguise(this);
    }

    private void addDefaultConfig() {
        saveDefaultConfig();
        File config = new File(getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(this, "config.yml", config, Lists.newArrayList());
        } catch (Exception e) {
            BukkitPlugin.getPluginLogger().error("Failed to tasks the config: " + e.getMessage());
        }

        this.reloadConfig();
    }

    @Override
    public void onDisable() {
        if (this.conversationUtil != null) {
            this.conversationUtil.cancelAllConversations();
        }

        if (this.settings != null) {
            this.getSettings().saveConfig();
        }

        logger.info("Successfully saved all local settings.");

        if (this.skinFactory != null) {
            this.skinFactory.saveLocally();
        }

        logger.info("Successfully saved the skin factory.");

        if (this.sum != null) {
            // Save the information
            for (UserImpl user : this.sum.getUsers()) {
                user.saveInformation();
            }
        }

        logger.info("Successfully saved all user data.");
    }

    @Override
    public PacketProvider<?> getPacketProvider() {
        return provider;
    }

    @Override
    public SkinFactory getSkinFactory() {
        return this.skinFactory;
    }

    @Override
    public DisguiseManager getManager() {
        return null;
    }

    @Override
    public UserManager getUserManager() {
        return this.sum;
    }

    public ConversationUtil getConversationUtil() {
        return conversationUtil;
    }

    public Database getSQLDatabase() {
        return database;
    }

    public SimpleInventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public Gson getGson() {
        return gson;
    }

    public DisguiseSettings getSettings() {
        return settings;
    }

    public SkullManager getSkullManager() {
        return skullManager;
    }

    public BaseConfiguration getConfiguration() {
        return configuration;
    }

    public BaseDisguiseManager getBaseManager() {
        return customManager;
    }
}
