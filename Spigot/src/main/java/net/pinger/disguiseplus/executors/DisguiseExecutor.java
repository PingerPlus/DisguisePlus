package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.entity.Player;

public class DisguiseExecutor {

    private final DisguisePlus disguisePlus;

    public DisguiseExecutor(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Command(name = "", desc = "Use this command to disguise yourself. It changes both your nickname and skin")
    @Require("permission.dp.disguise")
    public void disguise(@Sender Player sender) {
        this.disguisePlus.getExtendedManager().disguise(sender);
    }

}
