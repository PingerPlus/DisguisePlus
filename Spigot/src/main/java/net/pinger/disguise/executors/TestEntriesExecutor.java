package net.pinger.disguise.executors;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.settings.display.DisplaySettings;
import net.pinger.disguise.user.SimpleUser;
import org.bukkit.entity.Player;

public class TestEntriesExecutor {

    private final DisguisePlus dp;

    public TestEntriesExecutor(DisguisePlus dp) {
        this.dp = dp;
    }

    @Command(name = "", desc = "Test how entries look on yourself")
    @Require("dp.test.entries")
    public void onExecute(@Sender Player sender) {
        // Get the user
        SimpleUser user = (SimpleUser)
                this.dp.getUserManager().getUser(sender);

        DisplaySettings settings = this.dp.getSettings().getDisplaySettings();

        sender.sendMessage("");
    }
}
