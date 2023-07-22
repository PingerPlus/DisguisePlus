package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.entity.Player;

public class SkinExecutor {

    private final DisguisePlus dp;

    public SkinExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Set a skin by giving a player name", usage = "<playerName>")
    @Require("permission.dp.skin")
    public void setSkin(@Sender Player sender, String playerName) {
        this.dp.getExtendedManager().applySkinFromName(sender, playerName);
    }

}
