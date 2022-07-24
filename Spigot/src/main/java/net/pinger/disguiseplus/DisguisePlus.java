package net.pinger.disguiseplus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.tchristofferson.configupdater.ConfigUpdater;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.skull.SkullManager;
import net.pinger.disguiseplus.adapter.SkinPackAdapter;
import net.pinger.disguiseplus.executors.DisguisePlusExecutor;
import net.pinger.disguiseplus.file.configuration.BaseConfiguration;
import net.pinger.disguiseplus.internal.DisguiseManagerImpl;
import net.pinger.disguiseplus.internal.SkinFactoryImpl;
import net.pinger.disguiseplus.internal.user.UserManagerImpl;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.listeners.LoginListener;
import net.pinger.disguiseplus.user.UserManager;
import net.pinger.disguiseplus.utils.ConversationUtil;
import org.bukkit.Bukkit;
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
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create();

    private BaseConfiguration configuration;
    private SkinFactoryImpl skinFactory;
    private ConversationUtil conversationUtil;
    private SimpleInventoryManager inventoryManager;
    private final SkullManager skullManager = new SkullManager();
    private DisguiseManager disguiseManager;
    private UserManagerImpl sum;

    @Override
    public void onEnable() {
        // Make sure that we created all instances
        // Of the api, before we connect to the api
        DisguisePlusAPI.setDisguise(this);

        // Make sure to register the custom provider
//        DateTimeZone.setProvider(new UTCProvider());
        if (DisguiseAPI.getProvider() == null) {
            getOutput().info("FAILED TO FIND A PACKET PROVIDER!!!");
            getOutput().info("FAILED TO FIND A PACKET PROVIDER!!!");
            getOutput().info("FAILED TO FIND A PACKET PROVIDER!!!");

            // Disable the plugin
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        // Load the config
        this.addDefaultConfig();

        this.conversationUtil = new ConversationUtil(this);
        this.inventoryManager = new SimpleInventoryManager(this);
        this.configuration = new BaseConfiguration(this);
        this.sum = new UserManagerImpl(this);
        this.skinFactory = new SkinFactoryImpl(this, getConfig().getBoolean("downloadBaseSkins"));
        this.disguiseManager = new DisguiseManagerImpl(this, DisguiseAPI.getProvider());

        // Download skins
        this.skinFactory.downloadSkins();

        CommandService service = Drink.get(this);
        service.register(new DisguisePlusExecutor(this), "dp");
        service.registerCommands();

        Bukkit.getPluginManager().registerEvents(new LoginListener(this), this);
    }

    private void addDefaultConfig() {
        saveDefaultConfig();
        File config = new File(getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(this, "config.yml", config, new ArrayList<>());
        } catch (Exception e) {
            getOutput().error("Failed to update the config: " + e.getMessage());
        }

        getOutput().info("Successfully loaded the config.yml");
        this.reloadConfig();
    }

    @Override
    public void onDisable() {
        if (this.conversationUtil != null) {
            this.conversationUtil.cancelAllConversations();
        }

        if (this.skinFactory != null) {
            this.skinFactory.saveSkins();
        }

//        if (this.sum != null) {
//            // Save the information for users
//        }
    }

    public static Logger getOutput() {
        return logger;
    }

    @Override
    public SkinFactory getSkinFactory() {
        return this.skinFactory;
    }

    @Override
    public DisguiseManager getManager() {
        return this.disguiseManager;
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

    public BaseConfiguration getConfiguration() {
        return configuration;
    }

    public SkullManager getSkullManager() {
        return skullManager;
    }
}
