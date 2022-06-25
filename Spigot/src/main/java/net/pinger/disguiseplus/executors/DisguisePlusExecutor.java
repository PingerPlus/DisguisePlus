package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.entity.Player;

public class DisguisePlusExecutor {

    private final DisguisePlus disguisePlus;

    public DisguisePlusExecutor(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Command(name = "", desc = "Configure the Disguise+ plugin.")
    @Require(value = "permission.dp.modify")
    public void onCommand(@Sender Player sender) {
        this.disguisePlus.getInventoryManager().getDisguisePlusProvider().open(sender);
    }
}
