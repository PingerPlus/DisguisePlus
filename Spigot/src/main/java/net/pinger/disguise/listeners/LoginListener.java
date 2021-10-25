package net.pinger.disguise.listeners;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.DisguisePlusAPI;
import net.pinger.disguise.skin.SkinPack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

public class LoginListener implements Listener {

    private final DisguisePlus dp;

    public LoginListener(DisguisePlus dp) {
        this.dp = dp;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        // Player of the event
        Player p = event.getPlayer();

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (SkinPack pack : DisguisePlusAPI.getSkinFactory().getSkinPacks()) {
            event.getPlayer().getInventory().addItem(pack.getSkins().get(0).toSkull());
        }
    }

}
