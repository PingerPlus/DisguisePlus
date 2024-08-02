package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.DisguisePlayer;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.meta.PlayerMeta.Builder;
import net.pinger.disguiseplus.user.DisguiseUser;

public class ResetNicknameExecutor {

    private final DisguisePlus dp;

    public ResetNicknameExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Reset the nickname for yourself")
    @Require("permission.dp.nickname")
    public void resetNickname(@Sender DisguiseUser user) {
        final PlayerMeta meta = user.getActiveMeta();
        final DisguisePlayer player = DisguiseAPI.getDisguisePlayer(user.transform());
        if (meta == null || meta.getName().equals(player.getDefaultName())) {
            user.sendMessage("player.failed-nick-reset");
            return;
        }

        final Builder builder = Builder.copyOf(meta).setName(player.getDefaultName());
        this.dp.getUserManager().disguise(user, builder.build());
        user.sendMessage("player.nickname-reset");
    }

}
