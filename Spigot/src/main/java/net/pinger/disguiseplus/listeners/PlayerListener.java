package net.pinger.disguiseplus.listeners;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.event.PlayerDisguiseEvent;
import net.pinger.disguiseplus.event.PlayerRemoveDisguiseEvent;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.internal.user.UserManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final DisguisePlus dp;

    public PlayerListener(DisguisePlus dp) {
        this.dp = dp;
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

}
