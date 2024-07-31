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
            this.disguiseUser(user);
            return;
        }

        final List<Rank> ranks = this.dp.getRankManager().getAvailableRanks(user);
        if (ranks != null && !ranks.isEmpty()) {
            this.disguiseUser(user);
            return;
        }

        // Do the rank inventory
        this.dp.getInventoryManager().getRankInventory(ranks, this::disguiseUser).open(user.transform());
        return;
    }

    private void disguiseUser(DisguiseUser user) {
        final Builder metaBuilder = user.getMetaBuilder();
        if (metaBuilder == null) {
            return;
        }

        // If previous meta has not been saved, we will save it
        final PlayerMeta activeMeta = user.getActiveMeta();
        if (activeMeta != null) {
            activeMeta.setEndTime(LocalDateTime.now());
            this.dp.getStorage().savePlayerMeta(user, activeMeta).join();
        }

        final PlayerMeta meta = metaBuilder.build();
        this.dp.getStorage().savePlayerMeta(user, meta).join();
        user.getMeta().add(meta);

        // Perform the user computations
        this.dp.getProvider().updatePlayer(user.transform(), meta.getSkin(), meta.getName());

        if (meta.getRank() != null) {
            this.dp.getVaultManager().setPrefix(user.transform(), meta.getRank());
        }
    }

}
