package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.entity.Player;

public class ResetNicknameExecutor {

    private final DisguisePlus disguisePlus;

    public ResetNicknameExecutor(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Command(name = "", desc = "Reset the nickname for yourself")
    @Require("permission.dp.nickname")
    public void resetNickname(@Sender Player sender) {
        this.disguisePlus.getExtendedManager().resetNickname(sender);
    }

}
