package net.pinger.disguiseplus.internal.user;

import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.User;
import net.pinger.disguiseplus.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManagerImpl implements UserManager {

    private final DisguisePlus dp;
    private final Map<UUID, UserImpl> users = new HashMap<>();

    public UserManagerImpl(DisguisePlus dp) {
        this.dp = dp;

        for (Player p : Bukkit.getOnlinePlayers()) {
            UserImpl user = new UserImpl(this.dp, p.getUniqueId());

            // Add it to the map
            this.users.put(p.getUniqueId(), user);

            // Send update packets for this player
            // This might need to happen
            // When we need to reset
            // The player name
            DisguiseAPI.getProvider().sendServerPackets(p);
        }
    }

    public UserImpl createPlayer(UUID id) {
        return this.users.putIfAbsent(id, new UserImpl(this.dp, id));
    }

    @Override
    public UserImpl getUser(UUID id) {
        return this.users.get(id);
    }

    @Override
    public UserImpl getUser(Player player) {
        return this.users.get(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        this.users.remove(player.getUniqueId());
    }

    @Override
    public List<? extends User> getOnlinePlayers() {
        List<User> users = new LinkedList<>();

        // Loop through the players
        for (Player p : Bukkit.getOnlinePlayers()) {
            User s = this.getUser(p.getUniqueId());

            if (s != null) {
                users.add(s);
            }
        }

        return users;
    }

    public Collection<UserImpl> getUsers() {
        return this.users.values();
    }
}
