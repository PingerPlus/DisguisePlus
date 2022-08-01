package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.User;
import org.bukkit.entity.Player;

public class RepeatDisguiseExecutor {

    private final DisguisePlus disguisePlus;

    public RepeatDisguiseExecutor(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Command(name = "", desc = "Disguise again with this command")
    @Require("permission.dp.disguise")
    public void disguise(@Sender Player sender) {
        // Get the user from this player
        User user = this.disguisePlus.getUserManager().getUser(sender);

        // Check if the user is not disguised
        // If so, cancel the command
        if (!user.isDisguised()) {
            user.sendMessage("player.not-disguised");
            return;
        }

        this.disguisePlus.getExtendedManager().disguise(sender);
    }

}
