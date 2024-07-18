package net.pinger.disguiseplus.internal.user;

import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.PlayerMeta;
import net.pinger.disguiseplus.user.User;
import net.pinger.disguiseplus.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManagerImpl implements UserManager {

    private final DisguisePlus dp;
    private final Map<UUID, UserImpl> users;

    public UserManagerImpl(DisguisePlus dp) {
        this.dp = dp;
        this.users = Collections.synchronizedMap(new HashMap<>());

        for (Player p : Bukkit.getOnlinePlayers()) {
            final UserImpl user = this.dp.getStorage().loadUser(p.getUniqueId()).join();
            this.users.put(p.getUniqueId(), user);

            // Send update packets for this player
            // This might need to happen when we restart
            final PlayerMeta meta = user.getActiveMeta();
            if (meta == null) {
                continue;
            }

            this.dp.getProvider().updatePlayer(p, meta.getSkin(), meta.getName());
            this.dp.getVaultManager().setPrefix(p, meta.getRank());
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

    public Map<UUID, UserImpl> getUserMap() {
        return this.users;
    }
}
