package net.pinger.disguiseplus.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisguisePlusExpansion extends PlaceholderExpansion {

    private final DisguisePlus disguisePlus;

    public DisguisePlusExpansion(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "dp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "itspinger";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        // Get the user for this player
        User user = this.disguisePlus.getUserManager().getUser(player);

        // Empty if unknown
        if (user == null) {
            return "";
        }

        // Switch from the params
        if (params.equalsIgnoreCase("name")) {
            return user.getName();
        }

        // Check if the user is requesting
        // A prefix
        if (params.equalsIgnoreCase("prefix")) {
            return this.disguisePlus.getPlayerPrefix().toPrefix(user);
        }

        if (params.equalsIgnoreCase("rank")) {
            return user.getCurrentRank() == null ? "" : user.getCurrentRank().getDisplayName();
        }

        return "";
    }
}
