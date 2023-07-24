package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.statistic.SkinStatistic;

public class ResetSkinExecutor {

    private final DisguisePlus dp;

    public ResetSkinExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Reset skin for yourself")
    @Require("permission.dp.skin")
    public void resetSkin(@Sender UserImpl user) {
        // Check if they have the skin applied
        // If they don't, we skip this action
        if (!user.hasSkinApplied()) {
            user.sendMessage("player.failed-reset-skin");
            return;
        }

        this.dp.getProvider().resetPlayerSkin(user.transform());
        user.removeStatistic(SkinStatistic.class);
        user.sendMessage("player.skin-reset");
    }

}
