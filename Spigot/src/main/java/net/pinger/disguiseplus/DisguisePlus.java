package net.pinger.disguiseplus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.tchristofferson.configupdater.ConfigUpdater;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.skull.SkullManager;
import net.pinger.disguiseplus.adapter.SkinPackAdapter;
import net.pinger.disguiseplus.executors.DisguiseExecutor;
import net.pinger.disguiseplus.executors.DisguisePlusExecutor;
import net.pinger.disguiseplus.executors.NicknameExecutor;
import net.pinger.disguiseplus.executors.ResetNicknameExecutor;
import net.pinger.disguiseplus.file.configuration.BaseConfiguration;
import net.pinger.disguiseplus.internal.DisguiseManagerImpl;
import net.pinger.disguiseplus.internal.ExtendedDisguiseManager;
import net.pinger.disguiseplus.internal.PlayerMatcherImpl;
import net.pinger.disguiseplus.internal.SkinFactoryImpl;
import net.pinger.disguiseplus.internal.user.UserManagerImpl;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.listeners.PlayerListener;
import net.pinger.disguiseplus.user.User;
import net.pinger.disguiseplus.user.UserManager;
import net.pinger.disguiseplus.utils.ConversationUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
    private PlayerMatcher playerMatcher;
    private SkullManager skullManager;
    private ExtendedDisguiseManager extendedDisguiseManager;
    private DisguiseManager disguiseManager;
    private UserManagerImpl sum;

    @Override
    public void onEnable() {
        // Make sure that we created all instances
        // Of the api, before we connect to the api
        DisguisePlusAPI.setDisguise(this);

        // Check if the provider failed to load
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

        this.sum = new UserManagerImpl(this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        this.skullManager = new SkullManager();
        this.playerMatcher = new PlayerMatcherImpl();
        this.conversationUtil = new ConversationUtil(this);
        this.inventoryManager = new SimpleInventoryManager(this);
        this.configuration = new BaseConfiguration(this);
        this.skinFactory = new SkinFactoryImpl(this, getConfig().getBoolean("downloadBaseSkins"));
        this.disguiseManager = new DisguiseManagerImpl(this, DisguiseAPI.getProvider());
        this.extendedDisguiseManager = new ExtendedDisguiseManager(this, DisguiseAPI.getProvider());

        // Download skins
        this.skinFactory.downloadSkins();

        CommandService service = Drink.get(this);
        service.register(new DisguisePlusExecutor(this), "dp");
        service.register(new NicknameExecutor(this), "nick", "nickname");
        service.register(new ResetNicknameExecutor(this), "unnick", "resetnick", "unnickname");
        service.register(new DisguiseExecutor(this), "d", "disguise");
        service.registerCommands();
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
        // Reset nicknames for all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = this.getUserManager().getUser(player);
            getOutput().info(user.getDefaultName());
            this.getManager().resetNickname(player);
        }

        if (this.conversationUtil != null) {
            this.conversationUtil.cancelAllConversations();
        }

        if (this.skinFactory != null) {
            this.skinFactory.saveSkins();
        }
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

    public ExtendedDisguiseManager getExtendedManager() {
        return extendedDisguiseManager;
    }

    @Override
    public PlayerMatcher getPlayerMatcher() {
        return this.playerMatcher;
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
