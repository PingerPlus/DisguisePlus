package net.pinger.disguise.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.user.User;
import org.bukkit.entity.Player;

public class DisguiseExecutor {

    private final DisguisePlus dp;

    public DisguiseExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "This command allows users to disguise themselves")
    @Require("dp.disguise")
    public void onExecute(@Sender Player sender) {
        User user = this.dp.getUserManager().getUser(sender);

        // Check if the player is nicked or skinned
        if (true) {
            user.sendMessage("");
            return;
        }

        if (user.getCooldown().isActive()) {
            user.sendMessage("cooldown.active");
            return;
        }

        if (this.dp.getSettings().isWorldDisabled(sender.getWorld().getName())) {
            user.sendMessage("world.disabled");
            return;
        }

        this.dp.getManager().disguise(sender);
    }

}
