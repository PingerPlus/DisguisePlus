package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.entity.Player;

public class ResetDisguiseExecutor {

    private final DisguisePlus disguisePlus;

    public ResetDisguiseExecutor(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Command(name = "", desc = "Remove disguise from yourself")
    @Require("permission.dp.undisguise")
    public void undisguise(@Sender Player sender) {
        this.disguisePlus.getExtendedManager().undisguise(sender);
    }

}
