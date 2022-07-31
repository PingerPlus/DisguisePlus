package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.User;
import org.bukkit.entity.Player;

public class ResetNicknameExecutor {

    private final DisguisePlus disguisePlus;

    public ResetNicknameExecutor(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Command(name = "", desc = "Reset the nickname for yourself")
    @Require("permission.dp.nickname")
    public void resetNickname(@Sender Player sender) {
        this.disguisePlus.getManager().resetNickname(sender);

        // Send a success message
        User user = this.disguisePlus.getUserManager().getUser(sender);
        user.sendMessage("player.nickname-reset");
    }

}
