package net.pinger.disguise.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.OptArg;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguise.DisguisePlus;
import org.bukkit.entity.Player;

public class NickExecutor {

    private final DisguisePlus dp;

    public NickExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Change your nickname", usage = "<nickname> [player]")
    @Require(value = "perm.dp.nick")
    public void onExecuteNick(@Sender Player sender, String name, @OptArg() Player target) {
        if (target == null) {
            this.dp.getBaseManager().applyNickname(sender, name);
            return;
        }

        this.dp.getBaseManager().applyNickname(target, name);
    }


}
