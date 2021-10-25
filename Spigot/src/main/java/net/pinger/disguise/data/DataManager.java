package net.pinger.disguise.data;

import net.pinger.common.lang.Lists;
import net.pinger.common.lang.Maps;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.user.SimpleUser;
import net.pinger.disguise.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private final Map<UUID, SimpleUser> users = Maps.newConcurrentHashMap();

    private final List<UUID> cooldown = Lists.newArrayList();
    private final List<UUID> mineskin = Lists.newArrayList();

    private final DisguisePlus dp;

    public DataManager(DisguisePlus dp) {
        this.dp = dp;
    }

    public boolean hasCooldown(User user) {
        return this.cooldown.contains(user.getId());
    }

    public boolean hasCooldown(UUID id) {
        return this.cooldown.contains(id);
    }

    public void addCooldown(UUID id) {
        // Just in case
        if (this.cooldown.contains(id))
            return;

        this.cooldown.add(id);

        // Remove it after x seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                cooldown.remove(id);
            }
        }.runTaskLater(this.dp, 0L);
    }

}
