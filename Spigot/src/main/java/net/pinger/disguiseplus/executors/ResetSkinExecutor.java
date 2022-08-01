package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.entity.Player;

public class ResetSkinExecutor {

    private final DisguisePlus disguisePlus;

    public ResetSkinExecutor(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Command(name = "", desc = "Reset skin for yourself")
    @Require("permission.dp.skin")
    public void resetSkin(@Sender Player sender) {
        this.disguisePlus.getExtendedManager().resetSkin(sender);
    }

}
