package net.pinger.disguiseplus.listeners;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.event.PlayerDisguiseEvent;
import net.pinger.disguiseplus.event.PlayerRemoveDisguiseEvent;
import net.pinger.disguiseplus.internal.PlayerMeta;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.internal.user.UserManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final DisguisePlus dp;

    public PlayerListener(DisguisePlus dp) {
        this.dp = dp;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        final UserImpl user = this.dp.getStorage().loadUser(event.getUniqueId()).join();
        if (user == null) {
            return;
        }

        this.dp.getUserManager().getUserMap().put(user.getId(), user);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UserImpl user = this.dp.getUserManager().getUser(player);
        if (user == null) {
            return;
        }

        this.processUndisguise(player);

        final PlayerMeta meta = user.getActiveMeta();
        if (meta == null) {
            return;
        }

        // Check if any players have the same name as the meta name
        // If so cancel the join
        if (!this.shouldDisguise(player, meta)) {
            meta.setEndTime(LocalDateTime.now());
            user.sendMessage("nick.overlapping");
            return;
        }

        user.sendMessage("nick.join");
        this.dp.getProvider().updatePlayerSilently(player, meta.getSkin(), meta.getName());
        this.dp.getVaultManager().setPrefix(player, meta.getRank());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        this.dp.getUserManager().removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onDisguise(PlayerDisguiseEvent event) {
        // Get the player
        Player player = event.getPlayer();
        UserImpl user = this.dp.getUserManager().getUser(player);
        if (user.getCurrentRank() == null) {
            return;
        }

        this.dp.getVaultManager().setPrefix(player, user.getCurrentRank());
    }

    @EventHandler
    public void onRemoveDisguise(PlayerRemoveDisguiseEvent event) {
        this.dp.getVaultManager().resetPrefix(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        this.dp.getVaultManager().resetPrefix(event.getPlayer());
    }

    private boolean shouldDisguise(Player player, PlayerMeta meta) {
        // Check if this player should be disguised with the provided meta
        // This can only be true if the player meta doesn't match the player name
        final Player online = Bukkit.getPlayer(meta.getName());
        if (online != null) {
            return player.equals(online);
        }

        final OfflinePlayer op = Bukkit.getOfflinePlayer(meta.getName());
        return !op.hasPlayedBefore();
    }

    private void processUndisguise(Player player) {
        for (final Player other : Bukkit.getOnlinePlayers()) {
            if (player.equals(other)) {
                continue;
            }

            if (!player.getName().equalsIgnoreCase(other.getName())) {
                continue;
            }

            final UserImpl user = this.dp.getUserManager().getUser(other);
            final PlayerMeta meta = user.getActiveMeta();

            // Update the player data
            this.dp.getVaultManager().resetPrefix(other);
            this.dp.getProvider().resetPlayer(other);
            meta.setEndTime(LocalDateTime.now());

            // Send a message to the same user
            user.sendMessage("nick.overlapping");
        }
    }



}
