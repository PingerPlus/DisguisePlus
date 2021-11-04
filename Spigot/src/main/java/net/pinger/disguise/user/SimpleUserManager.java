package net.pinger.disguise.user;

import net.pinger.common.lang.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SimpleUserManager implements UserManager {

    private final Map<UUID, SimpleUser> users = Maps.newHashMap();

    public SimpleUser createPlayer(UUID id) {
        return this.users.putIfAbsent(id, new SimpleUser(id));
    }

    @Override
    public User getUser(UUID id) {
        return this.users.get(id);
    }

    @Override
    public User getUser(Player player) {
        return this.users.get(player.getUniqueId());
    }

    @Override
    public List<? extends User> getOnlinePlayers() {
        List<User> users = new LinkedList<>();

        // Loop through the players
        for (Player p : Bukkit.getOnlinePlayers()) {
            User s = this.getUser(p.getUniqueId());

            if (s != null)
                users.add(s);
        }

        return users;
    }
}