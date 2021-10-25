package net.pinger.disguise.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguise.DisguisePlus;
import org.bukkit.entity.Player;

public class DatabaseExecutor {

    private final DisguisePlus disguisePlus;

    public DatabaseExecutor(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Command(name = "", desc = "Configure the DisguisePlus plugin.")
    public void onCommand(@Sender Player sender) {
        if (!sender.isOp())
            return;

        this.disguisePlus.getInventoryManager().getDisguisePlusProvider().open(sender);
    }
}
