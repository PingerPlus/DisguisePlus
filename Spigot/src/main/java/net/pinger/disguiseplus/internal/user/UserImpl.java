package net.pinger.disguiseplus.internal.user;

import java.util.UUID;
import javax.annotation.Nonnull;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.PlayerMeta;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.user.User;
import net.pinger.disguiseplus.utils.IndexedList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserImpl implements User {
    private final DisguisePlus dp;
    private final UUID id;
    private final IndexedList<PlayerMeta> meta;

    private Rank currentRank = null;

    public UserImpl(DisguisePlus dp, UUID id) {
        this(dp, id, new IndexedList<>());
   }

   public UserImpl(DisguisePlus dp, UUID id, IndexedList<PlayerMeta> meta) {
        this.dp = dp;
        this.id = id;
        this.meta = meta;
   }

    /**
     * Returns the uuid of this player.
     *
     * @return the uuid
     */

    @Nonnull
    @Override
    public UUID getId() {
        return this.id;
    }

    @Nonnull
    @Override
    public String getName() {
        // TODO: Fix this
        return this.transform().getName();
    }

    @Override
    public Rank getCurrentRank() {
        return this.currentRank;
    }

    @Override
    public boolean isDisguised() {
        return false;
    }

    @Override
    public void setRank(Rank rank) {
        this.currentRank = rank;
    }

    /**
     * Transforms the current user to a player.
     *
     * @return the player
     */

    @Override
    public Player transform() {
        return Bukkit.getPlayer(this.id);
    }

    public PlayerMeta getActiveMeta() {
        final PlayerMeta meta = this.meta.lastSafe();
        if (meta == null || meta.getEndTime() != null) {
            return null;
        }
        return meta;
    }

    public IndexedList<PlayerMeta> getMeta() {
        return this.meta;
    }

    /**
     * This method sends a message within the messages.yml file of the plugin.
     *
     * @param key the key of the message
     */

    @Override
    public void sendMessage(String key) {
        if (this.transform() == null || !this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().of(key);
        this.transform().sendMessage(message);
    }

    /**
     * This method sends a formatted message from the configuration file
     * to this player.
     * <p>
     * Do not do this if you want to send a message that is not within the messages.yml file.
     *
     * @param key    they key from the config
     * @param format the objects to format
     */

    @Override
    public void sendMessage(String key, Object... format) {
        if (this.transform() == null || !this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().ofFormatted(key, format);
        this.transform().sendMessage(message);
    }

    /**
     * This messages sends a raw message to this player
     * from the configuration.
     *
     * @param key the key from the config
     */

    @Override
    public void sendRawMessage(String key) {
        if (this.transform() == null || !this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().of(key);
        this.transform().sendRawMessage(message);
    }

    /**
     * This method sends a raw formatted message from the configuration file
     * to this player.
     * <p>
     * Do not do this if you want to send a message that is not within the messages.yml file.
     *
     * @param key    they key from the config
     * @param format the objects to format
     */

    @Override
    public void sendRawMessage(String key, Object... format) {
        if (this.transform() == null || !this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().ofFormatted(key, format);
        this.transform().sendRawMessage(message);
    }
}
