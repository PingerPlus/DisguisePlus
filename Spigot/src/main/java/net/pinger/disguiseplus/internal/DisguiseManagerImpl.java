package net.pinger.disguiseplus.internal;

import net.pinger.disguise.Skin;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguiseplus.DisguiseManager;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public class DisguiseManagerImpl implements DisguiseManager {

    protected final DisguisePlus dp;
    private final PacketProvider provider;

    public DisguiseManagerImpl(DisguisePlus dp, PacketProvider provider) {
        this.dp = dp;
        this.provider = provider;
    }

    @Override
    public void applySkin(Player player, @Nonnull Skin skin) {
        // First we need to apply the skin
        this.provider.updateProperties(player, skin);

        // Then we just need to send the needed packets to update
        this.provider.sendServerPackets(player);
    }

    @Override
    public void applySkin(UUID id, @Nonnull Skin skin) {

    }

    @Override
    public void applySkinFromName(Player player, @Nonnull String playerName) {

    }

    @Override
    public void applySkinFromUrl(Player player, @Nonnull String url) {

    }

    @Override
    public void disguise(Player player) {

    }

    @Override
    public void undisguise(Player player) {

    }
}
