package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.statistic.NickStatistic;

public class ResetNicknameExecutor {

    private final DisguisePlus dp;

    public ResetNicknameExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Reset the nickname for yourself")
    @Require("permission.dp.nickname")
    public void resetNickname(@Sender UserImpl user) {
        // Check if the user is nicked?
        // If they are not nicked, we don't have anything to reset
        if (!user.hasNickname()) {
            user.sendMessage("player.failed-nick-reset");
            return;
        }

        this.dp.getProvider().resetPlayerName(user.transform());
        user.sendMessage("player.nickname-reset");
        user.removeStatistic(NickStatistic.class);
    }

}
