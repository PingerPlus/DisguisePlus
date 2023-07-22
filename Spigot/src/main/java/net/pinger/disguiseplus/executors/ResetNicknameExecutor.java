package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.entity.Player;

public class ResetNicknameExecutor {

    private final DisguisePlus dp;

    public ResetNicknameExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Reset the nickname for yourself")
    @Require("permission.dp.nickname")
    public void resetNickname(@Sender Player sender) {
        this.dp.getExtendedManager().resetNickname(sender);
    }

}
