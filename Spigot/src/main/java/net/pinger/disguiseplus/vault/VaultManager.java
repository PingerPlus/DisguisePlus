package net.pinger.disguiseplus.vault;

import me.clip.placeholderapi.PlaceholderAPI;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import net.milkbowl.vault.chat.Chat;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultManager {

    private final DisguisePlus plus;
    private final boolean vaultEnabled;
    private final boolean autoRank;
    private final boolean tabEnabled;

    private Chat chat;
    private TabAPI tab;

    public VaultManager(DisguisePlus plus) {
        this.plus = plus;
        this.autoRank = this.plus.getConfig().getBoolean("tab.autoRank", true);
        this.tabEnabled = Bukkit.getPluginManager().isPluginEnabled("TAB");
        this.vaultEnabled = Bukkit.getPluginManager().isPluginEnabled("Vault");

        // If vault is enabled, grab the api
        if (this.vaultEnabled) {
            RegisteredServiceProvider<Chat> reg = plus.getServer().getServicesManager().getRegistration(Chat.class);
            if (reg != null) {
                this.chat = reg.getProvider();
            }
        }

        if (this.tabEnabled) {
            this.tab = TabAPI.getInstance();

            // Add tab listener
            this.tab.getEventBus().register(PlayerLoadEvent.class, e -> this.onPlayerLoad(e.getPlayer()));
        }
    }

    public void onPlayerLoad(TabPlayer tp) {
        Player player = Bukkit.getPlayer(tp.getUniqueId());
        User dp = this.plus.getUserManager().getUser(player.getUniqueId());

        // If for any reason the player is null
        if (dp == null) {
            return;
        }

        // The string to parse
        Rank rank = dp.getCurrentRank();
        if (rank == null) {
            return;
        }

        final String rankName = PlaceholderAPI.setPlaceholders(player, rank.getDisplayName());
        if (this.autoRank) {
            this.tab.getTabListFormatManager().setPrefix(tp, rankName);
            this.tab.getNameTagManager().setPrefix(tp, rankName);
        }

        tp.setTemporaryGroup(rank.getName());
    }

    public void setPrefix(Player player, Rank rank) {
        if (!this.vaultEnabled && !this.tabEnabled) {
            return;
        }

        if (this.chat != null) {
            this.chat.setPlayerPrefix(player, rank.getDisplayName());
        }

        if (this.tab != null) {
            TabPlayer tp = this.tab.getPlayer(player.getUniqueId());

            if (tp == null || !tp.isLoaded()) {
                return;
            }

            // Check if the player isn't null and apply the name
            if (this.autoRank) {
                this.tab.getTabListFormatManager().setPrefix(tp, rank.getDisplayName());
                this.tab.getNameTagManager().setPrefix(tp, rank.getDisplayName());
            }

            tp.setTemporaryGroup(rank.getName());
        }
    }

    public void resetPrefix(Player player) {
        if (!this.vaultEnabled && !this.tabEnabled) {
            return;
        }

        if (this.chat != null) {
            this.chat.setPlayerPrefix(player, null);
        }

        if (this.tab != null) {
            TabPlayer tp = this.tab.getPlayer(player.getUniqueId());
            if (tp == null || !tp.isLoaded()) {
                return;
            }

            // Check if the player isn't null and apply the name
            if (this.autoRank) {
                this.tab.getTabListFormatManager().setPrefix(tp, null);
                this.tab.getNameTagManager().setPrefix(tp, null);
            }

            tp.setTemporaryGroup(null);
        }
    }

}
