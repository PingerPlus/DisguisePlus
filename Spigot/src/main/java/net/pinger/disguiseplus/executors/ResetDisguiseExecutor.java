package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.event.PlayerRemoveDisguiseEvent;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.statistic.DisguiseStatistic;
import net.pinger.disguiseplus.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ResetDisguiseExecutor {

    private final DisguisePlus dp;

    public ResetDisguiseExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Remove disguise from yourself")
    @Require("permission.dp.undisguise")
    public void undisguise(@Sender UserImpl user) {
        Player player = user.transform();

        // If the user is not disguised
        // There is no point in resetting the disguise?
        if (!user.isDisguised()) {
            user.sendMessage("player.not-disguised");
            return;
        }

        // Clear the properties of this player
        this.dp.getProvider().resetPlayer(player);

        // Call the undisguised event
        // We will remove this probably?
        Bukkit.getPluginManager().callEvent(new PlayerRemoveDisguiseEvent(player));

        // Reset the player nickname
        user.sendMessage("player.success-undisguise");
        user.removeStatistic(DisguiseStatistic.class);
        user.setRank(null);
    }

}
