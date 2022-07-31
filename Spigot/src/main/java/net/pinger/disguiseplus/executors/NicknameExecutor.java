package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.User;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class NicknameExecutor {

    private final DisguisePlus disguisePlus;

    public NicknameExecutor(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Command(name = "", desc = "Set a custom nickname for yourself", usage = "<nickname>")
    @Require("permission.dp.nickname")
    public void setNickname(@Sender Player sender, @Nonnull String nickname) {
        this.disguisePlus.getManager().setNickname(sender, nickname);

        // Send a success message to this player
        User user = this.disguisePlus.getUserManager().getUser(sender);
        user.sendMessage("player.success-name", nickname);
    }

}
