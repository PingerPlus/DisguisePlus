package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.user.DisguiseUser;

public class ResetDisguiseExecutor {

    private final DisguisePlus dp;

    public ResetDisguiseExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Remove disguise from yourself")
    @Require("permission.dp.undisguise")
    public void undisguise(@Sender DisguiseUser user) {
        final PlayerMeta meta = user.getActiveMeta();
        if (meta == null) {
            user.sendMessage("player.not-disguised");
            return;
        }

        this.dp.getUserManager().resetDisguise(user);
        user.sendMessage("player.success-undisguise");
    }

}
