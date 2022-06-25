package net.pinger.disguiseplus.internal.user;

import net.pinger.common.lang.Maps;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.User;
import net.pinger.disguiseplus.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManagerImpl implements UserManager {

    private final DisguisePlus dp;
    private final Map<UUID, UserImpl> users = Maps.newHashMap();

    public UserManagerImpl(DisguisePlus dp) {
        this.dp = dp;

        for (Player p : Bukkit.getOnlinePlayers()) {
            this.createPlayer(p.getUniqueId());
        }
    }

    public UserImpl createPlayer(UUID id) {
        return this.users.putIfAbsent(id, new UserImpl(this.dp, id));
    }

    @Override
    public net.pinger.disguiseplus.user.User getUser(UUID id) {
        return this.users.get(id);
    }

    @Override
    public net.pinger.disguiseplus.user.User getUser(Player player) {
        return this.users.get(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        this.users.remove(player.getUniqueId());
    }

    @Override
    public List<? extends net.pinger.disguiseplus.user.User> getOnlinePlayers() {
        List<net.pinger.disguiseplus.user.User> users = new LinkedList<>();

        // Loop through the players
        for (Player p : Bukkit.getOnlinePlayers()) {
            User s = this.getUser(p.getUniqueId());

            if (s != null)
                users.add(s);
        }

        return users;
    }

    public Collection<UserImpl> getUsers() {
        return this.users.values();
    }
}
