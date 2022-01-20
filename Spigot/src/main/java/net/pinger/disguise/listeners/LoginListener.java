package net.pinger.disguise.listeners;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.DisguisePlusAPI;
import net.pinger.disguise.skin.SkinPack;
import net.pinger.disguise.user.SimpleUser;
import net.pinger.disguise.user.SimpleUserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;

public class LoginListener implements Listener {

    private final DisguisePlus dp;

    public LoginListener(DisguisePlus dp) {
        this.dp = dp;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        // Player of the event
        SimpleUser s = (SimpleUser) this.dp.getUserManager()
                .getUser(event.getPlayer().getUniqueId());

        // Set the name
        s.setDefaultName(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        SimpleUser s = ((SimpleUserManager) this.dp.getUserManager())
                .createPlayer(event.getUniqueId());

        // Here we will load the user data
        s.retrieveInformation();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        SimpleUser s = (SimpleUser) this.dp.getUserManager()
                .getUser(event.getPlayer().getUniqueId());

        // Save the data
        s.saveInformation();

        // Remove the player from the users
        ((SimpleUserManager) this.dp.getUserManager()).removePlayer(event.getPlayer());
    }

}
