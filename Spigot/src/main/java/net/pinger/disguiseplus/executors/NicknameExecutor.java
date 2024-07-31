package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.DisguiseUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class NicknameExecutor {

    private final DisguisePlus dp;

    public NicknameExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Set a custom nickname for yourself", usage = "<nickname>")
    @Require("permission.dp.nickname")
    public void setNickname(@Sender DisguiseUser user, @Nonnull String nickname) {
        // Check if the user is disguised
        if (user.isDisguised()) {
            user.sendMessage("player.currently-disguised");
            return;
        }

        // Check if the length of this nickname
        // Succeeds the allowed length
        if (nickname.length() < 3 || nickname.length() > 16) {
            user.sendMessage("player.invalid-nick");
            return;
        }

        // If another player already has this nickname
        // By nicking or just existing in the server
        // We want to disable this action
        if (!this.isValidName(nickname)) {
            user.sendMessage("player.invalid-nick");
            return;
        }

        this.dp.getProvider().updatePlayer(user.transform(), nickname);
        user.sendMessage("player.success-name", nickname);
    }

    private boolean isValidName(String name) {
        // Loop through all players online
        // And check whether they already have this name
        // If so, set this as invalid name
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        // Return that it's a valid name otherwise
        return true;
    }

}
