package net.pinger.disguise.manager.implementation;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.DisguisePlusAPI;
import net.pinger.disguise.exceptions.InvalidUrlException;
import net.pinger.disguise.exceptions.InvalidUserException;
import net.pinger.disguise.manager.SimpleDisguiseManager;
import net.pinger.disguise.manager.nick.NickFetcher;
import net.pinger.disguise.manager.nick.SimpleNickSetter;
import net.pinger.disguise.manager.skin.SkinFetcher;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguise.settings.DisguiseSettings;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguise.statistic.NickStatistic;
import net.pinger.disguise.statistic.SkinStatistic;
import net.pinger.disguise.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.IOException;

public class BaseDisguiseManager extends SimpleDisguiseManager {

    public BaseDisguiseManager(DisguisePlus dp, PacketProvider<?> provider) {
        super(dp, provider);
    }

    @Override
    public void applySkinFromPlayer(Player player, String playerName) {
        User user = this.dp.getUserManager().getUser(player);

        if (user.isDisguised()) {
            // Send the message to the user
            user.sendMessage("player.currently-disguised");
            return;
        }

        Skin fetched = SkinFetcher.getSkin(playerName);
        if (fetched == null) {
            // Not found
            user.sendMessage("skins.error-name");
            return;
        }

        // Add the statistic
        user.setStatistic(new SkinStatistic(user, true, fetched));

        user.sendMessage("player.success-skin-name", playerName);
        this.applySkin(player, fetched);
    }

    @Override
    public void applyNickname(Player player, String name) {
        User user = this.dp.getUserManager().getUser(player);
        DisguiseSettings set = this.dp.getSettings();

        if (user.isDisguised()) {
            // Send the message to the user
            user.sendMessage("player.currently-disguised");
            return;
        }

        if (!set.isNickValid(name)) {
            user.sendMessage("player.invalid-nick", set.getMin(), set.getMax());
            return;
        }

        user.setStatistic(new NickStatistic(user, true, name));
        this.applyNickname(player, name, false);

        // Send the success message
        user.sendMessage("player.success-name", name);
    }

    private void applyNickname(Player player, String name, boolean gen) {
        if (gen) {
            SimpleNickSetter.applyNick(player, ChatColor.translateAlternateColorCodes('&', name));
            return;
        }

        // Add to the nicks
        SimpleNickSetter.applyNick(player, ChatColor.translateAlternateColorCodes('&', name));
        PacketProvider.refreshPlayer(this.dp, player);

        // Send the packets
        this.dp.getPacketProvider().sendServerPackets(player);
    }


    @Override
    public void applySkin(Player player, @Nonnull Skin skin) {
        super.applySkin(player, skin);
    }

    @Override
    public void applySkinFromUrl(Player player, String url) {
        User user = this.dp.getUserManager().getUser(player);

        if (user.isDisguised()) {
            // Send the message to the user
            user.sendMessage("player.currently-disguised");
            return;
        }

        SkinFetcher.catchSkin(url, s -> {
            // Add the statistic
            user.setStatistic(new SkinStatistic(user, true, s));

            this.applySkin(player, s);

            // Send the success message
            user.sendMessage("player.success-skin-url");
        }, e -> user.sendMessage("skins.error-url"));
    }

    @Override
    public void disguise(Player player) {
        Skin random = DisguisePlusAPI.getSkinFactory().getRandomSkin();

        if (random == null) {
            player.sendMessage(ChatColor.RED + "Massive error is happening to the system, please contact the admins");
            return;
        }

        this.applySkin(player, random);

        // Apply a random nick aswell
        this.applyNickname(player, this.dp.getSettings().getCreator().createNick(), true);
    }

    @Override
    public void resetNickname(Player player) {
        // Apply the players original nickname
        User s = this.dp.getUserManager().getUser(player);

        // Apply the nick
        this.applyNickname(player, s.getDefaultName());

        // Send the success message
        s.sendMessage("player.nickname-reset");
    }

    @Override
    public void undisguise(Player player) {
        super.undisguise(player);
    }

}
