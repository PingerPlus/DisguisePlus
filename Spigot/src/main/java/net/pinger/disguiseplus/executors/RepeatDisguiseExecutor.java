package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class RepeatDisguiseExecutor {

    private final DisguisePlus dp;

    public RepeatDisguiseExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Disguise again with this command")
    @Require("permission.dp.disguise")
    public void disguise(@Sender UserImpl sender) {
        // Check if the user is not disguised
        // If so, cancel the command
        if (!sender.isDisguised()) {
            sender.sendMessage("player.not-disguised");
            return;
        }

        if (this.dp.getRankManager().isEnabled()) {
            // Check for player permission
            List<Rank> ranks = this.dp.getRankManager().getAvailableRanks(sender);
            if (ranks != null && !ranks.isEmpty()) {
                // Do the rank inventory
                this.dp.getInventoryManager().getRankInventory(ranks).open(sender);
                return;
            }
        }

        this.dp.getExtendedManager().disguise(sender.transform());
    }

}
