package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.rank.Rank;

import java.util.List;

public class DisguiseExecutor {

    private final DisguisePlus dp;

    public DisguiseExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Use this command to disguise yourself. It changes both your nickname and skin")
    @Require("permission.dp.disguise")
    public void disguise(@Sender UserImpl user) {
        // Check if this user is already disguised?
        if (user.isDisguised()) {
            user.sendMessage("player.already-disguised");
            return;
        }

        //// Check if they have either nick or skin?
        //if (user.hasSkinApplied() || user.hasNickname()) {
        //    user.sendMessage("player.failed-disguise");
        //    return;
        //}

        if (this.dp.getRankManager().isEnabled()) {
            // Check for player permission
            List<Rank> ranks = this.dp.getRankManager().getAvailableRanks(user);

            if (ranks != null && !ranks.isEmpty()) {
                // Do the rank inventory
                this.dp.getInventoryManager().getRankInventory(ranks).open(user.transform());
                return;
            }
        }

        this.dp.getManager().disguise(user);
    }

}
