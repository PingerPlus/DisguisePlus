package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.entity.Player;
import org.bukkit.event.server.TabCompleteEvent;

import javax.annotation.Nonnull;

public class NicknameExecutor {

    private final DisguisePlus dp;

    public NicknameExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Set a custom nickname for yourself", usage = "<nickname>")
    @Require("permission.dp.nickname")
    public void setNickname(@Sender Player sender, @Nonnull String nickname) {
        this.dp.getExtendedManager().setNickname(sender, nickname);
    }

}
