package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import java.time.LocalDateTime;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.meta.PlayerMeta.Builder;
import net.pinger.disguiseplus.user.DisguiseUser;
import net.pinger.disguiseplus.rank.Rank;

import java.util.List;
import net.pinger.disguiseplus.utils.StringUtil;

public class RepeatDisguiseExecutor {

    private final DisguisePlus dp;

    public RepeatDisguiseExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Disguise again with this command")
    @Require("permission.dp.disguise")
    public void disguise(@Sender DisguiseUser user) {
        // Check if the user is not disguised
        // If so, cancel the command
        if (!user.isDisguised()) {
            user.sendMessage("player.not-disguised");
            return;
        }

        final PlayerMeta.Builder builder = user.newMetaBuilder();
        builder.setSkin(this.dp.getSkinFactory().getRandomSkin());
        builder.setName(StringUtil.randomize());

        if (!this.dp.getRankManager().isEnabled()) {
            this.dp.getUserManager().disguise(user);
            return;
        }

        final List<Rank> ranks = this.dp.getRankManager().getAvailableRanks(user);
        if (ranks == null || ranks.isEmpty()) {
            this.dp.getUserManager().disguise(user);
            return;
        }

        // Do the rank inventory
        this.dp.getInventoryManager().getRankInventory(ranks, this.dp.getUserManager()::disguise).open(user.transform());
        return;
    }
}
