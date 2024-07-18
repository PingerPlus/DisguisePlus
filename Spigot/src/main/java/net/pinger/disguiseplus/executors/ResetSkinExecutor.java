package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.user.UserImpl;

public class ResetSkinExecutor {

    private final DisguisePlus dp;

    public ResetSkinExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Reset skin for yourself")
    @Require("permission.dp.skin")
    public void resetSkin(@Sender UserImpl user) {
        this.dp.getProvider().resetPlayerSkin(user.transform());
        user.sendMessage("player.skin-reset");
    }

}
