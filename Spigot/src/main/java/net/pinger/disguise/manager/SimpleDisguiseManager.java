package net.pinger.disguise.manager;

import net.pinger.disguise.DisguiseManager;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.DisguisePlusAPI;
import net.pinger.disguise.exceptions.InvalidUrlException;
import net.pinger.disguise.exceptions.InvalidUserException;
import net.pinger.disguise.manager.nick.SimpleNickSetter;
import net.pinger.disguise.manager.skin.SkinFetcher;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguise.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.omg.CORBA.DynAnyPackage.Invalid;

import javax.annotation.Nonnull;

public class SimpleDisguiseManager implements DisguiseManager {

    private final DisguisePlus dp;
    private final PacketProvider<?> provider;

    public SimpleDisguiseManager(DisguisePlus dp, PacketProvider<?> provider) {
        this.dp = dp;
        this.provider = provider;
    }

    /**
     * This method disguises a specific player.
     * <p>
     * It loads a random skin immediately and applies it directly to the player.
     *
     * @param player the player
     */

    @Override
    public void disguise(Player player) {
        this.applySkin(player, DisguisePlusAPI.getSkinFactory().getRandomSkin());

        // Apply a random nick aswell
        this.applyNickname(player,
                this.dp.getSettings().getCreator().createNick());
    }

    /**
     * Removes any skin that this player might have on them.
     *
     * @param player the player
     */

    @Override
    public void undisguise(Player player) {
        this.provider.clearProperties(player);

        if (this.dp.getSettings().isOnlineMode()) {
            // Check if the UUID matches to the player name
        }

        this.provider.sendServerPackets(player);
    }

    /**
     * This method applies a skin that another player has.
     *
     * @param player     the player that we are playing the skin to
     * @param playerName the name of the player that the skin is referring to
     * @throws InvalidUserException if the user was not found by the search engine.
     */

    @Override
    public void applySkinFromPlayer(Player player, String playerName) throws InvalidUserException {
        Skin s = SkinFetcher.getSkin(playerName);

        if (s == null)
            throw new InvalidUserException();

        this.applySkin(player, s);
    }


    @Override
    public void applySkinFromUrl(Player player, String url) throws InvalidUrlException {
        // Check if this user is under cooldown
        SkinFetcher.catchSkin(url, skin -> this.applySkin(player, skin), this.dp);
    }

    @Override
    public void applySkin(Player player, @Nonnull Skin skin) {
        this.provider.updateProperties(player, skin);
        this.provider.sendServerPackets(player);
    }

    @Override
    public void applyNickname(Player player, String name) {
        // Check the nick conditions
        if (name.isEmpty() ||
                ChatColor.translateAlternateColorCodes('&', name).length() >= 16)
            throw new IllegalArgumentException("Length cannot be more or equal to 16");

        SimpleNickSetter.applyNick(player,
                ChatColor.translateAlternateColorCodes('&', name));
    }

    @Override
    public void resetNickname(Player player) {
        // Apply the players original nickname
        User s = this.dp.getUserManager().getUser(player);

        // Apply the nick
        this.applyNickname(player, s.getDefaultName());
    }
}
