package net.pinger.disguiseplus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.pinger.disguise.skull.SkullManager;
import net.pinger.disguiseplus.executors.DisguisePlusExecutor;
import net.pinger.disguiseplus.file.configuration.BaseConfiguration;
import net.pinger.disguiseplus.internal.SkinFactoryImpl;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.listeners.LoginListener;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.internal.user.UserManagerImpl;
import net.pinger.disguiseplus.utils.ConversationUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DisguisePlus extends JavaPlugin implements Disguise {

    private static final Logger logger = LoggerFactory.getLogger("DisguisePlus");

    // Gson
    public static Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

    private BaseConfiguration configuration;
    private SkinFactoryImpl skinFactory;
    private ConversationUtil conversationUtil;
    private SimpleInventoryManager inventoryManager;
    private final SkullManager skullManager = new SkullManager();
    private UserManagerImpl sum;

    private PacketProvider<?> provider;

    @Override
    public void onEnable() {
        // Make sure to register the custom provider
        DateTimeZone.setProvider(new UTCProvider());

        // Load the config
        this.addDefaultConfig();

        this.conversationUtil = new ConversationUtil(this);
        this.inventoryManager = new SimpleInventoryManager(this);
        this.skinFactory = new SkinFactoryImpl(this, getConfig().getBoolean("downloadBaseSkins"));
        this.configuration = new BaseConfiguration(this);
        this.sum = new UserManagerImpl(this);

        CommandService service = Drink.get(this);

        // Registering the commands
        service.register(new DisguisePlusExecutor(this), "dp");
        service.registerCommands();

        Bukkit.getPluginManager().registerEvents(new LoginListener(this), this);

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
//                user.saveInformation();
            }
        }

        logger.info("Successfully saved all user data.");
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

    public SimpleInventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public DisguiseSettings getSettings() {
        return settings;
    }

    public BaseConfiguration getConfiguration() {
        return configuration;
    }

    public static Logger getOutput() {
        return logger;
    }


}
