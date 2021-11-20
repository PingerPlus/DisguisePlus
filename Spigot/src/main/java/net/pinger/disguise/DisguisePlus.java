package net.pinger.disguise;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.tchristofferson.configupdater.ConfigUpdater;
import net.pinger.bukkit.plugin.BukkitPlugin;
import net.pinger.common.lang.Lists;
import net.pinger.disguise.data.DataManager;
import net.pinger.disguise.database.Database;
import net.pinger.disguise.database.settings.DatabaseSettings;
import net.pinger.disguise.exceptions.ProviderNotFoundException;
import net.pinger.disguise.executors.DatabaseExecutor;
import net.pinger.disguise.factory.SimpleSkinFactory;
import net.pinger.disguise.factory.SkinFactory;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.listeners.LoginListener;
import net.pinger.disguise.packet.PacketManager;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguise.settings.DisguiseSettings;
import net.pinger.disguise.skin.SimpleSkin;
import net.pinger.disguise.skin.SkullManager;
import net.pinger.disguise.user.SimpleUser;
import net.pinger.disguise.user.SimpleUserManager;
import net.pinger.disguise.user.UserManager;
import net.pinger.disguise.utils.ConversationUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;

import java.io.File;

public class DisguisePlus extends JavaPlugin implements Disguise {

    private final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create();

    private SimpleSkinFactory skinFactory;
    private ConversationUtil conversationUtil;
    private Database database;
    private SimpleInventoryManager inventoryManager;
    private DisguiseSettings settings = new DisguiseSettings(this);
    private final SkullManager skullManager = new SkullManager();
    private DataManager dataManager;
    private final SimpleUserManager sum = new SimpleUserManager();

    private PacketProvider<?> provider;

    @Override
    public void onEnable() {
        // Make sure to register the custom provider
        DateTimeZone.setProvider(new UTCProvider());

        // Load the config
        this.addDefaultConfig();

        this.conversationUtil = new ConversationUtil(this);
        this.database = new Database(this, DatabaseSettings.create(this.getConfig()));
        this.inventoryManager = new SimpleInventoryManager(this);
        this.skinFactory = new SimpleSkinFactory(this);
        this.dataManager = new DataManager(this);

        CommandService service = Drink.get(this);

        // Registering the commands
        service.register(new DatabaseExecutor(this), "dp");
        service.registerCommands();

        Bukkit.getPluginManager().registerEvents(new LoginListener(this), this);

        // Adding the provider
        try {
            provider = PacketManager.getApplicableProvider();
        } catch (ProviderNotFoundException e) {
            e.printStackTrace();
        }

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
        // Leave all of the conversations
        this.conversationUtil.cancelAllConversations();
        this.getSettings().saveConfig();

        // Save the skins
        this.skinFactory.saveLocally();
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

    public DataManager getDataManager() {
        return dataManager;
    }
}
