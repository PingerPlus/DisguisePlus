package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta.Builder;
import net.pinger.disguiseplus.user.DisguiseUser;
import org.bukkit.Bukkit;

public class SkinExecutor {

    private final DisguisePlus dp;

    public SkinExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Set a skin by giving a player name", usage = "<playerName>")
    @Require("permission.dp.skin")
    public void setSkin(@Sender DisguiseUser user, String playerName) {
        // Do this as an async action, as it can stop the server
        Bukkit.getScheduler().runTaskAsynchronously(this.dp, () -> {
            try {
                Skin skin = DisguiseAPI.getSkinManager().getFromMojang(playerName);
                if (skin == null) {
                    user.sendMessage("skins.error-name", playerName);
                    return;
                }

                Bukkit.getScheduler().runTask(this.dp, () -> {
                    final Builder builder = user.copyActiveMeta().skin(skin);
                    this.dp.getUserManager().disguise(user, builder.build());
                });

                user.sendMessage("player.success-skin-name", playerName);
            } catch (Exception e) {
                e.printStackTrace();
                user.sendMessage("skins.error-name", playerName);
            }
        });
    }

}
