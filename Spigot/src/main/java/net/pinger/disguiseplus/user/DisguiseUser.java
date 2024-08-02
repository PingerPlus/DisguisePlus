package net.pinger.disguiseplus.user;

import java.util.UUID;
import javax.annotation.Nonnull;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.meta.PlayerMeta.Builder;
import net.pinger.disguiseplus.utils.IndexedList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DisguiseUser {
    private final DisguisePlus dp;
    private final UUID id;
    private final IndexedList<PlayerMeta> meta;

    private PlayerMeta.Builder metaBuilder;

    public DisguiseUser(DisguisePlus dp, UUID id) {
        this(dp, id, new IndexedList<>());
   }

   public DisguiseUser(DisguisePlus dp, UUID id, IndexedList<PlayerMeta> meta) {
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
    public UUID getId() {
        return this.id;
    }

    @Nonnull
    public String getName() {
        // TODO: Fix this
        return this.transform().getName();
    }

    /**
     * Transforms the current user to a player.
     *
     * @return the player
     */

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

    public PlayerMeta.Builder copyActiveMeta() {
        final PlayerMeta meta = this.getActiveMeta();
        if (meta == null) {
            return new Builder(this);
        }
        return Builder.copyOf(meta);
    }

    public void addMeta(PlayerMeta meta) {
        this.meta.add(meta);
    }

    public boolean isDisguised() {
        return this.getActiveMeta() != null;
    }

    public IndexedList<PlayerMeta> getMeta() {
        return this.meta;
    }

    public Builder getMetaBuilder() {
        return this.metaBuilder;
    }

    public void setMetaBuilder(Builder metaBuilder) {
        this.metaBuilder = metaBuilder;
    }

    public Builder newMetaBuilder() {
        final Builder builder = new Builder(this);
        this.setMetaBuilder(builder);
        return builder;
    }

    /**
     * This method sends a message within the messages.yml file of the plugin.
     *
     * @param key the key of the message
     */

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

    public void sendRawMessage(String key, Object... format) {
        if (this.transform() == null || !this.dp.getConfiguration().has(key)) {
            return;
        }

        // Get the message
        String message = this.dp.getConfiguration().ofFormatted(key, format);
        this.transform().sendRawMessage(message);
    }
}
