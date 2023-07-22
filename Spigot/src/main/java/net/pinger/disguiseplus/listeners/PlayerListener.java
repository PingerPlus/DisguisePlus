package net.pinger.disguiseplus.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.event.PlayerDisguiseEvent;
import net.pinger.disguiseplus.event.PlayerRemoveDisguiseEvent;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.internal.user.UserManagerImpl;
import net.pinger.disguiseplus.tab.TabIntegration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final DisguisePlus dp;

    public PlayerListener(DisguisePlus dp) {
        this.dp = dp;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        // Player of the event
        UserImpl s = this.dp.getUserManager().getUser(event.getPlayer().getUniqueId());
        s.setDefaultName(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UserManagerImpl manager = this.dp.getUserManager();
        manager.createPlayer(event.getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        this.dp.getUserManager().removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onDisguise(PlayerDisguiseEvent event) {
        // Get the player
        Player player = event.getPlayer();

        if (this.dp.getChat() == null) {
            return;
        }

        // Check whether the integration is enabled
        // If false, cancel
        if (!this.dp.getTabIntegration().isEnabled()) {
            return;
        }

        TabIntegration tab = this.dp.getTabIntegration();

        // The string to parse
        String parsed = tab.getPrefix();

        // Parse the string here
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            parsed = PlaceholderAPI.setPlaceholders(player, parsed);
        }

        this.dp.getChat().setPlayerPrefix(player, parsed);

        // Check if the tab plugin is enabled
        if (!Bukkit.getPluginManager().isPluginEnabled("TAB")) {
            return;
        }

        TabAPI api = TabAPI.getInstance();

        // Get player from player id
        TabPlayer tabPlayer = api.getPlayer(player.getUniqueId());

        // Add to tab-list formatter and scoreboard manager
        api.getTablistFormatManager().setPrefix(tabPlayer, parsed);
        api.getTeamManager().setPrefix(tabPlayer, parsed);
    }

    @EventHandler
    public void onRemoveDisguise(PlayerRemoveDisguiseEvent event) {
        // Get the player
        Player player = event.getPlayer();

        if (this.dp.getChat() == null) {
            return;
        }

        // Reset the player prefix
        this.dp.getChat().setPlayerPrefix(player, null);

        // Check whether the integration is enabled
        // If false, cancel
        if (!this.dp.getTabIntegration().isEnabled())
            return;

        // Check if the tab plugin is enabled
        if (!Bukkit.getPluginManager().isPluginEnabled("TAB")) {
            return;
        }

        TabAPI api = TabAPI.getInstance();

        // Get player from player id
        TabPlayer tabPlayer = api.getPlayer(player.getUniqueId());

        // Reset
        api.getTablistFormatManager().resetPrefix(tabPlayer);
        api.getTeamManager().resetPrefix(tabPlayer);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (this.dp.getChat() == null) {
            return;
        }

        // Reset the thing
        // Reset the player prefix
        this.dp.getChat().setPlayerPrefix(player, null);

        // Check whether the integration is enabled
        // If false, cancel
        if (!this.dp.getTabIntegration().isEnabled())
            return;

        // Check if the tab plugin is enabled
        if (!Bukkit.getPluginManager().isPluginEnabled("TAB")) {
            return;
        }

        TabAPI api = TabAPI.getInstance();

        // Get player from player id
        TabPlayer tabPlayer = api.getPlayer(player.getUniqueId());

        // Reset
        api.getTablistFormatManager().resetPrefix(tabPlayer);
        api.getTeamManager().resetPrefix(tabPlayer);
    }

}
