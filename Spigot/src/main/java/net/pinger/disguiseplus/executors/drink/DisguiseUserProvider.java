package net.pinger.disguiseplus.executors.drink;

import com.jonahseguin.drink.argument.CommandArg;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.user.UserImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;

public class DisguiseUserProvider extends DrinkProvider<UserImpl> {
    private final DisguisePlus dp;

    public DisguiseUserProvider(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public boolean doesConsumeArgument() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Nullable
    @Override
    public UserImpl provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws CommandExitMessage {
        if (!arg.isSenderPlayer()) {
            throw new CommandExitMessage("Sender must be a player.");
        }

        final UserImpl player = this.dp.getUserManager().getUser(arg.getSenderAsPlayer().getUniqueId());
        if (player == null) {
            throw new CommandExitMessage("No player with name " + arg.getSenderAsPlayer().getName());
        }

        return player;
    }

    @Override
    public String argumentDescription() {
        return "user";
    }
}
