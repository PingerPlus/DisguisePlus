package net.pinger.disguiseplus.listeners;

import java.time.LocalDateTime;
import net.md_5.bungee.api.ChatColor;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.user.DisguiseUser;
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
        final DisguiseUser user = this.dp.getStorage().loadUser(event.getUniqueId()).join();
        if (user == null) {
            return;
        }

        this.dp.getUserManager().getUserMap().put(user.getId(), user);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final DisguiseUser user = this.dp.getUserManager().getUser(player);
        if (user == null) {
            return;
        }

        this.dp.getStorage().savePlayer(user).join();
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
        this.dp.getUserManager().updatePlayer(player, meta);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        this.dp.getUserManager().forgetPlayer(event.getPlayer());
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

            final DisguiseUser user = this.dp.getUserManager().getUser(other);
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
