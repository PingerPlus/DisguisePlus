package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.user.DisguiseUser;
import net.pinger.disguiseplus.rank.Rank;

import java.util.List;
import net.pinger.disguiseplus.utils.StringUtil;

public class DisguiseExecutor {

    private final DisguisePlus dp;

    public DisguiseExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Use this command to disguise yourself. It changes both your nickname and skin")
    @Require("permission.dp.disguise")
    public void disguise(@Sender DisguiseUser user) {
        final PlayerMeta.Builder builder = user.newMetaBuilder();
        builder.skin(this.dp.getSkinFactory().getRandomSkin());
        builder.name(StringUtil.randomize());

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
