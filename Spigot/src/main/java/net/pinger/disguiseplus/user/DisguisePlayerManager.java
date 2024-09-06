package net.pinger.disguiseplus.user;

import java.time.LocalDateTime;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.DisguisePlayer;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.meta.PlayerMeta.Builder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class DisguisePlayerManager {
    private final DisguisePlus dp;
    private final Map<UUID, DisguiseUser> users;

    public DisguisePlayerManager(DisguisePlus dp) {
        this.dp = dp;
        this.users = Collections.synchronizedMap(new HashMap<>());

        for (final Player p : Bukkit.getOnlinePlayers()) {
            final DisguiseUser user = this.dp.getStorage().loadUser(p.getUniqueId()).join();
            this.users.put(p.getUniqueId(), user);

            // Send update packets for this player
            // This might need to happen when we restart
            final PlayerMeta meta = user.getActiveMeta();
            if (meta == null) {
                continue;
            }

            this.updatePlayer(p, meta);
        }
    }

    public void disguise(DisguiseUser user) {
        final Builder meta = user.getMetaBuilder();
        if (meta == null) {
            return;
        }

        this.disguise(user, meta.build());
    }

    public void disguise(DisguiseUser user, PlayerMeta meta) {
        this.replacePlayerMeta(user, meta);
        this.updatePlayer(user.transform(), meta);
    }

    public void resetDisguise(DisguiseUser user) {
        this.resetActiveMeta(user);
        this.dp.getProvider().resetPlayer(user.transform());
        this.dp.getVaultManager().resetPrefix(user.transform());
    }

    public void updatePlayer(Player player, PlayerMeta meta) {
        if (meta.getRank() == null) {
            this.dp.getProvider().updatePlayer(player, meta.getSkin(), meta.getName());
            return;
        }

        this.dp.getProvider().updatePlayer(player, meta.getSkin(), meta.getName());
        this.dp.getVaultManager().setPrefix(player, meta.getRank());
    }

    public void replacePlayerMeta(DisguiseUser user, PlayerMeta newMeta) {
        this.resetActiveMeta(user);
        if (this.emptyMeta(DisguiseAPI.getDisguisePlayer(user.getId()), newMeta)) {
            return;
        }
        this.dp.getStorage().savePlayerMeta(user, newMeta).join();
        user.addMeta(newMeta);
    }

    private void resetActiveMeta(DisguiseUser user) {
        final PlayerMeta meta = user.getActiveMeta();
        if (meta == null) {
            return;
        }

        meta.setEndTime(LocalDateTime.now());
        this.dp.getStorage().savePlayerMeta(user, meta).join();
    }

    private boolean emptyMeta(DisguisePlayer player, PlayerMeta newMeta) {
        return player.getDefaultName().equals(newMeta.getName()) &&
               Objects.equals(player.getDefaultSkin(), newMeta.getSkin()) &&
               newMeta.getRank() == null;
    }

    public void forgetPlayer(Player player) {
        this.users.remove(player.getUniqueId());
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
