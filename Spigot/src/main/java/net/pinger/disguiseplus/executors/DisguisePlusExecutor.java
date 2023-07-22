package net.pinger.disguiseplus.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisguisePlusExecutor {

    private final DisguisePlus dp;

    public DisguisePlusExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Configure the Disguise+ plugin.")
    @Require(value = "permission.dp.modify")
    public void onCommand(@Sender Player sender) {
        this.dp.getInventoryManager().getDisguisePlusProvider().open(sender);
    }

    @Command(name = "reload", desc = "Reload the configuration")
    @Require(value = "permission.dp.reload")
    public void reload(@Sender CommandSender sender) {
        this.dp.getFeatureManager().reload();
        sender.sendMessage(ChatColor.AQUA + "[Disguise+] Reloaded configuration files.");
    }
}
