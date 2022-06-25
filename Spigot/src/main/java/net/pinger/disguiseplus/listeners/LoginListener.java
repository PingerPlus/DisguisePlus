package net.pinger.disguiseplus.listeners;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.internal.user.UserManagerImpl;
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
        UserImpl s = (UserImpl) this.dp.getUserManager()
                .getUser(event.getPlayer().getUniqueId());

        // Set the name
        s.setDefaultName(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UserManagerImpl manager = ((UserManagerImpl) this.dp.getUserManager());
        manager.createPlayer(event.getUniqueId());

        // Get the user again
        UserImpl s = (UserImpl) manager.getUser(event.getUniqueId());

//        // Here we will load the user data
//        s.retrieveInformation();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        UserImpl s = (UserImpl) this.dp.getUserManager()
                .getUser(event.getPlayer().getUniqueId());

//        // Save the data
//        s.saveInformation();

        // Remove the player from the users
        ((UserManagerImpl) this.dp.getUserManager()).removePlayer(event.getPlayer());
    }

}
