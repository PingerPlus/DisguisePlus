package net.pinger.disguiseplus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.tchristofferson.configupdater.ConfigUpdater;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguise.skull.SkullManager;
import net.pinger.disguiseplus.adapter.SkinPackAdapter;
import net.pinger.disguiseplus.executors.*;
import net.pinger.disguiseplus.file.configuration.BaseConfiguration;
import net.pinger.disguiseplus.internal.DisguiseManagerImpl;
import net.pinger.disguiseplus.internal.ExtendedDisguiseManager;
import net.pinger.disguiseplus.internal.PlayerMatcherImpl;
import net.pinger.disguiseplus.internal.SkinFactoryImpl;
import net.pinger.disguiseplus.internal.user.UserManagerImpl;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.listeners.PlayerListener;
import net.pinger.disguiseplus.placeholders.DisguisePlusExpansion;
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
    private SkinFactory skinFactory;
    private ConversationUtil conversation;
    private InventoryManager inventoryManager;
    private PlayerMatcher playerMatcher;
    private SkullManager skullManager;
    private ExtendedDisguiseManager extendedDisguiseManager;
    private DisguiseManager disguiseManager;
    private UserManager userManager;
    private PlayerPrefix playerPrefix;

    @Override
    public void onEnable() {
        // Create a dependency to this instance
        DisguisePlusAPI.setDisguise(this);

        // Get the default provider for this server
        PacketProvider provider = DisguiseAPI.getProvider();

        // Load the config and register default events
        this.addDefaultConfig();
        boolean baseSkins = getConfig().getBoolean("downloadBaseSkins");

        // Check if the PlaceholderAPI is enabled
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new DisguisePlusExpansion(this).register();
        }

        // Load all modules here
        // Without downloading the skins
        this.userManager = new UserManagerImpl(this);
        this.configuration = new BaseConfiguration(this);
        this.conversation = new ConversationUtil(this);
        this.inventoryManager = new InventoryManager(this);
        this.skullManager = new SkullManager();
        this.playerMatcher = new PlayerMatcherImpl();
        this.disguiseManager = new DisguiseManagerImpl(this, provider);
        this.extendedDisguiseManager = new ExtendedDisguiseManager(this, provider);
        this.skinFactory = new SkinFactoryImpl(this, baseSkins);
        this.playerPrefix = new PlayerPrefix(getConfig().getConfigurationSection("display.prefix"));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // Check here if there is any need for downloading skins
        // If the provider was not found, it would just be a waste of time
        // To download skins, because this plugin doesn't have its functionality
        // Without the DisguiseAPI
        if (provider == null) {
            getOutput().info("FAILED TO FIND A PACKET PROVIDER!!!");
            getOutput().info("FAILED TO FIND A PACKET PROVIDER!!!");
            getOutput().info("FAILED TO FIND A PACKET PROVIDER!!!");

            // Disable the plugin
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        // Download skins at last
        // And register default commands after that
        this.skinFactory.downloadSkins();

        // Register all commands here
        CommandService service = Drink.get(this);
        service.register(new DisguisePlusExecutor(this), "dp");
        service.register(new NicknameExecutor(this), "nick", "nickname");
        service.register(new ResetNicknameExecutor(this), "unnick", "resetnick", "unnickname");
        service.register(new DisguiseExecutor(this), "d", "disguise");
        service.register(new ResetDisguiseExecutor(this), "und", "undisguise");
        service.register(new RepeatDisguiseExecutor(this), "rd", "redisguise");
        service.register(new SkinExecutor(this), "skin", "setskin");
        service.register(new ResetSkinExecutor(this), "resetskin", "unskin");
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
            this.getManager().resetNickname(player);
        }

        this.conversation.cancelAllConversations();
        this.skinFactory.saveSkins();
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
        return this.userManager;
    }

    public ExtendedDisguiseManager getExtendedManager() {
        return extendedDisguiseManager;
    }

    @Override
    public PlayerMatcher getPlayerMatcher() {
        return this.playerMatcher;
    }

    public PlayerPrefix getPlayerPrefix() {
        return playerPrefix;
    }

    public ConversationUtil getConversation() {
        return conversation;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public BaseConfiguration getConfiguration() {
        return configuration;
    }

    public SkullManager getSkullManager() {
        return skullManager;
    }
}
