package net.pinger.disguiseplus.internal;

import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.Skin;
import net.pinger.disguise.exception.UserNotFoundException;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.statistic.DisguiseStatistic;
import net.pinger.disguiseplus.statistic.NickStatistic;
import net.pinger.disguiseplus.statistic.SkinStatistic;
import net.pinger.disguiseplus.user.User;
import net.pinger.disguiseplus.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ExtendedDisguiseManager extends DisguiseManagerImpl {

    public ExtendedDisguiseManager(DisguisePlus dp, PacketProvider provider) {
        super(dp, provider);
    }

    @Override
    public void applySkinFromName(Player player, @Nonnull String playerName) {
        // Get the user from the player
        User user = this.dp.getUserManager().getUser(player);

        // Check if the user is disguised
        // If so, we do not want to continue the action
        if (user.isDisguised()) {
            user.sendMessage("player.currently-disguised");
            return;
        }

        // Fetch the skin
        try {
            Skin skin = DisguiseAPI.getSkinManager().getFromMojang(playerName);

            if (skin == null) {
                user.sendMessage("skins.error-name", playerName);
                return;
            }

            super.applySkin(player, skin);
            user.addStatistic(new SkinStatistic(skin));
        } catch (UserNotFoundException e) {
            user.sendMessage("skins.error-name", playerName);
            return;
        }

        user.sendMessage("player.success-name", playerName);
    }

    @Override
    public void resetSkin(Player player) {
        // Get the user from the player
        User user = this.dp.getUserManager().getUser(player);

        // Check if they have the skin applied
        if (!user.hasSkinApplied()) {
            user.sendMessage("player.failed-reset-skin");
            return;
        }

        super.resetSkin(player);
        user.removeStatistic(SkinStatistic.class);
        user.sendMessage("player.skin-reset");
    }

    @Override
    public void setNickname(Player player, @Nonnull String nickname) {
        // Get the user from the player
        User user = this.dp.getUserManager().getUser(player);

        // Check if the user is disguised
        if (user.isDisguised()) {
            user.sendMessage("player.currently-disguised");
            return;
        }

        // Check if the length of this nickname
        // Succeeds the allowed length
        if (nickname.length() < 3 || nickname.length() > 16) {
            user.sendMessage("player.invalid-nick");
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(nickname)) {
                user.sendMessage("player.invalid-nick");
                return;
            }
        }

        super.setNickname(player, nickname);
        user.sendMessage("player.success-name", nickname);
        user.addStatistic(new NickStatistic(nickname));
    }

    @Override
    public void resetNickname(Player player) {
        // Get the user from the player
        User user = this.dp.getUserManager().getUser(player);

        // Check if the user is nicked?
        if (!user.hasNickname()) {
            user.sendMessage("player.failed-nick-reset");
            return;
        }

        super.updatePlayerNickname(player, user.getDefaultName());
        this.provider.sendServerPackets(player);
        user.sendMessage("player.nickname-reset");
        user.removeStatistic(NickStatistic.class);
    }

    @Override
    public void disguise(Player player) {
        // Get the user from the player
        User user = this.dp.getUserManager().getUser(player);

        // Check if they have either nick or skin?
        if (user.hasSkinApplied() || user.hasNickname()) {
            user.sendMessage("player.failed-disguise");
            return;
        }

        // Try to get a random skin and nick
        // From the factory
        Skin randomSkin = this.dp.getSkinFactory().getRandomSkin();
        String nickname = StringUtil.randomize();

        // Check if the random skin is null
        // And if so apply the default value
        // Which is the skin of ITSPINGER
        if (randomSkin == null) {
            randomSkin = DisguiseAPI.getSkinManager().getFromMojang("ITSPINGER");
        }

        // Now apply the skin to the player
        super.updatePlayerNickname(player, nickname);
        this.applySkin(player, randomSkin);
        user.addStatistic(new DisguiseStatistic(randomSkin, nickname));
        user.sendMessage("player.success-disguise");
    }

    @Override
    public void undisguise(Player player) {
        // Get the user from the player
        User user = this.dp.getUserManager().getUser(player);

        if (!user.isDisguised()) {
            user.sendMessage("player.not-disguised");
            return;
        }

        // Clear the properties of this player
        this.provider.clearProperties(player);

        // Reset the player nickname
        this.updatePlayerNickname(player, user.getDefaultName());
        user.sendMessage("player.success-undisguise");
        user.removeStatistic(DisguiseStatistic.class);

        // Here we need to check for NickMatching
        // Condition
        if (!this.dp.getPlayerMatcher().matches(player)) {
            this.provider.sendServerPackets(player);
            return;
        }

        // Apply the skin here
        super.applySkinFromName(player, player.getName());
    }

}
