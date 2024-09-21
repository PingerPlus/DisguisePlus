package net.pinger.disguiseplus.vault;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import net.milkbowl.vault.chat.Chat;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.user.DisguiseUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultManager {
    private final DisguisePlus plus;
    private final boolean autoRank;

    private Chat chat;
    private TabAPI tab;

    public VaultManager(DisguisePlus plus) {
        this.plus = plus;
        this.autoRank = this.plus.getConfig().getBoolean("tab.autoRank", true);

        final boolean tabEnabled = Bukkit.getPluginManager().isPluginEnabled("TAB");
        final boolean vaultEnabled = Bukkit.getPluginManager().isPluginEnabled("Vault");
        if (vaultEnabled) {
            final RegisteredServiceProvider<Chat> reg = plus.getServer().getServicesManager().getRegistration(Chat.class);
            if (reg != null) {
                this.chat = reg.getProvider();
            }
        }

        if (tabEnabled) {
            this.tab = TabAPI.getInstance();
            if (this.tab.getEventBus() != null) {
                this.tab.getEventBus().register(PlayerLoadEvent.class, e -> this.onPlayerLoad(e.getPlayer()));
            }
        }
    }

    public void onPlayerLoad(TabPlayer tp) {
        final Player player = Bukkit.getPlayer(tp.getUniqueId());
        if (player == null) {
            return;
        }
        final DisguiseUser user = this.plus.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return;
        }

        final PlayerMeta meta = user.getActiveMeta();
        if (meta == null) {
            return;
        }

        final Rank rank = meta.getRank();
        this.resetPrefix(player);

        if (this.autoRank) {
            if (this.tab.getTabListFormatManager() != null) {
                this.tab.getTabListFormatManager().setPrefix(tp, rank.getDisplayName());
            }

            if (this.tab.getNameTagManager() != null) {
                this.tab.getNameTagManager().setPrefix(tp, rank.getDisplayName());
            }
        }

        tp.setTemporaryGroup(rank.getName());
    }

    public void setPrefix(Player player, Rank rank) {
        if (this.chat != null) {
            this.chat.setPlayerPrefix(player, rank.getDisplayName());
        }

        if (this.tab != null) {
            final TabPlayer tp = this.tab.getPlayer(player.getUniqueId());
            if (tp == null || !tp.isLoaded()) {
                return;
            }

            // Check if the player isn't null and apply the name
            if (this.autoRank) {
                if (this.tab.getTabListFormatManager() != null) {
                    this.tab.getTabListFormatManager().setPrefix(tp, rank.getDisplayName());
                }

                if (this.tab.getNameTagManager() != null) {
                    this.tab.getNameTagManager().setPrefix(tp, rank.getDisplayName());
                }
            }

            tp.setTemporaryGroup(rank.getName());
        }
    }

    public void resetPrefix(Player player) {
        if (this.chat != null) {
            this.chat.setPlayerPrefix(player, null);
        }

        if (this.tab != null) {
            final TabPlayer tp = this.tab.getPlayer(player.getUniqueId());
            if (tp == null || !tp.isLoaded()) {
                return;
            }

            // Check if the player isn't null and apply the name
            if (this.autoRank) {
                if (this.tab.getTabListFormatManager() != null) {
                    this.tab.getTabListFormatManager().setPrefix(tp, null);
                }

                if (this.tab.getNameTagManager() != null) {
                    this.tab.getNameTagManager().setPrefix(tp, null);
                }
            }

            tp.setTemporaryGroup(null);
        }
    }

}
