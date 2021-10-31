package net.pinger.disguise.update;

import net.pinger.bukkit.plugin.PluginResource;
import net.pinger.bukkit.utilities.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Stream;

public class UpdateTask extends BukkitRunnable {

    private final PluginResource resource;

    public UpdateTask(PluginResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        Stream<Player> players = PlayerUtil.getPlayersWithPermissions("disguise.update").stream();

        this.resource.checkVersion(() -> {
            players.forEach(player -> {
                player.sendMessage(ChatColor.YELLOW + "Disguise is using the latest version.");
            });
        }, () -> {
            players.forEach(player -> {
                player.sendMessage(ChatColor.YELLOW + "An tasks is available for Disguise.");
            });
        });
    }
}
