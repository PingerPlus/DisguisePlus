package net.pinger.disguiseplus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.jonahseguin.drink.annotation.Sender;
import com.tchristofferson.configupdater.ConfigUpdater;
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
import net.pinger.disguiseplus.internal.DisguiseManagerImpl;
import net.pinger.disguiseplus.internal.SkinFactoryImpl;
import net.pinger.disguiseplus.internal.rank.RankManagerImpl;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.internal.user.UserManagerImpl;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.listeners.PlayerListener;
import net.pinger.disguiseplus.placeholders.DisguisePlusExpansion;
import net.pinger.disguiseplus.rank.RankManager;
import net.pinger.disguiseplus.skin.SkinFactory;
import net.pinger.disguiseplus.skin.SkinPack;
import net.pinger.disguiseplus.utils.ConversationUtil;
import net.pinger.disguiseplus.vault.VaultManager;
import org.bstats.bukkit.Metrics;
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
            .registerTypeHierarchyAdapter(Skin.class, new GsonSkinAdapter())
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create();

    private DisguiseProvider provider;
    private FeatureManager featureManager;
    private MessageConfiguration configuration;
    private SkinFactory skinFactory;
    private ConversationUtil conversation;
    private InventoryManager inventoryManager;
    private SkullManager skullManager;
    private DisguiseManager disguiseManager;
    private UserManagerImpl userManager;
    private PlayerPrefix playerPrefix;
    private RankManager rankManager;
    private VaultManager vaultManager;

    @Override
    public void onEnable() {
        DisguisePlusAPI.setDisguise(this);

        // Load the config and register default events
        this.addDefaultConfig();
        boolean baseSkins = getConfig().getBoolean("downloadBaseSkins");

        // Check if the PlaceholderAPI is enabled
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
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

        this.featureManager = new BukkitFeatureManager();
        this.provider = DisguiseAPI.createProvider(RegistrySystem.DEFAULT_REGISTRATION);

        // Load all modules here
        // Without downloading the skins
        this.userManager = new UserManagerImpl(this);
        this.configuration = new MessageConfiguration(this);
        this.conversation = new ConversationUtil(this);
        this.inventoryManager = new InventoryManager(this);
        this.skullManager = new SkullManager();
        this.disguiseManager = new DisguiseManagerImpl(this);
        this.skinFactory = new SkinFactoryImpl(this, baseSkins);
        this.playerPrefix = new PlayerPrefix(this);
        this.rankManager = new RankManagerImpl(this);
        this.vaultManager = new VaultManager(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // Load all the features
        this.featureManager.load();

        // Download skins at last
        // And register default commands after that
        this.skinFactory.downloadSkins();

        // Register all commands here
        CommandService service = Drink.get(this);
        service.bind(UserImpl.class).annotatedWith(Sender.class).toProvider(new DisguiseUserProvider(this));
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
        for (final Player player : Bukkit.getOnlinePlayers()) {
            this.getProvider().resetPlayerName(player);
        }

        this.conversation.cancelAllConversations();
        this.skinFactory.saveSkins();
    }

    public static Logger getOutput() {
        return logger;
    }

    public DisguiseProvider getProvider() {
        return this.provider;
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
    public UserManagerImpl getUserManager() {
        return this.userManager;
    }

    @Override
    public RankManager getRankManager() {
        return this.rankManager;
    }

    @Override
    public FeatureManager getFeatureManager() {
        return this.featureManager;
    }

    public PlayerPrefix getPlayerPrefix() {
        return this.playerPrefix;
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
        return vaultManager;
    }
}
