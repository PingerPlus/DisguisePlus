package net.pinger.disguiseplus;

import net.pinger.disguise.Skin;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface DisguiseManager {

    void applySkin(Player player, @Nonnull Skin skin);

    void applySkin(UUID id, @Nonnull Skin skin);

    void applySkinFromName(Player player, @Nonnull String playerName);

    void applySkinFromUrl(Player player, @Nonnull String url);

    void disguise(Player player);

    void undisguise(Player player);
}
