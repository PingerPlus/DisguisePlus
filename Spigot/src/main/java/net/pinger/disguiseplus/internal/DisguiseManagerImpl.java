package net.pinger.disguiseplus.internal;

import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.DisguiseProvider;
import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.DisguiseManager;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.event.PlayerDisguiseEvent;
import net.pinger.disguiseplus.statistic.DisguiseStatistic;
import net.pinger.disguiseplus.user.User;
import net.pinger.disguiseplus.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DisguiseManagerImpl implements DisguiseManager {

    protected final DisguisePlus dp;
    protected final DisguiseProvider provider;

    public DisguiseManagerImpl(DisguisePlus dp) {
        this.dp = dp;
        this.provider = dp.getProvider();
    }

    @Override
    public void disguise(User user) {
        Player player = user.transform();

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

        // Call the disguise event
        // Maybe remove this?
        Bukkit.getPluginManager().callEvent(new PlayerDisguiseEvent(player));

        // Now apply the skin to the player
        this.dp.getProvider().updatePlayer(player, randomSkin, nickname);
        user.addStatistic(new DisguiseStatistic(randomSkin, nickname));
        user.sendMessage("player.success-disguise");
    }
}
