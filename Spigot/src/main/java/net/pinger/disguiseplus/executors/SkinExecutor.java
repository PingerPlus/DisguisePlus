package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.user.UserImpl;
import net.pinger.disguiseplus.statistic.SkinStatistic;
import org.bukkit.Bukkit;

public class SkinExecutor {

    private final DisguisePlus dp;

    public SkinExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Set a skin by giving a player name", usage = "<playerName>")
    @Require("permission.dp.skin")
    public void setSkin(@Sender UserImpl user, String playerName) {
        // Check if the user is disguised
        // If so, we do not want to continue the action
        if (user.isDisguised()) {
            user.sendMessage("player.currently-disguised");
            return;
        }

        // Do this as an async action, as it can stop the server
        Bukkit.getScheduler().runTaskAsynchronously(this.dp, () -> {
            try {
                Skin skin = DisguiseAPI.getSkinManager().getFromMojang(playerName);

                // If the found skin is null, we do not want to continue the action
                // But rather send an error message
                if (skin == null) {
                    user.sendMessage("skins.error-name", playerName);
                    return;
                }

                this.dp.getProvider().updatePlayer(user.transform(), skin);
                user.addStatistic(new SkinStatistic(skin));
                user.sendMessage("player.success-skin-name", playerName);
            } catch (Exception e) {
                user.sendMessage("skins.error-name", playerName);
            }
        });
    }

}
