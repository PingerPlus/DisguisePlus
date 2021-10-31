package net.pinger.disguise.manager.implementation;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.exceptions.InvalidUrlException;
import net.pinger.disguise.exceptions.InvalidUserException;
import net.pinger.disguise.manager.SimpleDisguiseManager;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguise.skin.Skin;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class BaseDisguiseManager extends SimpleDisguiseManager {

    public BaseDisguiseManager(DisguisePlus dp, PacketProvider<?> provider) {
        super(dp, provider);
    }

    @Override
    public void applySkinFromPlayer(Player player, String playerName) throws InvalidUserException {
        super.applySkinFromPlayer(player, playerName);
    }

    @Override
    public void applyNickname(Player player, String name) {
        super.applyNickname(player, name);
    }

    @Override
    public void applySkin(Player player, @Nonnull Skin skin) {
        super.applySkin(player, skin);
    }

    @Override
    public void applySkinFromUrl(Player player, String url) throws InvalidUrlException {
        super.applySkinFromUrl(player, url);
    }

    @Override
    public void disguise(Player player) {
        super.disguise(player);
    }

    @Override
    public void resetNickname(Player player) {
        super.resetNickname(player);
    }

    @Override
    public void undisguise(Player player) {
        super.undisguise(player);
    }
}
