package net.pinger.disguiseplus.user;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class DisguisePlayerManager {

    private final DisguisePlus dp;
    private final Map<UUID, DisguiseUser> users;

    public DisguisePlayerManager(DisguisePlus dp) {
        this.dp = dp;
        this.users = Collections.synchronizedMap(new HashMap<>());

        for (Player p : Bukkit.getOnlinePlayers()) {
            final DisguiseUser user = this.dp.getStorage().loadUser(p.getUniqueId()).join();
            this.users.put(p.getUniqueId(), user);

            // Send update packets for this player
            // This might need to happen when we restart
            final PlayerMeta meta = user.getActiveMeta();
            if (meta == null) {
                continue;
            }

            this.dp.getProvider().updatePlayer(p, meta.getSkin(), meta.getName());

            if (meta.getRank() != null) {
                this.dp.getVaultManager().setPrefix(p, meta.getRank());
            }
        }
    }

    public DisguiseUser getUser(UUID id) {
        return this.users.get(id);
    }

    public DisguiseUser getUser(Player player) {
        return this.users.get(player.getUniqueId());
    }

    public Map<UUID, DisguiseUser> getUserMap() {
        return this.users;
    }
}
