package net.pinger.disguise.listeners;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.user.SimpleUser;
import net.pinger.disguise.user.SimpleUserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        SimpleUserManager manager = ((SimpleUserManager) this.dp.getUserManager());
        manager.createPlayer(event.getUniqueId());

        // Get the user again
        SimpleUser s = (SimpleUser) manager.getUser(event.getUniqueId());

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
